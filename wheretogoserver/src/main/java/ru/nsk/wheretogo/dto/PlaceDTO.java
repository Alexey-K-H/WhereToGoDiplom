package ru.nsk.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsk.wheretogo.entity.Category;
import ru.nsk.wheretogo.entity.Place;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor

public class PlaceDTO {
    private Integer id;
    @NotNull(message = "Name of place cann't be null")
    private String name;
    private String description;
    private Double latt;
    private Double longg;
    private String thumbnail_link;
    private Instant upload_at;
    private List<CategoryDTO> categories;

    public static PlaceDTO getFromEntity(Place place_entity) {
        if (place_entity==null) {
            return null;
        }
        List<CategoryDTO> categories = new ArrayList<>();
        if (place_entity.getCategories()!=null) {
            for (Category category : place_entity.getCategories()) {
                categories.add(CategoryDTO.getFromEntity(category));
            }
        }
        return new PlaceDTO()
                .setId(place_entity.getId())
                .setName(place_entity.getName())
                .setLatt(place_entity.getLatt())
                .setLongg(place_entity.getLongg())
                .setDescription(place_entity.getDescription())
                .setThumbnail_link(place_entity.getThumbnail())
                .setUpload_at(place_entity.getUpload_at())
                .setCategories(categories);
    }
}
