package ru.nsu.fit.wheretogo.dto.place;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.wheretogo.entity.place.Coordinates;
import ru.nsu.fit.wheretogo.entity.place.Place;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class PlaceDescriptionDTO extends PlaceDTO {

    private String name;
    private String description;
    private Coordinates coordinates;
    private String thumbnail;
    private Instant uploadedAt;
    private List<CategoryDTO> categories;
    private Integer duration;

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
        dto.setDuration(entity.getDuration());

        return dto;
    }
}
