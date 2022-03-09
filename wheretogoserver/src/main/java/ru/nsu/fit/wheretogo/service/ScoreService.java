package ru.nsu.fit.wheretogo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.*;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.Score;
import ru.nsu.fit.wheretogo.entity.User;
import ru.nsu.fit.wheretogo.repository.ScoreRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Transactional
    public Score addScore(ScoreDTO scoreDTO) {
        return scoreRepository.save(Score.getFromDto(scoreDTO));
    }

    @Transactional
    public void deleteScore(ScoreDTO scoreDTO) {
        scoreRepository.delete(Score.getFromDto(scoreDTO));
    }

    public PagedListDTO<ScoreDTO> getByUser(UserDto userDto, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Score> scores = scoreRepository.findByAuthor(User.getFromDTO(userDto), pageRequest);
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
        Page<Score> scores = scoreRepository.findByPlace(Place.getFromDTO(placeDto), pageRequest);
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
