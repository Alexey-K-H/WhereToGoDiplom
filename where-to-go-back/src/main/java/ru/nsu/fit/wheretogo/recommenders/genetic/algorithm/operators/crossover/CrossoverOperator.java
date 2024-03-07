package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.crossover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.crossover.tags.TagPair;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.crossover.tags.TagSequence;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.crossover.tags.TimeTag;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CrossoverOperator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrossoverOperator.class);
    private static final Random RANDOM = new Random();

    @Value("${ru.nsu.fit.wheretogo.genetic.crossover.time_delta}")
    private Integer deltaTime;

    public List<Individual> execute(List<Individual> population) {

        LOGGER.debug("Запуск оператора скрещивания.\nПараметры\nМаксимальная разность между разрезами:{}", deltaTime);

        LOGGER.debug("Сортировка популяции по коэффициенту значимости");
        population.sort((o1, o2) -> {
            var diff = o1.getSummaryAttractionCoefficient() - o2.getSummaryAttractionCoefficient();

            if (diff < 0) {
                return 1;
            }

            if (diff > 0) {
                return -1;
            }

            return 0;
        });
        LOGGER.debug("Отсортированная популяция:{}", population);

        var firstParent = population.get(0);
        var secondParent = population.get(1);

        var firstParentTags = buildTagSequence(firstParent);
        LOGGER.debug("Временные метки родителя 1:{}", firstParentTags);
        var secondParentTags = buildTagSequence(secondParent);
        LOGGER.debug("Временные метки родителя 2:{}", secondParentTags);

        var cutTags = getCutIndexes(firstParentTags, secondParentTags);
        if (cutTags == null
                || cutTags.getFirstTag() == 0 || cutTags.getSecondTag() == 0
                || cutTags.getFirstTag() == firstParentTags.getTags().size() - 1
                || cutTags.getSecondTag() == secondParentTags.getTags().size() - 1) {
            LOGGER.debug("Не найдено подходящих тегов, скрещивание пропускается");
            return population;
        } else {
            LOGGER.debug("Найдены точки разреза маршрутов-родителей:\nДля первого:{}\nДля второго:{}\nРазность:{}",
                    cutTags.getFirstTag(), cutTags.getSecondTag(), cutTags.getDiffTags());

            var offspring = getOffspring(firstParent, secondParent, cutTags);

            population.add(offspring);
        }

        return population;
    }

    private TagSequence buildTagSequence(Individual individual) {
        var result = new TagSequence();
        var timeTagValue = 0;
        var index = 0;
        for (var place : individual.getRoutePlaces()) {
            timeTagValue += place.getPlaceDescription().getDuration();

            result.addTag(TimeTag
                    .builder()
                    .value(timeTagValue)
                    .index(index)
                    .build()
            );

            index++;
        }

        return result;
    }

    private TagPair getCutIndexes(TagSequence firstParentTags, TagSequence secondParentTags) {
        var i = 0;
        var j = 0;

        var tagPairs = new ArrayList<TagPair>();
        var merged = new TagSequence();

        while (i < firstParentTags.getTags().size() && j < secondParentTags.getTags().size()) {
            if (Math.abs(firstParentTags.getTags().get(i).getValue() - secondParentTags.getTags().get(j).getValue()) <= deltaTime
                    && firstParentTags.getTags().get(i).getIndex() != 0
                    && firstParentTags.getTags().get(i).getIndex() != firstParentTags.getTags().size() - 1
                    && secondParentTags.getTags().get(j).getIndex() != 0
                    && secondParentTags.getTags().get(j).getIndex() != secondParentTags.getTags().size() - 1) {
                tagPairs.add(TagPair
                        .builder()
                        .firstTag(i)
                        .secondTag(j)
                        .diffTags(Math.abs(firstParentTags.getTags().get(i).getValue() - secondParentTags.getTags().get(j).getValue()))
                        .build()
                );
            }

            if (firstParentTags.getTags().get(i).getValue() <= secondParentTags.getTags().get(j).getValue()) {
                merged.addTag(firstParentTags.getTags().get(i));
                i++;
            } else {
                merged.addTag(secondParentTags.getTags().get(j));
                j++;
            }
        }

        if (tagPairs.isEmpty()) {
            return null;
        }

        if (tagPairs.size() > 1) {
            return tagPairs.get(RANDOM.nextInt(tagPairs.size()));
        } else {
            return tagPairs.get(0);
        }

    }

    private Individual getOffspring(Individual firstParent, Individual secondParent, TagPair cutTags) {

        var firstParentHead = getPartOfParent(firstParent, 0, cutTags.getFirstTag());
        LOGGER.debug("Голова первого родителя:{}", firstParentHead);
        var firstParentTail = getPartOfParent(firstParent, cutTags.getFirstTag() + 1, firstParent.getRoutePlaces().size() - 1);
        LOGGER.debug("Хвост первого родителя:{}", firstParentTail);

        var secondParentHead = getPartOfParent(secondParent, 0, cutTags.getSecondTag());
        LOGGER.debug("Голова второго родителя:{}", secondParentHead);
        var secondParentTail = getPartOfParent(secondParent, cutTags.getSecondTag() + 1, secondParent.getRoutePlaces().size() - 1);
        LOGGER.debug("Хвост второго родителя:{}", secondParentTail);

        var firstOffspring = mergeHeadAndTail(firstParentHead, secondParentTail);
        LOGGER.debug("Первый потомок:{}", firstOffspring);

        var secondOffspring = mergeHeadAndTail(secondParentHead, firstParentTail);
        LOGGER.debug("Второй потомок:{}", secondOffspring);

        var result = selectBestOffspring(firstOffspring, secondOffspring);
        LOGGER.debug("Лучший потомок:{}", result);

        return result;
    }

    private Individual getPartOfParent(Individual parent, int startIndex, int endIndex) {
        var result = new Individual();

        for (int i = startIndex; i < endIndex; i++) {
            result.addPlace(
                    parent.getRoutePlaces().get(i).getPlaceDescription(),
                    parent.getMovementsDurations().get(i),
                    parent.getRoutePlaces().get(i).getPlaceAttractionCoefficient()
            );
        }

        return result;
    }

    private Individual mergeHeadAndTail(Individual head, Individual tail) {
        var result = new Individual();

        var headPlaceIds = new ArrayList<Long>();

        for (int i = 0; i < head.getRoutePlaces().size(); i++) {
            result.addPlace(
                    head.getRoutePlaces().get(i).getPlaceDescription(),
                    head.getMovementsDurations().get(i),
                    head.getRoutePlaces().get(i).getPlaceAttractionCoefficient()
            );

            headPlaceIds.add(head.getRoutePlaces().get(i).getPlaceDescription().getId());
        }

        for (int i = 0; i < tail.getRoutePlaces().size(); i++) {
            if (!headPlaceIds.contains(tail.getRoutePlaces().get(i).getPlaceDescription().getId())) {
                result.addPlace(
                        tail.getRoutePlaces().get(i).getPlaceDescription(),
                        tail.getMovementsDurations().get(i),
                        tail.getRoutePlaces().get(i).getPlaceAttractionCoefficient()
                );
            }
        }

        return result;
    }

    private Individual selectBestOffspring(Individual firstOffspring, Individual secondOffspring) {
        double firstOffspringRank = 0.0;
        double secondOffspringRank = 0.0;

        for (var place : firstOffspring.getRoutePlaces()) {
            firstOffspringRank += place.getPlaceAttractionCoefficient();
        }

        for (var place : secondOffspring.getRoutePlaces()) {
            secondOffspringRank += place.getPlaceAttractionCoefficient();
        }

        LOGGER.debug("Ранг первого потомка:{}", firstOffspringRank);
        LOGGER.debug("Ранг второго потомка:{}", secondOffspringRank);

        return firstOffspringRank > secondOffspringRank ? firstOffspring : secondOffspring;
    }
}
