package ru.nsu.fit.wheretogo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.entity.Category;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO extends BaseDTO {

    private Integer id;

    @NotNull(message = "Enter category name")
    @Size(min = 1, max = 40, message = "Invalid category name length (can be up to 40 characters)")
    private String name;

    public static CategoryDTO getFromEntity(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO()
                .setId(category.getId())
                .setName(category.getName());
    }
}
