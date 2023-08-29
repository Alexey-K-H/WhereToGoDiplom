package ru.nsu.fit.wheretogo.service.score;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.dto.user.ScoreDTO;
import ru.nsu.fit.wheretogo.dto.user.UserDTO;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.score.ScoreId;
import ru.nsu.fit.wheretogo.entity.user.User;
import ru.nsu.fit.wheretogo.entity.user.coefficient.main.UserCoefficientMain;
import ru.nsu.fit.wheretogo.entity.user.coefficient.main.UserCoefficientMainId;
import ru.nsu.fit.wheretogo.repository.place.PlaceRepository;
import ru.nsu.fit.wheretogo.repository.score.ScoreRepository;
import ru.nsu.fit.wheretogo.repository.user.coefficient.UserCoefficientMainRepository;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;
    private final UserCoefficientMainRepository userCoefficientMainRepository;
    private final PlaceRepository placeRepository;

    public ScoreServiceImpl(
            ScoreRepository scoreRepository,
            UserCoefficientMainRepository userCoefficientMainRepository,
            PlaceRepository placeRepository) {
        this.scoreRepository = scoreRepository;
        this.userCoefficientMainRepository = userCoefficientMainRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    @Transactional
    public ScoreDTO createScore(Long userId, Long placeId, Integer value) {
        var oldValue = 0;
        if (scoreRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            var currScore = scoreRepository.findByUserIdAndPlaceId(userId, placeId);
            oldValue = currScore.getScoreValue();
        }

        Score score = scoreRepository.save(new Score()
                .setId(new ScoreId().setUser(userId).setPlace(placeId))
                .setUser(new User().setId(userId))
                .setPlace(new Place().setId(placeId))
                .setScoreValue(value));

        List<Category> placeCategories = placeRepository.findById(placeId).orElseThrow().getCategories();

        for (Category category : placeCategories) {
            if (userCoefficientMainRepository.existsByCategory(category)) {
                if (oldValue != 0) {
                    double newValue = (value - oldValue);
                    userCoefficientMainRepository.updateCoefficientWithoutIncrement(newValue, category.getId(), userId);
                } else {
                    userCoefficientMainRepository.updateCoefficientWithIncrement(value.doubleValue(), category.getId(), userId);
                }

            } else {
                userCoefficientMainRepository.save(new UserCoefficientMain()
                        .setCoefficientId(new UserCoefficientMainId().setUser(userId).setCategory(category.getId()))
                        .setUser(new User().setId(userId))
                        .setCategory(category)
                        .setPlacesCount(1L)
                        .setCoefficient(value.doubleValue()));
            }
        }
        return ScoreDTO.getFromEntity(score);
    }

    @Override
    @Transactional
    public void deleteScore(ScoreDTO scoreDTO) {
        scoreRepository.delete(Score.getFromDto(scoreDTO));
    }

    @Override
    @Transactional
    public ScoreDTO getByPlaceScoreFromUser(Long userId, Long placeId) {
        if (userId == null || placeId == null) {
            return null;
        }
        return ScoreDTO.getFromEntity(scoreRepository.findByUserIdAndPlaceId(userId, placeId));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedListDTO<ScoreDTO> getByUser(UserDTO userDto, int page, int size) {
        var pageRequest = PageRequest.of(page, size);
        Page<Score> scores = scoreRepository.findByUserId(User.getFromDTO(userDto).getId(), pageRequest);
        List<ScoreDTO> userScoresDtos = scores.toList()
                .stream()
                .map(ScoreDTO::getFromEntity)
                .toList();

        var listDto = new PagedListDTO<ScoreDTO>();
        listDto.setList(userScoresDtos);
        listDto.setPageNum(page);
        listDto.setTotalPages(scores.getTotalPages());

        return listDto;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedListDTO<ScoreDTO> getByPlace(PlaceDescriptionDTO placeDto, int page, int size) {
        var pageRequest = PageRequest.of(page, size);
        Page<Score> scores = scoreRepository.findByPlaceId(Place.getFromDTO(placeDto).getId(), pageRequest);
        List<ScoreDTO> userScoresDtos = scores.toList()
                .stream()
                .map(ScoreDTO::getFromEntity)
                .toList();

        var listDto = new PagedListDTO<ScoreDTO>();
        listDto.setList(userScoresDtos);
        listDto.setPageNum(page);
        listDto.setTotalPages(scores.getTotalPages());

        return listDto;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean ifUserHasScores(Long userId) {
        return scoreRepository.existsByUserId(userId);
    }
}
