package ru.nsu.fit.wheretogo.service.score;

import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.dto.user.ScoreDTO;
import ru.nsu.fit.wheretogo.dto.user.UserDTO;

import java.util.List;

/**
 * Сервис по работе с оценками пользователей
 */
public interface ScoreService {

    /**
     * Добавить оценку пользователя
     *
     * @param userId  идентификатор пользователя
     * @param placeId идентификатор места
     * @param value   значение оценки
     * @return данные сформированной оценки
     */
    ScoreDTO createScore(Long userId, Long placeId, Integer value);

    /**
     * Удалить оценку пользователя
     *
     * @param scoreDTO данные запроса
     */
    void deleteScore(ScoreDTO scoreDTO);

    /**
     * Получить оценку места от конкретного пользователя
     *
     * @param userId  идентификатор пользователя
     * @param placeId идентификатор места
     * @return оценка пользователя
     */
    ScoreDTO getByPlaceScoreFromUser(Long userId, Long placeId);

    /**
     * Получить все оценки пользователя
     *
     * @param userDto данные пользователя
     * @return список оценок
     */
    List<ScoreDTO> getByUser(UserDTO userDto);

    /**
     * Получить все оценки места
     *
     * @param placeDto данные места
     * @return список оценок
     */
    List<ScoreDTO> getByPlace(PlaceDescriptionDTO placeDto);

    /**
     * Проверка того, что у пользователя есть оценки
     *
     * @param userId идентификатор пользователя
     * @return true или false
     */
    boolean ifUserHasScores(Long userId);
}
