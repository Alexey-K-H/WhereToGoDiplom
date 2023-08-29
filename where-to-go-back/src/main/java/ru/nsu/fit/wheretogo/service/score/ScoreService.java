package ru.nsu.fit.wheretogo.service.score;

import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.dto.user.ScoreDTO;
import ru.nsu.fit.wheretogo.dto.user.UserDTO;

/**
 * Сервис по работе с оценками пользователей
 */
public interface ScoreService {

    /**
     * Добавить оценку пользователя
     *
     * @param userId
     * @param placeId
     * @param value
     * @return
     */
    ScoreDTO createScore(Long userId, Long placeId, Integer value);

    /**
     * Удалить оценку пользователя
     *
     * @param scoreDTO
     */
    void deleteScore(ScoreDTO scoreDTO);

    /**
     * Получить оценку места от конкретного пользователя
     *
     * @param userId
     * @param placeId
     * @return
     */
    ScoreDTO getByPlaceScoreFromUser(Long userId, Long placeId);

    /**
     * Получить все оценки пользователя
     *
     * @param userDto
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<ScoreDTO> getByUser(UserDTO userDto, int page, int size);

    /**
     * Получить все оценки места
     *
     * @param placeDto
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<ScoreDTO> getByPlace(PlaceDescriptionDTO placeDto, int page, int size);

    /**
     * Проверка того, что у пользователя есть оценки
     *
     * @param userId
     * @return
     */
    boolean ifUserHasScores(Long userId);
}
