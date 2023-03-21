package ru.nsu.fit.wheretogo.recommenders.cbf;

import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.Place;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemVectorBuilder {
    public ItemVectorBuilder() {
    }

    public static Map<Category, Double> getItemVector(Place place, List<Category> categories) {
        //Берем ктаегории места
        List<Category> placeCategories = place.getCategories();

        //Инициализируем вектор нулями
        Map<Category, Double> itemVector = new HashMap<>();
        for (Category category : categories) {
            itemVector.put(category, 0.0);
        }

        //Те категории, которые имеют отноешние к месту получают значения 1.0
        for (Category placeCategory : placeCategories) {
            itemVector.replace(placeCategory, 1.0);
        }

        //Возвращаем вектор из 0 и 1 для дальнейшего скалярного умножения
        return itemVector;
    }
}
