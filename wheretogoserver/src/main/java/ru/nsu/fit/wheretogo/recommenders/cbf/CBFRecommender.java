package ru.nsu.fit.wheretogo.recommenders.cbf;

import org.springframework.data.domain.Page;
import ru.nsu.fit.wheretogo.dto.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.Place;

import java.util.*;

public class CBFRecommender {
    private static final int TOP_RECOMMENDATIONS = 10;

    public CBFRecommender(){}

    public static double getPlaceCoefficient(
            Map<Category, Double> userVector,
            Map<Category, Double> placeVector,
            List<Category> categoryList){
        double coefficient = 0.0;

        for(Category category : categoryList){
            coefficient += (userVector.get(category) * placeVector.get(category));
        }

        return coefficient/categoryList.size();
    }

    public static List<PlaceBriefDTO> getRecommendations(
            List<Category> categoryList,
            Map<Category, Double> userVector,
            Page<Place> notVisitedPlacesPage)
    {
        //Места с их коэффициентами значимости для рекмоендаций
        Map<Double, PlaceBriefDTO> placeCoefficients = new TreeMap<>(Collections.reverseOrder());

        for(Place notVisitedPlace : notVisitedPlacesPage){
            //Для каждого из них строим itemVector
            Map<Category, Double> currItemVector = ItemVectorBuilder.getItemVector(notVisitedPlace, categoryList);
            //Вычисляем для места коэффициент значимости и заносим в карту коэффициентов
            System.out.println("Add Coeff:" + CBFRecommender.getPlaceCoefficient(userVector, currItemVector, categoryList) + ", PLACE:" + notVisitedPlace.getName());
            placeCoefficients.put(
                    CBFRecommender.getPlaceCoefficient(userVector, currItemVector, categoryList),
                    PlaceBriefDTO.getFromEntity(notVisitedPlace)
            );
        }

        Iterator<Map.Entry<Double, PlaceBriefDTO>> i = placeCoefficients.entrySet().iterator();
        int topCount = TOP_RECOMMENDATIONS;

        List<PlaceBriefDTO> topPlaces = new ArrayList<>();
        //Формируем список из первого топа рекомендаций
        while (i.hasNext() && topCount > 0){
            Map.Entry<Double, PlaceBriefDTO> entry = (Map.Entry<Double, PlaceBriefDTO>)i.next();
            topPlaces.add(entry.getValue());
            topCount--;
        }
        return topPlaces;
    }
}
