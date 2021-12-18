package ru.nsu.fit.wheretogo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.UserDto;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.User;
import ru.nsu.fit.wheretogo.exception.EmailAlreadyRegistered;
import ru.nsu.fit.wheretogo.exception.UsernameAlreadyRegistered;
import ru.nsu.fit.wheretogo.repository.UserRepository;
import ru.nsu.fit.wheretogo.utils.SecurityContextHelper;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(String email, String username, String password) throws EmailAlreadyRegistered, UsernameAlreadyRegistered {
        checkEmailIsFree(email);
        checkUsernameIsFree(username);
        User user = userRepository.save(new User(email, passwordEncoder.encode(password), username, Instant.now()));
        return UserDto.getFromEntity(user);
    }

    @Transactional
    public UserDto setCurrentUsername(String username) throws UsernameAlreadyRegistered {
        User user = userRepository.findByEmail(SecurityContextHelper.email()).get();
        checkUsernameIsFree(username);
        user.setUsername(username);
        return UserDto.getFromEntity(user);
    }

    @Transactional
    public void setCurrentPassword(String password) {
        userRepository
                .findByEmail(SecurityContextHelper.email()).get()
                .setPassword(passwordEncoder.encode(password));
        SecurityContextHelper.setNotAuthenticated();
    }

    //TODO:Добавили поиск не только сущности но и DTO объекта
    public UserDto getCurrentUserDto() {
        return UserDto.getFromEntity(userRepository.findByEmail(SecurityContextHelper.email()).orElse(null));
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(SecurityContextHelper.email()).orElse(null);
    }
    public UserDto getUser(Long userId) {
        return UserDto.getFromEntity(userRepository.findById(userId).orElse(null));
    }

    private void checkUsernameIsFree(String username) throws UsernameAlreadyRegistered {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyRegistered();
        }
    }

    private void checkEmailIsFree(String email) throws EmailAlreadyRegistered {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegistered();
        }
    }

    //TODO:Добавлены запросы для добавления избранных, посещенных и получения информации о них
    @Transactional(readOnly = true)
    public List<PlaceBriefDTO> getFavourite() {
        return userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getFavouritePlaces().stream().map(
                PlaceBriefDTO::getFromEntity
        ).toList();
    }

    @Transactional(readOnly = true)
    public List<PlaceBriefDTO> getVisited() {
        return userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getVisitedPlaces().stream().map(
                PlaceBriefDTO::getFromEntity
        ).toList();
    }

    @Transactional
    public void addFavourite(Long placeId) {
        if (placeId == null) {
            return;
        }
        Place place = new Place();
        place.setId(placeId);
        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getFavouritePlaces().add(place)
        );
    }

    @Transactional
    public void addVisited(Long placeId) {
        if (placeId == null) {
            return;
        }
        Place place = new Place();
        place.setId(placeId);
        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getVisitedPlaces().add(place)
        );
    }
}
