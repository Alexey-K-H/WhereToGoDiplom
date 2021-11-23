package ru.nsk.wheretogo.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsk.wheretogo.dto.CategoryDTO;
import ru.nsk.wheretogo.dto.PlaceDTO;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
@Entity
@Table(name="places")
public class Place {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name="latt", nullable = false)
    private Double latt;

    @Column(name="longg", nullable = false)
    private Double longg;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "uploaded_at", nullable = false)
    private Instant upload_at;

    @PrePersist
    public void setUploadDate() {
        upload_at = Instant.now();
    }

    @ManyToMany
    @JoinTable(
            name="places_categories",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public static Place getFromDTO(PlaceDTO dto) {
        List<Category> categories = new ArrayList<>();
        if (dto.getCategories() != null) {
            for (CategoryDTO category : dto.getCategories()) {
                categories.add(Category.getFromDTO(category));
            }
        }
        return new Place()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setThumbnail(dto.getThumbnail_link())
                .setCategories(categories);
    }

}
