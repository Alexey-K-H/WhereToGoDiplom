package ru.nsu.fit.wheretogo.entity.place;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.dto.user.StayPointDTO;
import ru.nsu.fit.wheretogo.entity.user.User;

import java.time.Instant;

@Entity
@Table(name = "T_STAY_POINT")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class StayPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "LATITUDE", nullable = false)
    private double latitude;

    @Column(name = "LONGITUDE", nullable = false)
    private double longitude;

    @Column(name = "UPLOADED_AT", nullable = false)
    private Instant uploadedAt;

    @PrePersist
    public void setUploadDate() {
        uploadedAt = Instant.now();
    }

    public static StayPoint getFromDTO(StayPointDTO dto) {
        if (dto == null) {
            return null;
        }
        return new StayPoint()
                .setId(dto.getId())
                .setLatitude(dto.getLatitude())
                .setLongitude(dto.getLongitude())
                .setUser(new User().setId(dto.getUserId()));
    }

}
