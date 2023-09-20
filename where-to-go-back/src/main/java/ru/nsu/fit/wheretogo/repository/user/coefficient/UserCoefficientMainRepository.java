package ru.nsu.fit.wheretogo.repository.user.coefficient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.user.coefficient.UserCoefficient;

import java.util.List;

public interface UserCoefficientMainRepository extends JpaRepository<UserCoefficient, Long> {

    List<UserCoefficient> getAllByUserId(Long userId);

    boolean existsByCategory(Category category);

    @Modifying
    @Query(value = "UPDATE T_USER_COEFFICIENT_MAIN " +
            "SET COEFFICIENT = ((COEFFICIENT) + (:_new_score/COUNT_PLACES))*(COUNT_PLACES/(COUNT_PLACES+1))," +
            " COUNT_PLACES = COUNT_PLACES + 1 " +
            "WHERE CATEGORY_ID = :_category_id AND USER_ID = :_user_id", nativeQuery = true)
    void updateCoefficientWithIncrement(
            @Param("_new_score") Double newScore,
            @Param("_category_id") Long categoryId,
            @Param("_user_id") Long userId
    );

    @Modifying
    @Query(value = "UPDATE T_USER_COEFFICIENT_MAIN SET COEFFICIENT = ((COEFFICIENT) + (:_new_score/COUNT_PLACES))" +
            "WHERE CATEGORY_ID = :_category_id and USER_ID = :_user_id", nativeQuery = true)
    void updateCoefficientWithoutIncrement(
            @Param("_new_score") Double newScore,
            @Param("_category_id") Long categoryId,
            @Param("_user_id") Long userId
    );
}
