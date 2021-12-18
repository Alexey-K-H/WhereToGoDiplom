package ru.nsu.fit.wheretogo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.repository.PlaceRepository;
import ru.nsu.fit.wheretogo.service.fetch.PlaceFetchParameters;
import ru.nsu.fit.wheretogo.utils.SortDirection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;////EntityManager это интерфейс, который описывает API для всех основных операций над Enitity, получение данных и других сущностей JPA. По сути главный API для работы с JPA
    private final PlaceRepository repository;

    @Transactional
    public void savePlace(PlaceDescriptionDTO place) {
        validatePlace(place);
        repository.save(Place.getFromDTO(place));
    }

    private void validatePlace(PlaceDescriptionDTO place) {
        if (repository.existsByNameOrDescriptionOrCoords(place.getName(), place.getDescription(), place.getCoords())) {
            throw new ValidationException("Place with such name, description or coords is already exist");
        }
    }

    @Transactional
    public void deletePlace(PlaceDescriptionDTO place) {
        if (place == null || place.getId() == null) {
            return;
        }
        repository.deleteById(place.getId());
    }

    @Transactional
    public PlaceDescriptionDTO getPlaceById(Long id) {
        if (id == null) {
            return null;
        }
        return PlaceDescriptionDTO.getFromEntity(repository.findById(id).orElse(null));
    }

    //criteriabuilder - интерфейс, который юзается для построения запросов
//Predicate встроенный функциональный интерфейс, добавленный в Java SE 8 в пакет java.util.function.
//Принимает на вход значение, проверяет состояние и возвращает boolean значение в качестве результата.
//Predicate подтверждает какое-то значение как true или false
    //root-интерфейс, который наследуется от Form - обычно сущность, которая появляется в предложении from, содержит базовые методы для join таблиц, root-корневой интерфейс, чтобы строить критерий для сущности

    private List<Predicate> getPredicates(PlaceFetchParameters parameters, CriteriaBuilder criteriaBuilder, Root<Place> root) {
        List<Predicate> predicates = new LinkedList<>();////создаем двунаправленный список
        if (parameters.name() != null && !parameters.name().equals("")) {
            //   если строка с именем непустая и ненулевая
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + parameters.name().toLowerCase() + "%"));//то же самое что и в фильтре WHERE name LIKE %...%
            // многоточие-это строчка, которую в имени найти надо
        }
        if (parameters.categoryIds() != null  && !parameters.categoryIds().isEmpty()) {
            Join<Place, Category> categoryJoin = root.join("categories", JoinType.LEFT);////сперва необходимо определять соединение со связанной сущностью путем вызова одного из методов From.join для корневого объекта запроса или другого объекта соединения
//            Predicate categoryPredicate = criteriaBuilder.equal(categoryJoin.get("id"), parameters.categoryIds());//сраниваем айди из параметров и из джойна, в случае эквпивалентности пихаем в categoryPredicate
//            predicates.add(categoryPredicate);//добавляем в список предикаты
            //TODO:Ниже выполняется тоже самое, только все сразу в одном вызове без создание построителя условия извне
            Predicate criteriaPredicate = criteriaBuilder.or(parameters.categoryIds().stream()
                    .filter(categoryId -> categoryId != Integer.MAX_VALUE)
                    .map(categoryId -> criteriaBuilder.equal(categoryJoin.get("id"), categoryId))
                    .toArray(Predicate[]::new));
            //TODO:Проверка для случая, когда фильтры пустые (наверное, уточни у Ильи)
            if (parameters.categoryIds().contains(Integer.MAX_VALUE)) {
                criteriaPredicate = criteriaBuilder.or(criteriaPredicate, categoryJoin.isNull());
            }
            predicates.add(criteriaPredicate);
        }
        return predicates;//в списке предикатов будут сами параметры
    }

    public PagedListDTO<PlaceBriefDTO> getPlaceList(PlaceFetchParameters parameters) {
        long pages = getPlaceListPages(parameters);////вернет количество страниц для мест
        return new PagedListDTO<PlaceBriefDTO>(
                getPlaceListData(parameters).stream().map(PlaceBriefDTO::getFromEntity).toList(),
                (int)(pages / parameters.pageSize() + (pages % parameters.pageSize() == 0 ? 0 : 1)),
                parameters.page()
        );
    }

    private long getPlaceListPages(PlaceFetchParameters parameters) {
        EntityManager countEntityManager = entityManagerFactory.createEntityManager();//менеджер сущностей
        try {
            countEntityManager.getTransaction().begin();//начинаем транзакцию
            CriteriaBuilder countCriteriaBuilder = countEntityManager.getCriteriaBuilder();//начало запроса на получения колиества страниц
            CriteriaQuery<Long> queryCount = countCriteriaBuilder.createQuery(Long.class);;//указываем явно Long, чтобы не кастовать
            Root<Place> countRoot = queryCount.from(Place.class);////Определяет и возвращает элемент в предложении from запроса для класса сущности

            List<Predicate> countPredicates = getPredicates(parameters, countCriteriaBuilder, countRoot);////получаем список параметров для запроса

            queryCount.where(countCriteriaBuilder.and(countPredicates.toArray(new Predicate[0])));////добпаляем в countCriteriaBuilder список предикатов

            Long pages = countEntityManager.createQuery(queryCount.select(countCriteriaBuilder.countDistinct(countRoot))).getSingleResult();////в pages записывает результат select записываем число страниц ненулевых,getSingleResult - для создание только 1 объекта, .countDistinct - для получения колиечства

            countEntityManager.getTransaction().commit();

            return pages;//возвращает количество страниц для мест
        } finally {
            countEntityManager.close();
        }
    }
    //для получения списка мест по страницам

    private List<Place> getPlaceListData(PlaceFetchParameters parameters) {
        EntityManager dataEntityManager = entityManagerFactory.createEntityManager();//менеджер сущностей
        try {
            dataEntityManager.getTransaction().begin();
            CriteriaBuilder dataCriteriaBuilder = dataEntityManager.getCriteriaBuilder();//начало запроса длы получения списка мест по страницам
            CriteriaQuery<Place> queryData = dataCriteriaBuilder.createQuery(Place.class);//указываем явно значение возвращаемых данных
            Root<Place> dataRoot = queryData.from(Place.class);///Определяет и возвращает элемент в предложении from запроса для класса сущности

            if (parameters.sortDirection() == SortDirection.ASC) {
                queryData.orderBy(dataCriteriaBuilder.asc(dataRoot.get(parameters.sortBy().getFieldName())));
            } else if (parameters.sortDirection() == SortDirection.DESC) {
                queryData.orderBy(dataCriteriaBuilder.desc(dataRoot.get(parameters.sortBy().getFieldName())));
            }
            //упорядочиваем результаты - либо по возрастанию/либо по убыванию

            List<Predicate> dataPredicates = getPredicates(parameters, dataCriteriaBuilder, dataRoot);//получаем список параметров для запроса

            queryData.where(dataCriteriaBuilder.and(dataPredicates.toArray(new Predicate[0])));//добпаляем в dataCriteriaBuilder список предикатов

            queryData.distinct(true);//избегаем дублирования результатов

            List<Place> resultData = dataEntityManager.createQuery(queryData.select(dataRoot))
                    .setFirstResult(parameters.page() * parameters.pageSize())
                    .setMaxResults(parameters.pageSize()).getResultList();////утсанавливаем начальный и максимальный реузльтат в запросе

            resultData.forEach((p) -> {
                p.getCategories().size();
                p.getPicturesLinks().size();////для каждого места подгружаем категории и места
            });

            dataEntityManager.getTransaction().commit();

            return resultData;////возвращаем список мест по страницам
        } finally {
            dataEntityManager.close();
        }
    }

}
