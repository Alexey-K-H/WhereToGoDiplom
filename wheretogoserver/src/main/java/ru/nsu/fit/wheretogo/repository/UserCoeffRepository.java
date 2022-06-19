package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.user_coeff.UserCoefficient;

import java.util.List;

public interface UserCoeffRepository extends JpaRepository<UserCoefficient, Long> {

    //Получить список коэффициентов пользователя
    List<UserCoefficient> getAllByUserId(Long userId);

    //Существует ли коэффициент для этой ктаегории
    boolean existsByCategory(Category category);

    //Обновление коэффициента с добавлением нового места
    @Modifying
    @Query(value = "update where_to_go.user_coeff set coeff = ((coeff) + (:_new_score/count_places))*(count_places/(count_places+1)), count_places = count_places + 1 " +
            "where category_id = :_category_id and user_id = :_user_id", nativeQuery = true)
    void updateCoeffWithInc(
            @Param("_new_score") Double newScore,
            @Param("_category_id") Integer categoryId,
            @Param("_user_id") Long userId
    );

    //Обновление коэффициента без добавления нового места
    @Modifying
    @Query(value = "update where_to_go.user_coeff set coeff = ((coeff) + (:_new_score/count_places))" +
            "where category_id = :_category_id and user_id = :_user_id", nativeQuery = true)
    void updateCoeffWitOutInc(
            @Param("_new_score") Double newScore,
            @Param("_category_id") Integer categoryId,
            @Param("_user_id") Long userId
    );
}
