package ru.nsu.fit.wheretogo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.*;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.UserCoefficient;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.User;
import ru.nsu.fit.wheretogo.entity.score.ScoreId;
import ru.nsu.fit.wheretogo.repository.PlaceRepository;
import ru.nsu.fit.wheretogo.repository.ScoreRepository;
import ru.nsu.fit.wheretogo.repository.UserCoeffRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserCoeffRepository userCoeffRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Transactional
    public ScoreDTO createScore(Long userId, Long placeId, Integer value) {
        int oldValue = 0;
        if(scoreRepository.existsByUserIdAndPlaceId(userId, placeId)){
            Score currScore = scoreRepository.findByUserIdAndPlaceId(userId, placeId);
            oldValue = currScore.getScore();
        }

        Score score = scoreRepository.save(new Score()
                .setId(new ScoreId().setUser(userId).setPlace(placeId))
                .setUser(new User().setId(userId))
                .setPlace(new Place().setId(placeId))
                .setScore(value));

        //Берем категории места
        List<Category> placeCategories = placeRepository.findById(placeId).orElseThrow().getCategories();

        //Проходим по всем катгориям и смотрим где обновить или создать коэффициенты
        for(Category category : placeCategories){
            if(userCoeffRepository.existsByCategory(category)){
                //Обновляем коэффицент
                if(oldValue != 0){
                    double newValue = Math.abs(oldValue - value);
                    userCoeffRepository.updateCoeffWitOutInc(newValue, category.getId(), userId);
                }else{
                    userCoeffRepository.updateCoeffWithInc(value.doubleValue(), category.getId(), userId);
                }

            }else {
                //Создаем новый коэффициент
                userCoeffRepository.save(new UserCoefficient()
                        .setUser(new User().setId(userId))
                        .setCategory(category)
                        .setPlacesCount(1L)
                        .setCoeff(value.doubleValue()));
            }
        }


        return ScoreDTO.getFromEntity(score);
    }

    @Transactional
    public void deleteScore(ScoreDTO scoreDTO) {
        scoreRepository.delete(Score.getFromDto(scoreDTO));
    }

    @Transactional
    public ScoreDTO getByUserPlace(Long userId, Long placeId){
        if(userId == null || placeId == null){
            return null;
        }
        return ScoreDTO.getFromEntity(scoreRepository.findByUserIdAndPlaceId(userId, placeId));
    }

    @Transactional(readOnly = true)
    public PagedListDTO<ScoreDTO> getByUserId(UserDTO userDto, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Score> scores = scoreRepository.findByUserId(User.getFromDTO(userDto).getId(), pageRequest);
        List<ScoreDTO> userScoresDtos = scores.toList()
                .stream()
                .map(ScoreDTO::getFromEntity)
                .collect(toList());
        return new PagedListDTO<ScoreDTO>()
                .setList(userScoresDtos)
                .setPageNum(page)
                .setTotalPages(scores.getTotalPages());
    }

    @Transactional(readOnly = true)
    public List<ScoreDTO> getScoreDtoListByUserId(Long userId){
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Score> scores = scoreRepository.findByUserId(userId, pageRequest);
        return scores.toList()
                .stream()
                .map(ScoreDTO::getFromEntity)
                .collect(toList());
    }


    @Transactional(readOnly = true)
    public PagedListDTO<ScoreDTO> getByPlace(PlaceDescriptionDTO placeDto, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Score> scores = scoreRepository.findByPlaceId(Place.getFromDTO(placeDto).getId(), pageRequest);
        List<ScoreDTO> userScoresDtos = scores.toList()
                .stream()
                .map(ScoreDTO::getFromEntity)
                .collect(toList());
        return new PagedListDTO<ScoreDTO>()
                .setList(userScoresDtos)
                .setPageNum(page)
                .setTotalPages(scores.getTotalPages());
    }

    @Transactional(readOnly = true)
    public boolean ifUserHasScores(Long userId){
        return scoreRepository.existsByUserId(userId);
    }
}
