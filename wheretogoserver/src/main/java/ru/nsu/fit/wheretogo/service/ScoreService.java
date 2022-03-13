package ru.nsu.fit.wheretogo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.*;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.User;
import ru.nsu.fit.wheretogo.entity.score.ScoreId;
import ru.nsu.fit.wheretogo.repository.ScoreRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Transactional
    public ScoreDTO createScore(Long userId, Long placeId, Integer value) {
        Score score = scoreRepository.save(new Score()
                .setId(new ScoreId().setAuthor(userId).setPlace(placeId))
                .setUser(new User().setId(userId))
                .setPlace(new Place().setId(placeId))
                .setScore(value));
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

    public PagedListDTO<ScoreDTO> getByUser(UserDTO userDto, int page, int size) {
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
}
