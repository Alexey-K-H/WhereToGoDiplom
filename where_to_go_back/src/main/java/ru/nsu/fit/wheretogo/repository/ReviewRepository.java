package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.Review;
import ru.nsu.fit.wheretogo.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    public Page<Review> findByAuthor(User author, Pageable pageable);

    public Page<Review> findByPlace(Place place, Pageable pageable);
}
