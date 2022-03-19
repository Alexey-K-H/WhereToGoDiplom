package ru.nsu.fit.wheretogo.recommenders.contentbased;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.dto.PlaceBriefDTO;

import java.util.List;

@Component
public class ContentBasedFilter {

    @Autowired
    PlaceContentAnalyzer placeContentAnalyzer;

    @Autowired
    UserProfileLearner userProfileLearner;

    @Autowired
    PlaceScorer placeScorer;

    //Первые топ мест в списке рекомендованных, которые в конечном итоге будут отправлены пользователю
    static final int TOPN = 10;

    public List<PlaceBriefDTO> recommend(Long userId){
        List<PlaceBriefDTO> placeBriefDTOList = null;

        return placeBriefDTOList;
    }
}
