package ru.nsu.fit.wheretogo.recommenders.cbf;

import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.user_coeff.UserCoefficient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserVectorBuilder {
    public UserVectorBuilder() {
    }

    public static Map<Category, Double> getUserVector(List<UserCoefficient> userCoefficients, List<Category> categories) {
        //Инициализируем вектор средних оценок
        Map<Category, Double> avgScores = new HashMap<>();
        for (Category category : categories) {
            avgScores.put(category, 0.0);
        }

        for (UserCoefficient userCoefficient : userCoefficients) {
            avgScores.put(userCoefficient.getCategory(), userCoefficient.getCoeff());
        }

        return avgScores;
    }
}
