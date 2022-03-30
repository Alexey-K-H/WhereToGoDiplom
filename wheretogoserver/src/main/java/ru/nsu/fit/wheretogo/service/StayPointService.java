package ru.nsu.fit.wheretogo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.dto.StayPointDTO;
import ru.nsu.fit.wheretogo.entity.StayPoint;
import ru.nsu.fit.wheretogo.repository.StayPointRepository;
import ru.nsu.fit.wheretogo.repository.UserRepository;
import ru.nsu.fit.wheretogo.utils.SecurityContextHelper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class StayPointService {

    @Autowired
    private StayPointRepository stayPointRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addStoryPoint(double lat, double lon){
        Coords coords = new Coords();
        coords.setLatitude(new BigDecimal(lat));
        coords.setLongitude(new BigDecimal(lon));
        StayPoint stayPoint = new StayPoint()
                .setUser(userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow())
                .setCoords(coords);

        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getStayPoints().add(stayPoint)
        );

        stayPointRepository.save(stayPoint);
    }

    @Transactional
    public void deleteOldStoryPoint(){
        //Сперва берем список stay-point-ов пользователя и находим в них самый старый
        List<StayPoint> userStayPoints = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getStayPoints();
        userStayPoints.sort(Comparator.comparing(StayPoint::getUploadedAt));
        Instant oldTimeStamp = userStayPoints.get(0).getUploadedAt();

        //Удаляем stay-point с самой старой датой загрузки
        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getStayPoints().removeIf(storyPoint -> Objects.equals(storyPoint.getUploadedAt(), oldTimeStamp)));

        stayPointRepository.deleteByUploadedAt(oldTimeStamp);
    }

    @Transactional(readOnly = true)
    public List<StayPointDTO> getByUser(){
        return userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getStayPoints().stream().map(
                StayPointDTO::getFromEntity
        ).toList();
    }

}
