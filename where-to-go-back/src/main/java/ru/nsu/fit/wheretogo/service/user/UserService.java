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
     * @param email    эл. почта пользователя
     * @param username имя пользователя
     * @param password пароль в кодировке base64
     * @return данные созданного пользователя
     * @throws EmailAlreadyRegistered    ошибка в случае, если эл. почта уже существует в системе
     * @throws UsernameAlreadyRegistered ошибка в случае, если имя пользователя уже занято
     */
    UserDTO createUser(String email, String username, String password)
            throws EmailAlreadyRegistered, UsernameAlreadyRegistered;

    /**
     * Изменить имя пользователя
     *
     * @param username имя пользователя
     * @return данные обновленного пользователя
     * @throws UsernameAlreadyRegistered ошибка в случае, если имя пользователя уже занято
     * @throws UserNotFoundException     ошибка, в случае если пользователь не найден
     */
    UserDTO setCurrentUsername(String username)
            throws UsernameAlreadyRegistered, UserNotFoundException;

    /**
     * Изменить пароль пользователя
     *
     * @param password пароль
     * @throws UserNotFoundException ошибка, в случае если пользователь не найден
     */
    void setCurrentPassword(String password) throws UserNotFoundException;

    /**
     * Получить данные пользователя в рамках текущей сессии
     *
     * @return данные пользователя
     */
    UserDTO getCurrentUser();

    /**
     * Получить список избранных мест текущего пользователя
     *
     * @return список мест
     */
    List<PlaceBriefDTO> getFavourites();

    /**
     * Получить список посещенных мест пользователя
     *
     * @return список мест
     */
    List<PlaceBriefDTO> getVisited();

    /**
     * Проверка, что место в списке посещенных пользователя
     *
     * @param placeId идентификатор места
     * @return true или false
     */
    boolean findVisitedById(Long placeId);

    /**
     * Проверка, что место в списке избранных пользователя
     *
     * @param placeId идентификатор места
     * @return true или false
     */
    boolean findFavouriteById(Long placeId);

    /**
     * Добавить место в список избранных пользователя
     *
     * @param placeId идентификатор места
     */
    void addFavourite(Long placeId);

    /**
     * Добавить место в список посещенных пользователя
     *
     * @param placeId идентификатор места
     */
    void addVisited(Long placeId);

    /**
     * Удалить место из списка посещенных пользователя
     *
     * @param placeId идентификатор места
     */
    void deleteVisited(Long placeId);

    /**
     * Удалить место из списка избранных пользователя
     *
     * @param placeId идентификатор места
     */
    void deleteFavourite(Long placeId);
}
