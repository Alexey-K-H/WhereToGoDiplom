package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.UserCoefficient;

import java.util.List;

public interface UserCoeffRepository extends JpaRepository<UserCoefficient, Long> {

    //Получить список коэффициентов пользователя
    List<UserCoefficient> getAllByUserId(Long userId);

    //Существует ли коэффициент для этой ктаегории
    boolean existsByCategory(Category category);

    //Обновление коэффициента с добавлением нового места
    @Modifying
    @Query(value = "update user_coeff u set u.coeff = ((u.coeff) + (:_new_score/u.count_places))*(u.count_places/(u.count_places+1)), u.count_places = u.count_places + 1 " +
            "where u.category_id = :_category_id and u.user_id = :_user_id", nativeQuery = true)
    void updateCoeffWithInc(
            @Param("_new_score") Double newScore,
            @Param("_category_id") Integer categoryId,
            @Param("_user_id") Long userId
    );

    //Обновление коэффициента без добавления нового места
    @Modifying
    @Query(value = "update user_coeff u set u.coeff = ((u.coeff) + (:_new_score/u.count_places))*(u.count_places/(u.count_places+1))" +
            "where u.category_id = :_category_id and u.user_id = :_user_id", nativeQuery = true)
    void updateCoeffWitOutInc(
            @Param("_new_score") Double newScore,
            @Param("_category_id") Integer categoryId,
            @Param("_user_id") Long userId
    );
}
