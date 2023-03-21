package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.wheretogo.entity.StayPoint;

import java.time.Instant;

@Repository
public interface StayPointRepository extends JpaRepository<StayPoint, Integer> {
    //Получить список стори-поинтов конкретного пользователя
    //Нужно для рекомендательной системы
    public Page<StayPoint> getStoryPointByUserId(Long userId, Pageable pageable);

    public void deleteByUploadedAt(Instant instant);

    public boolean existsByLatitudeAndLongitudeAndUserId(double lat, double lon, Long user_id);

    public int countByUserId(Long userId);

    @Modifying
    @Query("update StayPoint s set s.uploadedAt = :_new_time")
    public void setNewPointTimeStamp(
            @Param("_new_time") Instant instant
    );

    public boolean existsByUserId(Long userId);
}
