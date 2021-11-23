package ru.nsk.wheretogo.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsk.wheretogo.dto.UserDto;
import ru.nsk.wheretogo.entity.User;
import ru.nsk.wheretogo.repository.UserRepository;
import ru.nsk.wheretogo.utils.SecurityContextHelper;

import javax.transaction.Transactional;
import java.time.Instant;
import javax.validation.ValidationException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserDto getCurrentUser() {
    return UserDto.getFromEntity(userRepository.findById(SecurityContextHelper.userId()).orElse(null));
    }

    public UserDto getUser(Integer id) {
        return UserDto.getFromEntity(userRepository.findById(id).orElse(null));
    }

    public void deleteCurrentUser() {
        userRepository.deleteById(SecurityContextHelper.userId());
    }

    private void validateUser(UserDto userDto) {
    if (userRepository.existsByEmail(userDto.getEmail())) {
        throw new ValidationException("User with that email already exists");
    }

    }
    @Transactional
    public void saveUser(UserDto userDto) {
       validateUser(userDto);
       User user = userRepository.findById(userDto.getId()).orElse(null);
       userRepository.save(User.getFromDTO(userDto))
               .setCreated_at(user!= null ? user.getCreated_at() : Instant.now());

    }


}