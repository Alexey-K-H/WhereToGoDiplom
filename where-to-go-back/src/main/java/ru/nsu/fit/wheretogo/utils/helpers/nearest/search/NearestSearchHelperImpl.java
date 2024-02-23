package ru.nsu.fit.wheretogo.utils.helpers.nearest.search;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.model.ors.direction.LatLong;
import ru.nsu.fit.wheretogo.repository.place.PlaceRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NearestSearchHelperImpl implements NearestSearchHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(NearestSearchHelperImpl.class);

    private final PlaceRepository placeRepository;

    @Override
    public List<LatLong> findNearestPlaces2Point(LatLong point, Integer limit) {

        try {
            var nearestPlaces = placeRepository.findNearestPlaces(
                    Double.valueOf(point.getLatitude()),
                    Double.valueOf(point.getLongitude()),
                    1.0,
                    20.0,
                    limit
            );

            if (nearestPlaces.isEmpty()) {
                return Collections.emptyList();
            }

            var result = new ArrayList<LatLong>();

            for (var place : nearestPlaces) {
                result.add(LatLong
                        .builder()
                        .latitude(place.getCoordinates().getLatitude().toString())
                        .longitude(place.getCoordinates().getLongitude().toString())
                        .build());
            }

            return result;

        } catch (NumberFormatException | NullPointerException e) {
            LOGGER.debug("Не удалось получить координаты из запроса:{}", point);
            return Collections.emptyList();
        }
    }
}
