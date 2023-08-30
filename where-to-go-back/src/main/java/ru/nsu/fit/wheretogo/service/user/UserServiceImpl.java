package ru.nsu.fit.wheretogo.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.user.UserDTO;
import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.entity.user.User;
import ru.nsu.fit.wheretogo.exception.user.EmailAlreadyRegistered;
import ru.nsu.fit.wheretogo.exception.user.UserNotFoundException;
import ru.nsu.fit.wheretogo.exception.user.UsernameAlreadyRegistered;
import ru.nsu.fit.wheretogo.repository.user.UserRepository;
import ru.nsu.fit.wheretogo.utils.SecurityContextHelper;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDTO createUser(String email, String username, String password)
            throws EmailAlreadyRegistered, UsernameAlreadyRegistered {
        checkEmailIsFree(email);
        checkUsernameIsFree(username);
        User user = userRepository.save(new User(email, passwordEncoder.encode(password), username, Instant.now()));
        return UserDTO.getFromEntity(user);
    }

    @Override
    @Transactional
    public UserDTO setCurrentUsername(String username) throws UsernameAlreadyRegistered, UserNotFoundException {
        var user = userRepository.findByEmail(SecurityContextHelper.email());
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        checkUsernameIsFree(username);

        user.get().setUsername(username);
        return UserDTO.getFromEntity(user.get());
    }

    @Override
    @Transactional
    public void setCurrentPassword(String password) throws UserNotFoundException {
        var user = userRepository.findByEmail(SecurityContextHelper.email());
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        user.get().setPassword(passwordEncoder.encode(password));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        return UserDTO.getFromEntity(userRepository.findByEmail(SecurityContextHelper.email()).orElse(null));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceBriefDTO> getFavourites() {
        return userRepository.findByEmail(SecurityContextHelper.email())
                .orElseThrow().getFavouritePlaces().stream().map(
                        PlaceBriefDTO::getFromEntity
                ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceBriefDTO> getVisited() {
        return userRepository.findByEmail(SecurityContextHelper.email())
                .orElseThrow().getVisitedPlaces().stream().map(
                        PlaceBriefDTO::getFromEntity
                ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean findVisitedById(Long placeId) {
        List<PlaceBriefDTO> userVisitedPlaces = userRepository.findByEmail(
                        SecurityContextHelper.email())
                .orElseThrow()
                .getVisitedPlaces()
                .stream().
                map(PlaceBriefDTO::getFromEntity).
                toList();

        for (PlaceBriefDTO place : userVisitedPlaces) {
            if (Objects.equals(place.getId(), placeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean findFavouriteById(Long placeId) {
        List<PlaceBriefDTO> userFavouritePlaces = userRepository.findByEmail(
                        SecurityContextHelper.email())
                .orElseThrow()
                .getFavouritePlaces()
                .stream().
                map(PlaceBriefDTO::getFromEntity).
                toList();

        for (PlaceBriefDTO place : userFavouritePlaces) {
            if (Objects.equals(place.getId(), placeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void addFavourite(Long placeId) {
        if (placeId == null) {
            return;
        }
        var place = new Place();
        place.setId(placeId);
        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getFavouritePlaces().add(place)
        );
    }

    @Override
    @Transactional
    public void addVisited(Long placeId) {
        if (placeId == null) {
            return;
        }
        var place = new Place();
        place.setId(placeId);
        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getVisitedPlaces().add(place)
        );
    }

    @Override
    @Transactional
    public void deleteVisited(Long placeId) {
        if (placeId == null) {
            return;
        }

        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getVisitedPlaces()
                        .removeIf(place -> Objects.equals(place.getId(), placeId)));

    }

    @Override
    @Transactional
    public void deleteFavourite(Long placeId) {
        if (placeId == null) {
            return;
        }

        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getFavouritePlaces()
                        .removeIf(place -> Objects.equals(place.getId(), placeId)));
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
}
