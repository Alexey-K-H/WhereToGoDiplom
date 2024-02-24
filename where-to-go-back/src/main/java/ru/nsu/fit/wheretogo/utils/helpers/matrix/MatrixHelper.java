package ru.nsu.fit.wheretogo.utils.helpers.matrix;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.matrix.DurationMatrix;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MatrixHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatrixHelper.class);

    public static String printDurationsMatrix() {
        var result = new StringBuilder();

        for (var raw : DurationMatrix.getMatrix()) {
            result.append("[");
            for (var element : raw) {
                result.append(element).append(" ");
            }
            result.deleteCharAt(result.lastIndexOf(" "));
            result.append("]").append("\n");
        }

        return result.toString();
    }

    public static List<MatrixPlaceData> getFirstNNearestPlacesToPoint(List<String> durationsRaw, int count, List<Long> usedIndexes) {
        var matrixSortRaw = new ArrayList<MatrixPlaceData>();

        for (int i = 0; i < durationsRaw.size(); i++) {
            var doubleValue = Double.parseDouble(durationsRaw.get(i));
            matrixSortRaw.add(MatrixPlaceData
                    .builder()
                    .index(i)
                    .duration((int) doubleValue)
                    .build());
        }

        matrixSortRaw = new ArrayList<>(matrixSortRaw
                .stream()
                .filter(matrixPlaceData ->
                        !usedIndexes.contains(matrixPlaceData.getIndex())
                                && matrixPlaceData.getDuration() != 0)
                .toList());

        matrixSortRaw.sort(Comparator.comparingInt(MatrixPlaceData::getDuration));

        return matrixSortRaw
                .stream()
                .limit(count + 1L).toList();
    }
}
