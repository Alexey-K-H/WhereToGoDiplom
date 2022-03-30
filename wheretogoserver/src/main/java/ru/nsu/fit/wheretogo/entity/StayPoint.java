package ru.nsu.fit.wheretogo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.dto.StayPointDTO;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "stay_points")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class StayPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude", nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude", nullable = false))
    })
    private Coords coords;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    @PrePersist
    public void setUploadDate() {
        uploadedAt = Instant.now();
    }

    public static StayPoint getFromDTO(StayPointDTO dto){
        if(dto == null){
            return null;
        }
        return new StayPoint()
                .setId(dto.getId())
                .setCoords(dto.getCoords())
                .setUser(new User().setId(dto.getUserId()));
    }

}
