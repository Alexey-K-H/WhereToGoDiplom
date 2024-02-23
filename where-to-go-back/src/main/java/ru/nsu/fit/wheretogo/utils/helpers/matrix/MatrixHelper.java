package ru.nsu.fit.wheretogo.utils.helpers.matrix;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.matrix.DurationMatrix;

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

    public static int findMinDurationIndexInRaw(List<String> durationsRaw) {
        var index = 0;
        var minValue = 0.0;

        for (int i = 0; i < durationsRaw.size(); i++) {
            var doubleValue = Double.parseDouble(durationsRaw.get(i));

            if ((doubleValue != 0.0 && minValue == 0.0) || (doubleValue < minValue)) {
                minValue = doubleValue;
                index = i;
            }
        }

        LOGGER.debug("Минимальная временная затрата:{}", minValue);
        LOGGER.debug("Индекс ближайшего места:{}", index);

        return index;
    }
}
