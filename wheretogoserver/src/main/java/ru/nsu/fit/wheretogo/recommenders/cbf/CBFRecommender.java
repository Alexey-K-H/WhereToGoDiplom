package ru.nsu.fit.wheretogo.recommenders.cbf;

import org.springframework.data.domain.Page;
import ru.nsu.fit.wheretogo.dto.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.Place;

import java.util.*;
import java.util.stream.Collectors;

public class CBFRecommender {
    private static final int TOP_RECOMMENDATIONS = 5;

    public CBFRecommender(){}

    public static double getPlaceCoefficient(
            Map<Category, Double> userVector,
            Map<Category, Double> placeVector,
            List<Category> categoryList){
        double coefficient = 0.0;

        for(Category category : categoryList){
            coefficient += (userVector.get(category) * placeVector.get(category));
        }

        return coefficient/(categoryList.size() * 5);
    }

    public static List<PlaceBriefDTO> getRecommendations(
            List<Category> categoryList,
            Map<Category, Double> userVector,
            Page<Place> notVisitedPlacesPage)
    {
        //Места с их коэффициентами значимости для рекмоендаций
        Map<PlaceBriefDTO, Double> placeCoefficients = new HashMap<>();
        for(Place notVisitedPlace : notVisitedPlacesPage){
            //Для каждого из них строим itemVector
            Map<Category, Double> currItemVector = ItemVectorBuilder.getItemVector(notVisitedPlace, categoryList);
            //Вычисляем для места коэффициент значимости и заносим в карту коэффициентов
            System.out.println("Add coefficient:" + CBFRecommender.getPlaceCoefficient(userVector, currItemVector, categoryList) + ", PLACE:" + notVisitedPlace.getName());
            placeCoefficients.put(
                    PlaceBriefDTO.getFromEntity(notVisitedPlace),
                    CBFRecommender.getPlaceCoefficient(userVector, currItemVector, categoryList)
            );
        }

        Map<PlaceBriefDTO, Double> sortedCoefficients = placeCoefficients.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                ));

        Iterator<Map.Entry<PlaceBriefDTO, Double>> i = sortedCoefficients.entrySet().iterator();
        int topCount = TOP_RECOMMENDATIONS;

        List<PlaceBriefDTO> topPlaces = new ArrayList<>();
        //Формируем список из первого топа рекомендаций
        while (i.hasNext() && topCount > 0){
            Map.Entry<PlaceBriefDTO, Double> entry = i.next();
            topPlaces.add(entry.getKey());
            topCount--;
        }
        return topPlaces;
    }
}
