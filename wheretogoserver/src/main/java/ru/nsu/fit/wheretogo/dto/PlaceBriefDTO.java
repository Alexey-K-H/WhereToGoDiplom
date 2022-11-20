package ru.nsu.fit.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.entity.Place;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PlaceBriefDTO extends BaseDTO {
    private Long id;
    private String name;
    private Coords coords;
    private String thumbnailLink;
    private List<CategoryDTO> categories;

    public static PlaceBriefDTO getFromEntity(Place entity) {
        if (entity == null) {
            return null;
        }
        List<CategoryDTO> categories = new ArrayList<>();
        if (entity.getCategories() != null) {
            for (Category category : entity.getCategories()) {
                categories.add(CategoryDTO.getFromEntity(category));
            }
        }
        return new PlaceBriefDTO()
                .setId(entity.getId())
                .setName(entity.getName())
                .setCoords(entity.getCoords())
                .setThumbnailLink(entity.getThumbnail())
                .setCategories(categories);
    }
}
