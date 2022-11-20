package ru.nsu.fit.wheretogo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.dto.CategoryDTO;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public static Category getFromDTO(CategoryDTO dto ) {
        if (dto == null) {
            return null;
        }
        return new Category()
                .setId(dto.getId())
                .setName(dto.getName());
    }
}
