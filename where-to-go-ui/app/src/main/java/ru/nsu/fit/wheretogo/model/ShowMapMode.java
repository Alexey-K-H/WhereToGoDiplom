package ru.nsu.fit.wheretogo.model;

/**
 * Режимы отображения мест на карте:
 * <ul>
 *     <li>ALL - отображение всех мест, которые есть в базе данных</li>
 *     <li>NEAREST - отображение мест, расположенных близко к пользователю</li>
 *     <li>RECOMMENDED_VISITED - отображение мест-рекомендаций на основе посещенных пользователем мест</li>
 *     <li>RECOMMENDED_SCORED - отображение мест-рекомендаций на основе оценок пользователя</li>
 *     <li>RECOMMENDED_USERS - отображение мест-рекомендаций на основе оценок других пользователей</li>
 *     <li>RECOMMENDED_ROUTE - отображение путей-рекомендаций</li>
 * </ul>
 */
public enum ShowMapMode {
    ALL,
    NEAREST,
    RECOMMENDED_VISITED,
    RECOMMENDED_SCORED,
    RECOMMENDED_USERS,
    RECOMMENDED_ROUTE
}
