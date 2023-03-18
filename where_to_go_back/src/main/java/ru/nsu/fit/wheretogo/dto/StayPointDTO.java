package ru.nsu.fit.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.entity.StayPoint;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StayPointDTO extends BaseDTO {
    private Long id;
    private double latitude;
    private double longitude;
    private Long userId;

    public static StayPointDTO getFromEntity(StayPoint stayPoint) {
        if (stayPoint == null) {
            return null;
        }
        return new StayPointDTO()
                .setId(stayPoint.getId())
                .setLatitude(stayPoint.getLatitude())
                .setLongitude(stayPoint.getLongitude())
                .setUserId(stayPoint.getUser().getId());
    }
}
