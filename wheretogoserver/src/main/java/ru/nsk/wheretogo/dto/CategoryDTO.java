package ru.nsk.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsk.wheretogo.entity.Category;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor

public class CategoryDTO extends BaseDTO{
    private Integer id;
    @NotNull(message = "category must have name")
    private String name;

    public static CategoryDTO getFromEntity(Category category) {
        if (category==null)
        {
            return null;
        }
        return new CategoryDTO()
                .setId(category.getId())
                .setName(category.getName());
    }

}
