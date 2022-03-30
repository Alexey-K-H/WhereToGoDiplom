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

        StayPoint stayPoint = new StayPoint()
                .setUser(userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow())
                .setLatitude(lat)
                .setLongitude(lon);

        //Проверяем, есть ли данный stay-point уже в базе данных
        //Берем список всех stay-point-ов пользователя
        List<StayPoint> userStayPoints = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getStayPoints();
        boolean exist = false;
        double eps = 0.001;//нужно для сравнения вещественных чисел

        for(StayPoint userStayPoint : userStayPoints){
            if (Math.abs(userStayPoint.getLatitude() - lat) < eps && Math.abs(userStayPoint.getLongitude() - lon) < eps) {
                exist = true;
                break;
            }
        }

        if(exist){
            //Обнволяем дату загрузки
            //Удаляем старую запись
            userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                    currentUser -> currentUser.getStayPoints().removeIf(
                            currStayPoint -> currStayPoint.getLatitude() == lat && currStayPoint.getLongitude() == lon)
            );

            //Добавляем новую
            userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                    currentUser -> currentUser.getStayPoints().add(stayPoint)
            );

            stayPointRepository.setNewPointTimeStamp(Instant.now());

        }else {
            userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                    currentUser -> currentUser.getStayPoints().add(stayPoint)
            );

            stayPointRepository.save(stayPoint);
        }

        //Проверяем число stay-point-ов в базе данных
        //Если их стало больше 5(тетосвое значение), то удаялем самый старый

        if(stayPointRepository.countByUserId(
                userRepository.findByEmail(SecurityContextHelper.email())
                        .orElseThrow()
                        .getId()) > 5){
            deleteOldStoryPoint();
        }
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
