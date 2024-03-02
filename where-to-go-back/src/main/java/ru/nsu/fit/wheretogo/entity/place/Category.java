package ru.nsu.fit.wheretogo.entity.place;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.dto.place.CategoryDTO;

@Entity
@Table(name = "T_CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CATEGORY_PATH", nullable = false, columnDefinition = "ltree")
    private String categoryPath;


    public Category(String name) {
        this.name = name;
    }

    public static Category getFromDTO(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Category()
                .setId(dto.getId())
                .setName(dto.getName());
    }
}
