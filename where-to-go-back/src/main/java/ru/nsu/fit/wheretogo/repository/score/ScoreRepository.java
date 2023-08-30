package ru.nsu.fit.wheretogo.repository.score;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.score.ScoreId;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, ScoreId> {

    List<Score> findByUserId(Long author);

    boolean existsByUserId(Long userId);

    List<Score> findByPlaceId(Long place);

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    Score findByUserIdAndPlaceId(Long userId, Long placeId);
}
