package ru.nsu.fit.wheretogo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.dto.StayPointDTO;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.User;
import ru.nsu.fit.wheretogo.entity.user_coeff.UserCoefficient;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.recommenders.cbf.CBFRecommender;
import ru.nsu.fit.wheretogo.recommenders.cbf.UserVectorBuilder;
import ru.nsu.fit.wheretogo.recommenders.cf.SlopeOne;
import ru.nsu.fit.wheretogo.repository.*;
import ru.nsu.fit.wheretogo.utils.SecurityContextHelper;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PlaceService {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;////EntityManager это интерфейс, который описывает API для всех основных операций над Enitity, получение данных и других сущностей JPA. По сути главный API для работы с JPA

    //набор репозиториев для работы с местами
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ScoreRepository scoreRepository;

    private final UserCoeffRepository userCoeffRepository;

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
            int page,
            int size
    ){
        //Ищем пользователя по его id/email в базе данных и берем список посещенных им мест
        List<PlaceBriefDTO> visitedPlaces = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getVisitedPlaces().stream().map(
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

    @Transactional
    public PagedListDTO<PlaceBriefDTO> getNearestPlacesByStayPoints(
            int page,
            int size
    ){
        //Ищем пользователя по его id/email в базе данных и берем его stay-point-ы
        List<StayPointDTO> stayPoints = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getStayPoints().stream().map(
                StayPointDTO::getFromEntity).toList();
        PageRequest pageRequest = PageRequest.of(page, size);

        //Создаем список из id посещенных мест (мест, исключенных из поиска), если они есть
        List<PlaceBriefDTO> visitedPlaces = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getVisitedPlaces().stream().map(
                PlaceBriefDTO::getFromEntity).toList();

        String isolators;

        //Формируем список рекомендаций, который будет отправлен в конечном итоге
        List<PlaceBriefDTO> recommendations = new ArrayList<>();
        int totalPages = 0;

        if(visitedPlaces.isEmpty()){
            for(StayPointDTO stayPoint : stayPoints){
                //Отправляем запрос на поиск ближайших мест к данному stay-point-у
                Page<Place> currentStayPointPlaces = placeRepository.findNearestPlaces(
                        stayPoint.getLatitude(),
                        stayPoint.getLongitude(),
                        1.0,
                        5.0,
                        2,
                        pageRequest
                );

                //Обновляем число полученных страниц для финального списка
                totalPages += currentStayPointPlaces.getTotalPages();

                //Конвертируем страницу в список
                List<PlaceBriefDTO> currentPlaceRecommendations = currentStayPointPlaces.toList()
                        .stream()
                        .map(PlaceBriefDTO::getFromEntity).toList();

                //Добавляем полученный список к результирующему
                recommendations.addAll(currentPlaceRecommendations);

                //Убираем дубликаты из списка мест
                recommendations = recommendations.stream().distinct().collect(toList());
            }
        }else {
            //Обходим все места в списке посещенных, извлекаем координаты из каждого из них, используем их в качестве параметров для вызова
            //функции поиска ближайших мест к этим местам
            List<Long> isolatorsVisited = new ArrayList<>();
            for(PlaceBriefDTO place : visitedPlaces){
                isolatorsVisited.add(place.getId());
            }

            //Формируем из списка строку id посещенных мест, чтобы передать их в качестве исключенных кандидатов при поиске ближайших
            isolators = isolatorsVisited.stream().map(String::valueOf).collect(Collectors.joining(","));

            //Обходим все места в списке посещенных, извлекаем координаты из каждого из них, используем их в качестве параметров для вызова
            //функции поиска ближайших мест к этим местам
            for(StayPointDTO stayPoint : stayPoints){
                //Отправляем запрос на поиск ближайших мест к данному посещенному
                Page<Place> currentStayPointPlaces = placeRepository.findNearestByVisited(
                        stayPoint.getLatitude(),
                        stayPoint.getLongitude(),
                        1.0,
                        5.0,
                        2,
                        isolators,
                        pageRequest
                );

                //Обновляем число полученных страниц для финального списка
                totalPages += currentStayPointPlaces.getTotalPages();

                //Конвертируем страницу в список
                List<PlaceBriefDTO> currentPlaceRecommendations = currentStayPointPlaces.toList()
                        .stream()
                        .map(PlaceBriefDTO::getFromEntity).toList();

                //Добавляем полученный список к результирующему
                recommendations.addAll(currentPlaceRecommendations);

                //Обновляем список изоляторов, чтобы исключить их при поиске в последующем
                for(PlaceBriefDTO recPlace : currentPlaceRecommendations){
                    isolatorsVisited.add(recPlace.getId());
                }
                //Оновляем строку-параметров с дополнительными
                isolators = isolatorsVisited.stream().map(String::valueOf).collect(Collectors.joining(","));
            }
        }

        return new PagedListDTO<PlaceBriefDTO>()
                .setList(recommendations)
                .setPageNum(page)
                .setTotalPages(totalPages);
    }

    @Transactional
    public PagedListDTO<PlaceBriefDTO> getContentBasedRecommendations(
            int page,
            int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);

        //Берем список категорий мест в базе данных
        List<Category> categoryList = categoryRepository.findAll();

        //Строим вектор пользователя
        List<UserCoefficient> userCoefficients = userCoeffRepository.getAllByUserId(userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getId());
        Map<Category, Double> userVector = UserVectorBuilder.getUserVector(userCoefficients, categoryList);

        //Берем список мест, не посещенных данным пользователем
        Page<Place> notVisitedPlacesPage = placeRepository.findNotVisitedByUser(userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getId(), pageRequest);

        List<PlaceBriefDTO> recommendations = CBFRecommender.getRecommendations(categoryList, userVector, notVisitedPlacesPage);

        return new PagedListDTO<PlaceBriefDTO>()
                .setList(recommendations)
                .setPageNum(page)
                .setTotalPages(notVisitedPlacesPage.getTotalPages());
    }

    @Transactional
    public PagedListDTO<PlaceBriefDTO> getCollaborativeRecommendationsByScores(
            int page,
            int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        User user = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow();

        //Берем список всех мест, которые пользователь не посетил
        Page<Place> notVisitedPlacesPage = placeRepository.findNotVisitedByUser(user.getId(), pageRequest);
        List<Place> placeList = notVisitedPlacesPage.stream().toList();

        //Входные данные: пользователи и набор их оценок
        Map<User, HashMap<Place, Double>> data = new HashMap<>();

        //Берем из БД все оценки, которые можно найти
        List<Score> scoreList = scoreRepository.findAll();

        //Заполняем входные данные для рекомендательной системы
        for(Score score : scoreList){
            if(data.containsKey(score.getUser())){
                if(!data.get(score.getUser()).containsKey(score.getPlace())){
                    data.get(score.getUser()).put(score.getPlace(), (double)score.getScore());
                }
            }else {
                HashMap<Place, Double>  userRating = new HashMap<>();
                userRating.put(score.getPlace(), (double)score.getScore());
                data.put(score.getUser(), userRating);
            }
        }

        //Запуск алгоритма составления предсказаний
        Map<User, HashMap<Place, Double>> projectedData  = SlopeOne.slopeOne(data, placeList);

        //Предсказания конкретного пользователя
        List<PlaceBriefDTO> recommendations = new ArrayList<>((projectedData.get(user)).keySet().stream().map(PlaceBriefDTO::getFromEntity).toList());


        return new PagedListDTO<PlaceBriefDTO>()
                .setList(recommendations)
                .setPageNum(page)
                .setTotalPages(notVisitedPlacesPage.getTotalPages());
    }

    @Transactional
    public PagedListDTO<PlaceBriefDTO> getCollaborativeRecommendationsByFavourites(
            int page,
            int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        User user = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow();

        Page<Place> notVisitedPlacesPage = placeRepository.findNotVisitedByUser(user.getId(), pageRequest);
        List<Place> placeList = notVisitedPlacesPage.stream().toList();

        //Входные данные: пользователи и набор их оценок
        Map<User, HashMap<Place, Double>> data = new HashMap<>();


        //Берем список избранных мест
//        List<Score> favouritesPlaces = new ArrayList<>();
//        List<User> systemUsers = userRepository.findAll();

//        for(User currUser : systemUsers){
//            List<Place> currUserFavourPlaces = currUser.getFavouritePlaces();
//            for(Place userFavour : currUserFavourPlaces){
//                favouritesPlaces.add(new Score().setPlace(userFavour).setUser(currUser).setScore(5));
//            }
//        }

        //Берем из БД все оценки, которые можно найти
        List<Score> scoreList = scoreRepository.findAll();
        List<Place> currUserFavourPlaces = user.getFavouritePlaces();

        //Дополнительно добавляем оценки пользователя равные 5, так как места избранные
        for(Place favourPlace : currUserFavourPlaces){
            scoreList.add(new Score().setPlace(favourPlace).setUser(user).setScore(5));
        }

        //Заполняем входные данные для рекомендательной системы
        for(Score score : scoreList){
            if(data.containsKey(score.getUser())){
                if(!data.get(score.getUser()).containsKey(score.getPlace())){
                    data.get(score.getUser()).put(score.getPlace(), (double)score.getScore());
                }
            }else {
                HashMap<Place, Double>  userRating = new HashMap<>();
                userRating.put(score.getPlace(), (double)score.getScore());
                data.put(score.getUser(), userRating);
            }
        }

        //Запуск алгоритма составления предсказаний
        Map<User, HashMap<Place, Double>> projectedData  = SlopeOne.slopeOne(data, placeList);

        //Предсказания конкретного пользователя
        List<PlaceBriefDTO> recommendations = new ArrayList<>((projectedData.get(user)).keySet().stream().map(PlaceBriefDTO::getFromEntity).toList());


        return new PagedListDTO<PlaceBriefDTO>()
                .setList(recommendations)
                .setPageNum(page)
                .setTotalPages(notVisitedPlacesPage.getTotalPages());
    }

    @Transactional
    public PagedListDTO<PlaceBriefDTO> getPlaces(
            String categoryIds,
            int page,
            int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Place> places = placeRepository.getPlaces(
                categoryIds,
                pageRequest
        );
        List<PlaceBriefDTO> placeBriefDTOS = places.toList()
                .stream()
                .map(PlaceBriefDTO::getFromEntity)
                .collect(toList());
        return new PagedListDTO<PlaceBriefDTO>()
                .setList(placeBriefDTOS)
                .setPageNum(page)
                .setTotalPages(places.getTotalPages());
    }

    @Transactional
    public void addPlaceCategory(Long placeId, Integer categoryId){
        if(categoryId == null
                || placeId == null
                || !categoryRepository.existsById(categoryId)
                || !placeRepository.existsById(placeId)){
            return;
        }

        Category category = new Category();
        category.setId(categoryId);

        placeRepository.findById(placeId).ifPresent(currPlace -> currPlace.getCategories().add(category));

    }
}
