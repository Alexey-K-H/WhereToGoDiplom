package ru.nsu.fit.wheretogo.service.place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.repository.place.CategoryRepository;
import ru.nsu.fit.wheretogo.repository.place.PlaceRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

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
        placeRepository.save(Place.getFromDTO(place));
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
    public PagedListDTO<PlaceBriefDTO> getNearestPlacesToPoint(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit,
            int page,
            int size
    ) {
        var pageRequest = PageRequest.of(page, size);
        Page<Place> places = placeRepository.findNearestPlaces(myLat, myLon, startDist, maxDist, limit, pageRequest);
        List<PlaceBriefDTO> nearestPlaceDtos = places.toList()
                .stream()
                .map(PlaceBriefDTO::getFromEntity)
                .toList();

        var listDto = new PagedListDTO<PlaceBriefDTO>();
        listDto.setList(nearestPlaceDtos);
        listDto.setTotalPages(places.getTotalPages());
        listDto.setPageNum(page);

        return listDto;
    }

    @Transactional
    public PagedListDTO<PlaceBriefDTO> getPlaces(
            String categoryIds,
            int page,
            int size
    ) {
        var pageRequest = PageRequest.of(page, size);
        Page<Place> places = placeRepository.getPlaces(
                categoryIds,
                pageRequest
        );

        List<PlaceBriefDTO> placeBriefDTOS = places.toList()
                .stream()
                .map(PlaceBriefDTO::getFromEntity)
                .toList();

        var listDto = new PagedListDTO<PlaceBriefDTO>();
        listDto.setList(placeBriefDTOS);
        listDto.setPageNum(page);
        listDto.setTotalPages(places.getTotalPages());

        return listDto;
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

    private void validatePlace(PlaceDescriptionDTO place) {
        if (placeRepository.existsByNameOrDescriptionOrCoordinates(
                place.getName(),
                place.getDescription(),
                place.getCoordinates())) {
            throw new ValidationException("Place with such name, description or coords is already exist");
        }
    }
}
