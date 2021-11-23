package ru.nsk.wheretogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsk.wheretogo.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
}
