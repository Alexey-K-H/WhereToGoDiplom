package ru.nsu.fit.wheretogo.dto.user;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.wheretogo.dto.BaseDTO;
import ru.nsu.fit.wheretogo.entity.place.StayPoint;

@Getter
@Setter
public class StayPointDTO extends BaseDTO {

    private double latitude;
    private double longitude;
    private Long userId;

    public static StayPointDTO getFromEntity(StayPoint stayPoint) {
        if (stayPoint == null) {
            return null;
        }

        var dto = new StayPointDTO();
        dto.setId(stayPoint.getId());
        dto.setLatitude(stayPoint.getLatitude());
        dto.setLongitude(stayPoint.getLongitude());
        dto.setUserId(stayPoint.getUser().getId());

        return dto;
    }
}
