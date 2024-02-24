package ru.nsu.fit.wheretogo.repository.user.coefficient;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.user.coefficient.UserCoefficient;

import java.util.List;
import java.util.Optional;

public interface UserCoefficientRepository extends JpaRepository<UserCoefficient, Long> {

    List<UserCoefficient> getAllByUserId(Long userId);

    boolean existsByCategoryAndUserId(Category category, Long userId);

    Optional<UserCoefficient> findByUserIdAndCategoryId(Long userId, Long categoryId);
}
