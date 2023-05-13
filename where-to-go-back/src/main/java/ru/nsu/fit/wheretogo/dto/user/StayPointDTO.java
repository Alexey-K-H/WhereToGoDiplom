package ru.nsu.fit.wheretogo.dto.user;

import lombok.Getter;
import ru.nsu.fit.wheretogo.dto.BaseDTO;
import ru.nsu.fit.wheretogo.entity.place.StayPoint;

@Getter
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
