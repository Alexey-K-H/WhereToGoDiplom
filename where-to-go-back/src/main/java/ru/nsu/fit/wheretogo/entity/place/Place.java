package ru.nsu.fit.wheretogo.entity.place;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.nsu.fit.wheretogo.dto.place.CategoryDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.entity.score.Score;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_PLACE")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;

    @Embedded
    @AttributeOverride(name = "latitude", column = @Column(name = "LATITUDE", nullable = false))
    @AttributeOverride(name = "longitude", column = @Column(name = "LONGITUDE", nullable = false))
    private Coordinates coordinates;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Column(name = "UPLOADED_AT", nullable = false)
    private Instant uploadedAt;

    @Column(name = "DURATION", nullable = false)
    private Integer duration;


    @PrePersist
    public void setUploadDate() {
        uploadedAt = Instant.now();
    }

    @ManyToMany
    @JoinTable(
            name = "T_PLACE_CATEGORY",
            joinColumns = @JoinColumn(name = "PLACE_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "place", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Score> placeScores;

    public static Place getFromFullDTO(PlaceDescriptionDTO dto) {
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
                .setCoordinates(dto.getCoordinates())
                .setThumbnail(dto.getThumbnail())
                .setCategories(categories);
    }

    public static Place getFromBriefDTO(PlaceBriefDTO dto) {
        List<Category> categories = new ArrayList<>();
        if (dto.getCategories() != null) {
            for (CategoryDTO category : dto.getCategories()) {
                categories.add(Category.getFromDTO(category));
            }
        }
        return new Place()
                .setId(dto.getId())
                .setName(dto.getName())
                .setCoordinates(dto.getCoordinates())
                .setThumbnail(dto.getThumbnailLink())
                .setCategories(categories);
    }

}
