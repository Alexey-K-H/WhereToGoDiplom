package ru.nsu.fit.wheretogo.service.user;

import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.user.UserDTO;
import ru.nsu.fit.wheretogo.exception.user.EmailAlreadyRegistered;
import ru.nsu.fit.wheretogo.exception.user.UserNotFoundException;
import ru.nsu.fit.wheretogo.exception.user.UsernameAlreadyRegistered;

import java.util.List;

/**
 * Сервис для работы с пользователями
 */
public interface UserService {

    /**
     * Создать пользователя
     *
     * @param email
     * @param username
     * @param password
     * @return
     * @throws EmailAlreadyRegistered
     * @throws UsernameAlreadyRegistered
     */
    UserDTO createUser(String email, String username, String password)
            throws EmailAlreadyRegistered, UsernameAlreadyRegistered;

    /**
     * Изменить имя пользователя
     *
     * @param username
     * @return
     * @throws UsernameAlreadyRegistered
     * @throws UserNotFoundException
     */
    UserDTO setCurrentUsername(String username)
            throws UsernameAlreadyRegistered, UserNotFoundException;

    /**
     * Изменить пароль пользователя
     *
     * @param password
     * @throws UserNotFoundException
     */
    void setCurrentPassword(String password) throws UserNotFoundException;

    /**
     * Получить данные пользователя в виде dto
     *
     * @return
     */
    UserDTO getCurrentUserDto();

    /**
     * Получить данные пользователя
     *
     * @param userId
     * @return
     */
    UserDTO getUser(Long userId);

    /**
     * Получить список избранных мест текущего пользователя
     *
     * @return
     */
    List<PlaceBriefDTO> getFavourites();

    /**
     * Получить список посещенных мест пользователя
     *
     * @return
     */
    List<PlaceBriefDTO> getVisited();

    /**
     * Проверка, что место в списке посещенных пользователя
     *
     * @param placeId
     * @return
     */
    boolean findVisitedById(Long placeId);

    /**
     * Проверка, что место в списке избранных пользователя
     *
     * @param placeId
     * @return
     */
    boolean findFavouriteById(Long placeId);

    /**
     * Добавить место в список избранных пользователя
     *
     * @param placeId
     */
    void addFavourite(Long placeId);

    /**
     * Добавить место в список посещенных пользователя
     *
     * @param placeId
     */
    void addVisited(Long placeId);

    /**
     * Удалить место из списка посещенных пользователя
     *
     * @param placeId
     */
    void deleteVisited(Long placeId);

    /**
     * Удалить место из списка избранных пользователя
     *
     * @param placeId
     */
    void deleteFavourite(Long placeId);
}
