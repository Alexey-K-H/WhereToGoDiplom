package ru.nsu.fit.wheretogo.dto.place;

import lombok.Getter;
import ru.nsu.fit.wheretogo.entity.place.Coordinates;
import ru.nsu.fit.wheretogo.entity.place.Place;

import java.util.List;

@Getter
public class PlaceBriefDTO extends PlaceDTO {

    private String name;
    private Coordinates coordinates;
    private String thumbnailLink;
    private List<CategoryDTO> categories;

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

        return dto;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}
