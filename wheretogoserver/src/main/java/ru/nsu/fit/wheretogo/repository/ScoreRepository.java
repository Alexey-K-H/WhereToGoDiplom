package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.score.ScoreId;

public interface ScoreRepository extends JpaRepository<Score, ScoreId> {
    //Получить все оценки конкретного пользователя
    public Page<Score> findByUserId(Long author, Pageable pageable);

    public boolean existsByUserId(Long userId);

    //Получить все оценки конкретного места
    public Page<Score> findByPlaceId(Long place, Pageable pageable);

    public boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    //Получить оценку пользователя конкретного места
//    @Query("select s from Score s where s.author = :user_id and s.place = :place_id")
//    public Score findUserPlaceScore(@Param("user_id") Long userId, @Param("place_id") Long placeId);

    public Score findByUserIdAndPlaceId(Long userId, Long placeId);
}
