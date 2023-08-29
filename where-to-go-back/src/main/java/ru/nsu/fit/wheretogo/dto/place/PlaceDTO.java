package ru.nsu.fit.wheretogo.dto.place;

import ru.nsu.fit.wheretogo.dto.BaseDTO;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовая модель DTO места
 */
public abstract class PlaceDTO extends BaseDTO {

    protected static List<CategoryDTO> extractCategories(Place entity) {
        List<CategoryDTO> categories = new ArrayList<>();
        if (entity.getCategories() != null) {
            for (Category category : entity.getCategories()) {
                categories.add(CategoryDTO.getFromEntity(category));
            }
        }

        return categories;
    }
}
