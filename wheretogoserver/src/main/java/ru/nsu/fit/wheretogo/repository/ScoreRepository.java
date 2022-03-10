package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.User;
import ru.nsu.fit.wheretogo.entity.score.ScoreId;

public interface ScoreRepository extends JpaRepository<Score, ScoreId> {
    //Получить все оценки конкретного пользователя
    public Page<Score> findByAuthor(Long author, Pageable pageable);

    //Получить все оценки конкретного места
    public Page<Score> findByPlace(Long place, Pageable pageable);

    //Получить оценку пользователя конкретного места
//    @Query("select s from Score s where s.author = :user_id and s.place = :place_id")
//    public Score findUserPlaceScore(@Param("user_id") Long userId, @Param("place_id") Long placeId);

    public Score findByAuthorAndPlace(Long userId, Long placeId);
}
