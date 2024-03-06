package ru.nsu.fit.wheretogo.service.place;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.repository.place.CategoryRepository;
import ru.nsu.fit.wheretogo.repository.place.PlaceRepository;

import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceServiceImpl.class);

    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;

    public PlaceServiceImpl(
            PlaceRepository placeRepository,
            CategoryRepository categoryRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void addPlace(PlaceDescriptionDTO place) {
        validatePlace(place);
        placeRepository.save(Place.getFromFullDTO(place));
    }

    @Override
    @Transactional
    public void deletePlace(PlaceDescriptionDTO place) {
        placeRepository.deleteById(place.getId());
    }

    @Override
    @Transactional
    public PlaceDescriptionDTO getPlaceById(Long id) {
        if (id == null) {
            return null;
        }
        return PlaceDescriptionDTO.getFromEntity(placeRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public List<PlaceBriefDTO> getNearestPlacesToPoint(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit
    ) {
        List<Place> places = placeRepository.findNearestPlaces(myLat, myLon, startDist, maxDist, limit);
        return places
                .stream()
                .map(PlaceBriefDTO::getFromEntity)
                .toList();
    }

    @Transactional
    public List<PlaceBriefDTO> getPlaces(
            String categoryIds
    ) {
        List<Place> places = placeRepository.getPlaces(
                categoryIds
        );

        return places
                .stream()
                .map(PlaceBriefDTO::getFromEntity)
                .toList();
    }

    @Transactional
    public void addPlaceCategory(Long placeId, Long categoryId) {
        if (categoryId == null
                || placeId == null
                || !categoryRepository.existsById(categoryId)
                || !placeRepository.existsById(placeId)) {
            return;
        }

        var category = new Category();
        category.setId(categoryId);

        placeRepository.findById(placeId).ifPresent(currPlace -> currPlace.getCategories().add(category));
    }

    @Override
    public List<PlaceBriefDTO> getAllPlaces() {
        return placeRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().map(PlaceBriefDTO::getFromEntity).toList();
    }

    private void validatePlace(PlaceDescriptionDTO place) {
        if (placeRepository.existsByNameOrDescriptionOrCoordinates(
                place.getName(),
                place.getDescription(),
                place.getCoordinates())) {
            throw new ValidationException("Место с таким именем/координатами/описанием уже существует");
        }
    }
}
