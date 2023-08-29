package ru.nsu.fit.wheretogo.dto.place;

import lombok.Getter;
import ru.nsu.fit.wheretogo.common.Coordinates;
import ru.nsu.fit.wheretogo.entity.place.Place;

import java.time.Instant;
import java.util.List;

@Getter
public class PlaceDescriptionDTO extends PlaceDTO {

    private String name;
    private String description;
    private Coordinates coordinates;
    private String thumbnail;
    private Instant uploadedAt;
    private List<CategoryDTO> categories;

    public static PlaceDescriptionDTO getFromEntity(Place entity) {
        if (entity == null) {
            return null;
        }

        List<CategoryDTO> categories = extractCategories(entity);

        var dto = new PlaceDescriptionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCoordinates(entity.getCoordinates());
        dto.setThumbnail(entity.getThumbnail());
        dto.setUploadedAt(entity.getUploadedAt());
        dto.setCategories(categories);

        return dto;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}
