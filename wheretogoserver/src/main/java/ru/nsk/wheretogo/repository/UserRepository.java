package ru.nsk.wheretogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsk.wheretogo.entity.User;


public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);
}
