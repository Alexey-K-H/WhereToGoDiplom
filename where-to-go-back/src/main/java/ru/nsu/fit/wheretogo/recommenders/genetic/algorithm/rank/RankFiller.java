package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.rank;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.entity.user.coefficient.UserCoefficient;
import ru.nsu.fit.wheretogo.recommenders.cbf.ItemVectorBuilder;
import ru.nsu.fit.wheretogo.recommenders.cbf.UserVectorBuilder;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.Individual;
import ru.nsu.fit.wheretogo.repository.place.CategoryRepository;
import ru.nsu.fit.wheretogo.repository.user.UserRepository;
import ru.nsu.fit.wheretogo.repository.user.coefficient.UserCoefficientRepository;
import ru.nsu.fit.wheretogo.utils.helpers.SecurityContextHelper;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RankFiller {
    private static final Logger LOGGER = LoggerFactory.getLogger(RankFiller.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserCoefficientRepository userCoefficientRepository;

    public void updateRoutesAttractionCoefficients(List<Individual> population) {
        List<Category> categoryList = categoryRepository.findAll();

        List<UserCoefficient> userCoefficients = userCoefficientRepository
                .getAllByUserId(userRepository.findByEmail(SecurityContextHelper.email())
                        .orElseThrow().getId());

        Map<Category, Double> userVector = UserVectorBuilder.getUserVector(userCoefficients, categoryList);
        LOGGER.debug("Вектор предпочтений пользователя:{}", userVector);

        for (var individual : population) {
            var routeCoefficient = 0.0;

            for (var place : individual.getRoutePlaces()) {
                var placeEntity = Place.getFromBriefDTO(place.getPlaceDescription());
                Map<Category, Double> currItemVector = ItemVectorBuilder.getItemVector(placeEntity, categoryList);

                var coefficient = getPlaceCoefficient(userVector, currItemVector, categoryList);
                routeCoefficient += coefficient;
                place.setPlaceAttractionCoefficient(coefficient);
            }

            individual.setSummaryAttractionCoefficient(routeCoefficient);
        }
    }

    private double getPlaceCoefficient(
            Map<Category, Double> userVector,
            Map<Category, Double> placeVector,
            List<Category> categoryList) {
        var coefficient = 0.0;

        for (Category category : categoryList) {
            coefficient += (userVector.get(category) * placeVector.get(category));
        }

        return coefficient / (categoryList.size() * 5);
    }
}
