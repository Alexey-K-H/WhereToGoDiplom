package ru.nsu.fit.wheretogo.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.wheretogo.entity.place.StayPoint;

import java.time.Instant;

@Repository
public interface StayPointRepository extends JpaRepository<StayPoint, Integer> {

    void deleteByUploadedAt(Instant instant);

    int countByUserId(Long userId);

    @Modifying
    @Query("update StayPoint s set s.uploadedAt = :_new_time")
    void setNewPointTimeStamp(
            @Param("_new_time") Instant instant
    );

    boolean existsByUserId(Long userId);
}
