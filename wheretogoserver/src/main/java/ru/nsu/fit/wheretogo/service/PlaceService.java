package ru.nsu.fit.wheretogo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.repository.PlaceRepository;
import ru.nsu.fit.wheretogo.repository.UserRepository;
import ru.nsu.fit.wheretogo.service.fetch.PlaceFetchParameters;
import ru.nsu.fit.wheretogo.utils.SortDirection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PlaceService {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;////EntityManager это интерфейс, который описывает API для всех основных операций над Enitity, получение данных и других сущностей JPA. По сути главный API для работы с JPA
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void savePlace(PlaceDescriptionDTO place) {
        validatePlace(place);
        placeRepository.save(Place.getFromDTO(place));
    }

    private void validatePlace(PlaceDescriptionDTO place) {
        if (placeRepository.existsByNameOrDescriptionOrCoords(place.getName(), place.getDescription(), place.getCoords())) {
            throw new ValidationException("Place with such name, description or coords is already exist");
        }
    }

    @Transactional
    public void deletePlace(PlaceDescriptionDTO place) {
        if (place == null || place.getId() == null) {
            return;
        }
        placeRepository.deleteById(place.getId());
    }

    @Transactional
    public PlaceDescriptionDTO getPlaceById(Long id) {
        if (id == null) {
            return null;
        }
        return PlaceDescriptionDTO.getFromEntity(placeRepository.findById(id).orElse(null));
    }

    //Получение списка ближайших мест к определенной точке на карте
    //заданной координатами (myLat, myLon)
    @Transactional
    public PagedListDTO<PlaceBriefDTO> getNearestPlacesToPoint(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit,
            int page,
            int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Place> places = placeRepository.findNearestPlaces(myLat, myLon, startDist, maxDist, limit, pageRequest);
        List<PlaceBriefDTO> nearestPlaceDtos = places.toList()
                .stream()
                .map(PlaceBriefDTO::getFromEntity)
                .collect(toList());
        return new PagedListDTO<PlaceBriefDTO>()
                .setList(nearestPlaceDtos)
                .setPageNum(page)
                .setTotalPages(places.getTotalPages());
    }

    //Получение списка ближайших мест к определенной точке на карте с учтом выбранных категорий
    @Transactional
    public PagedListDTO<PlaceBriefDTO> getNearestPlacesByCategory(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit,
            String categoryIds,
            int page,
            int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Place> places = placeRepository.findNearestByCategory(
                myLat,
                myLon,
                startDist,
                maxDist,
                limit,
                categoryIds,
                pageRequest
        );
        List<PlaceBriefDTO> nearestPlaceDtos = places.toList()
                .stream()
                .map(PlaceBriefDTO::getFromEntity)
                .collect(toList());
        return new PagedListDTO<PlaceBriefDTO>()
                .setList(nearestPlaceDtos)
                .setPageNum(page)
                .setTotalPages(places.getTotalPages());
    }

    //Получение списка блжиашйих мест к тем, которые пользователь посетил
    @Transactional
    public PagedListDTO<PlaceBriefDTO> getNearestPlacesByVisited(
            Long userId,
            int page,
            int size
    ){
        //Ищем пользователя по его id/email в базе данных и берем список посещенных им мест
        List<PlaceBriefDTO> visitedPlaces = userRepository.findById(userId).orElseThrow().getVisitedPlaces().stream().map(
                PlaceBriefDTO::getFromEntity).toList();

        PageRequest pageRequest = PageRequest.of(page, size);

        //Создаем список из id посещенных мест (мест, исключенных из поиска)
        List<Long> isolatorIds = new ArrayList<>();
        for(PlaceBriefDTO place : visitedPlaces){
            isolatorIds.add(place.getId());
        }

        //Формируем из списка строку id посещенных мест, чтобы передать их в качестве исключенных кандидатов при поиске ближайших
        String isolators = isolatorIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        //Формируем список рекомендаций, который будет отправлен в конечном итоге
        List<PlaceBriefDTO> recommendations = new ArrayList<>();
        int totalPages = 0;

        //Обходим все места в списке посещенных, извлекаем координаты из каждого из них, используем их в качестве параметров для вызова
        //функции поиска ближайших мест к этим местам
        for(PlaceBriefDTO place : visitedPlaces){
            //Извлекаем координаты посещенного места
            Coords placeCoords = place.getCoords();

            //Отправляем запрос на поиск ближайших мест к данному посещенному
            Page<Place> currentVisitedPlaces = placeRepository.findNearestByVisited(
                    placeCoords.getLatitude().doubleValue(),
                    placeCoords.getLongitude().doubleValue(),
                    1.0,
                    5.0,
                    2,
                    isolators,
                    pageRequest
            );

            //Обновляем число полученных страниц для финального списка
            totalPages += currentVisitedPlaces.getTotalPages();

            //Конвертируем страницу в список
            List<PlaceBriefDTO> currentPlaceRecommendations = currentVisitedPlaces.toList()
                    .stream()
                    .map(PlaceBriefDTO::getFromEntity).toList();

            //Добавляем полученный список к результирующему
            recommendations.addAll(currentPlaceRecommendations);

            //Обновляем список изоляторов, чтобы исключить их при поиске в последующем
            for(PlaceBriefDTO recPlace : currentPlaceRecommendations){
                isolatorIds.add(recPlace.getId());
            }
            //Оновляем строку-параметров с дополнительными
            isolators = isolatorIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        }

        return new PagedListDTO<PlaceBriefDTO>()
                .setList(recommendations)
                .setPageNum(page)
                .setTotalPages(totalPages);
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
           if  (parameters.categoryIds().contains(Integer.MAX_VALUE)) {
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
