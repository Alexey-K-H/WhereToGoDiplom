package ru.nsu.fit.wheretogo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.dto.CategoryDTO;
import ru.nsu.fit.wheretogo.dto.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.entity.score.Score;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "place")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude", nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude", nullable = false))
    })
    private Coords coords;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;


    @PrePersist
    public void setUploadDate() {
        uploadedAt = Instant.now();
    }

    @ManyToMany
    @JoinTable(
            name = "place_category",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "place")
    private List<PlacePicture> picturesLinks;

    @OneToMany(mappedBy = "place")
    private List<Score> placeScores;

    public static Place getFromDTO(PlaceDescriptionDTO dto) {
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
                .setCoords(dto.getCoords())
                .setThumbnail(dto.getThumbnail())
                .setCategories(categories);
    }

}
