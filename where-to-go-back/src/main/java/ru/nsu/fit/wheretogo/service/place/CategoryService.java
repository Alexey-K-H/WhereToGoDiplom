package ru.nsu.fit.wheretogo.service.place;

import ru.nsu.fit.wheretogo.dto.place.CategoryDTO;

import java.util.List;

/**
 * Сервис для работы с категориями
 */
public interface CategoryService {

    /**
     * Добавить категорию
     *
     * @param categoryDTO тело запроса
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * Удаление категории
     *
     * @param categoryDTO тело запроса
     */
    void deleteCategory(CategoryDTO categoryDTO);

    /**
     * Получить список всех категорий
     *
     * @return список категорий
     */
    List<CategoryDTO> getAllCategories();
}
