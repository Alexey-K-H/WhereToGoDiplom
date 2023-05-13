package ru.nsu.fit.wheretogo.recommenders.cbf;

import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemVectorBuilder {
    private ItemVectorBuilder() {
    }

    public static Map<Category, Double> getItemVector(Place place, List<Category> categories) {
        List<Category> placeCategories = place.getCategories();

        Map<Category, Double> itemVector = new HashMap<>();
        for (Category category : categories) {
            itemVector.put(category, 0.0);
        }

        for (Category placeCategory : placeCategories) {
            itemVector.replace(placeCategory, 1.0);
        }

        return itemVector;
    }
}
