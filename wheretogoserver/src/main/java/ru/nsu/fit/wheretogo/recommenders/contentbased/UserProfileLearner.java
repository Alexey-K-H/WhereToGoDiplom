package ru.nsu.fit.wheretogo.recommenders.contentbased;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.dto.ScoreDTO;
import ru.nsu.fit.wheretogo.service.ScoreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserProfileLearner {

    @Autowired
    private ScoreService scoreService;

    //TODO:описать метод, который построит профиль(вектор) предпочтений пользователя на основе его оценок
//    public Map<String, Double> makeUserProfile(Long userId, Map<Integer, Map<String, Double>> itemVectors){
//        Map<String, Double> profile = new HashMap<>();
//
//        List<ScoreDTO> placeScoreDtoList = scoreService.getScoreDtoListByUserId(userId);
//
//        for(ScoreDTO placeScore: placeScoreDtoList){
//
//        }
//    }
}
