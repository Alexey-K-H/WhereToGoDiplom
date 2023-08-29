package ru.nsu.fit.wheretogo.repository.score;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.score.ScoreId;

public interface ScoreRepository extends JpaRepository<Score, ScoreId> {

    Page<Score> findByUserId(Long author, Pageable pageable);

    boolean existsByUserId(Long userId);

    Page<Score> findByPlaceId(Long place, Pageable pageable);

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    Score findByUserIdAndPlaceId(Long userId, Long placeId);
}
