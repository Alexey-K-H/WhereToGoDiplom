package ru.nsu.fit.wheretogo.recommenders.cbf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemVectorBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemVectorBuilder.class);

    private ItemVectorBuilder() {
    }

    public static Map<Category, Double> getItemVector(Place place, List<Category> categories) {
        List<Category> placeCategories = place.getCategories();

        var placeCategoryIds = new ArrayList<Long>();
        for (Category category : placeCategories) {
            placeCategoryIds.add(category.getId());
        }

        Map<Category, Double> itemVector = new HashMap<>();

        for (Category category : categories) {
            if (placeCategoryIds.contains(category.getId())) {
                itemVector.put(category, 1.0);
            } else {
                itemVector.put(category, 0.0);
            }
        }

        return itemVector;
    }
}
