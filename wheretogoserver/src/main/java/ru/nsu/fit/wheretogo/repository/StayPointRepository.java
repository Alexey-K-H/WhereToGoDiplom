package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.wheretogo.entity.StayPoint;

import java.time.Instant;

@Repository
public interface StayPointRepository extends JpaRepository<StayPoint, Integer> {
    //Получить список стори-поинтов конкретного пользователя
    //Нужно для рекомендательной системы
    public Page<StayPoint> getStoryPointByUserId(Long userId, Pageable pageable);
    public void deleteByUploadedAt(Instant instant);
}
