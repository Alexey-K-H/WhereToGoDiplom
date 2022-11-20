package ru.nsu.fit.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.PlacePicture;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDescriptionDTO {
    private Long id;
    @NotNull(message = "Enter place name")
    private String name;
    private String description;
    private Coords coords;
    private String thumbnail;
    private Instant uploadedAt;
    private List<CategoryDTO> categories;
    private List<String> picturesLinks;

    public static PlaceDescriptionDTO getFromEntity(Place entity) {
        if (entity == null) {
            return null;
        }
        List<CategoryDTO> categories = new ArrayList<>();
        if (entity.getCategories() != null) {
            for (Category category : entity.getCategories()) {
                categories.add(CategoryDTO.getFromEntity(category));
            }
        }
        return new PlaceDescriptionDTO()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setCoords(entity.getCoords())
                .setThumbnail(entity.getThumbnail())
                .setUploadedAt(entity.getUploadedAt())
                .setCategories(categories)
                .setPicturesLinks(entity.getPicturesLinks().stream().map((PlacePicture::getUrl)).toList());
        //        //создаем список картинок, который может изменяться
    }
}
