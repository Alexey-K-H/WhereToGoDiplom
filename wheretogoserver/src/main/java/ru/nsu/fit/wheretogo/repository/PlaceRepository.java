package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsByNameOrDescriptionOrCoords(String name, String description, Coords coords);
}
