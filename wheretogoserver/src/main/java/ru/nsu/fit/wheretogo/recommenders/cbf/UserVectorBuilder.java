package ru.nsu.fit.wheretogo.recommenders.cbf;

import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.User;
import ru.nsu.fit.wheretogo.entity.score.Score;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserVectorBuilder {
    public UserVectorBuilder(){}

    public static Map<Category, Double> getUserVector(User user, List<Category> categories){
        //Берем оценки пользователя
        List<Score> userScores = user.getUserScores();
        //Формируем из них вектор средних оценок пользователя по категориям
        return getAvgScoresByCategory(userScores, categories);
    }

    private static Map<Category, Double> getAvgScoresByCategory(List<Score> userScores, List<Category> categories){
        //Инициализируем вектор средних оценок
        Map<Category, Double> avgScores = new HashMap<>();
        for(Category category : categories){
            avgScores.put(category, 0.0);
        }

        double scoreValue;
        //Число оценок пользователя по конкрентой категории
        Map<Category, Integer> countersByCategory = new HashMap<>();
        for(Category category : categories){
            countersByCategory.put(category, 0);
        }

        for(Score currScore : userScores){
            //Берем значение оценки
            scoreValue = currScore.getScore();
            //Берем место, к которому относится данная оценка
            Place place = currScore.getPlace();
            //Берем категории этого места
            List<Category> currPlaceCategories = place.getCategories();
            //Добавляем оценку к суммируемой велчине для каждой из ктаегорий
            for(Category category : currPlaceCategories){
                avgScores.replace(category, avgScores.get(category) + scoreValue);
                countersByCategory.replace(category, countersByCategory.get(category) + 1);
            }
        }

        //Усредняем значения, используя число оценок пользователя по категориям
        for(Category category : categories){
            if(avgScores.get(category) != 0.0){
                avgScores.replace(category, avgScores.get(category)/countersByCategory.get(category));
            }
        }

        return avgScores;
    }
}
