package ru.nsu.fit.wheretogo.model;

/*Режимы отображения мест на карте:
    ALL - отображение всех мест, которые есть в базе данных
    NEAREST - отображение мест, расположенных близко к пользователю
    RECOMMENDED_VISITED - отображение мест-рекомендаций на основе посещнных пользователем мест
    RECOMMENDED_SCORED - отображение мест-рекомендаций на основе оценок пользователя
    RECOMMENDED_USERS - отображение мест-рекомендаций на основе оценок других пользователей
*/

public enum ShowMapMode {
    ALL,
    NEAREST,
    RECOMMENDED_VISITED,
    RECOMMENDED_SCORED,
    RECOMMENDED_USERS
}
