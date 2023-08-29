package ru.nsu.fit.wheretogo.recommenders.cbf;

import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CBFRecommender {
    private static final int TOP_RECOMMENDATIONS = 5;

    private CBFRecommender() {
    }

    public static double getPlaceCoefficient(
            Map<Category, Double> userVector,
            Map<Category, Double> placeVector,
            List<Category> categoryList) {
        var coefficient = 0.0;

        for (Category category : categoryList) {
            coefficient += (userVector.get(category) * placeVector.get(category));
        }

        return coefficient / (categoryList.size() * 5);
    }

    public static List<PlaceBriefDTO> getRecommendations(
            List<Category> categoryList,
            Map<Category, Double> userVector,
            List<Place> notVisitedPlacesPage) {
        Map<PlaceBriefDTO, Double> placeCoefficients = new HashMap<>();
        for (Place notVisitedPlace : notVisitedPlacesPage) {
            Map<Category, Double> currItemVector = ItemVectorBuilder.getItemVector(notVisitedPlace, categoryList);
//            System.out.println("Add coefficient:" + CBFRecommender.getPlaceCoefficient(userVector, currItemVector, categoryList) + ", PLACE:" + notVisitedPlace.getName());
            placeCoefficients.put(
                    PlaceBriefDTO.getFromEntity(notVisitedPlace),
                    CBFRecommender.getPlaceCoefficient(userVector, currItemVector, categoryList)
            );
        }

        for (Map.Entry<PlaceBriefDTO, Double> entry : placeCoefficients.entrySet()) {
            System.out.println("PLACE:" + entry.getKey().getName() + "/COEFFICIENT:" + entry.getValue());
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

        List<PlaceBriefDTO> topPlaces = new ArrayList<>();
        int topCount = TOP_RECOMMENDATIONS;
        while (i.hasNext() && topCount > 0) {
            Map.Entry<PlaceBriefDTO, Double> entry = i.next();
            topPlaces.add(entry.getKey());
            topCount--;
        }
        return topPlaces;
    }
}
