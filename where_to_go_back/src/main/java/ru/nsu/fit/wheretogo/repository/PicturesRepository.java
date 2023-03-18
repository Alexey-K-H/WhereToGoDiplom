package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.wheretogo.entity.PlacePicture;

public interface PicturesRepository extends JpaRepository<PlacePicture, Long> {
}
