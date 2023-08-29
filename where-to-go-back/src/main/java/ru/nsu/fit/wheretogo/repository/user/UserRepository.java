package ru.nsu.fit.wheretogo.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.wheretogo.entity.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);
}
