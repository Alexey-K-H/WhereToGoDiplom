package ru.nsu.fit.wheretogo.dto.place;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.wheretogo.dto.BaseDTO;
import ru.nsu.fit.wheretogo.entity.place.Category;

@Getter
@Setter
public class CategoryDTO extends BaseDTO {

    @NotNull(message = "Enter category name")
    @Size(min = 1, max = 40, message = "Invalid category name length (can be up to 40 characters)")
    private String name;

    public static CategoryDTO getFromEntity(Category category) {
        if (category == null) {
            return null;
        }

        var dto = new CategoryDTO();
        dto.id = category.getId();
        dto.name = category.getName();

        return dto;
    }

}
