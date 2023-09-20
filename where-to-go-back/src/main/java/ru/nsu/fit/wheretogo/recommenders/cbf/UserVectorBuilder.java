package ru.nsu.fit.wheretogo.recommenders.cbf;

import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.user.coefficient.UserCoefficient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UserVectorBuilder {
    private UserVectorBuilder() {
    }

    public static Map<Category, Double> getUserVector(List<UserCoefficient> userCoefficients, List<Category> categories) {
        Map<Category, Double> avgScores = new HashMap<>();
        for (Category category : categories) {
            avgScores.put(category, 0.0);
        }

        for (UserCoefficient userCoefficient : userCoefficients) {
            avgScores.put(userCoefficient.getCategory(), userCoefficient.getCoefficient());
        }

        return avgScores;
    }
}
