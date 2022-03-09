package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.Score;
import ru.nsu.fit.wheretogo.entity.User;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    public Page<Score> findByAuthor(User author, Pageable pageable);
    public Page<Score> findByPlace(Place place, Pageable pageable);
}
