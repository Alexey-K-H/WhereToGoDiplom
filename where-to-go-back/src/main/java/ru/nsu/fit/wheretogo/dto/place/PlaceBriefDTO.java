package ru.nsu.fit.wheretogo.dto.place;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.wheretogo.entity.place.Coordinates;
import ru.nsu.fit.wheretogo.entity.place.Place;

import java.util.List;

@Getter
@Setter
public class PlaceBriefDTO extends PlaceDTO {

    private String name;
    private Coordinates coordinates;
    private String thumbnailLink;
    private List<CategoryDTO> categories;
    private Integer duration;

    public static PlaceBriefDTO getFromEntity(Place entity) {
        if (entity == null) {
            return null;
        }

        List<CategoryDTO> categories = extractCategories(entity);

        var dto = new PlaceBriefDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCoordinates(entity.getCoordinates());
        dto.setThumbnailLink(entity.getThumbnail());
        dto.setCategories(categories);
        dto.setDuration(entity.getDuration());

        return dto;
    }
}
