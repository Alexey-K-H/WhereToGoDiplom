package ru.nsu.fit.wheretogo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.UserDTO;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.User;
import ru.nsu.fit.wheretogo.exception.EmailAlreadyRegistered;
import ru.nsu.fit.wheretogo.exception.UsernameAlreadyRegistered;
import ru.nsu.fit.wheretogo.repository.UserRepository;
import ru.nsu.fit.wheretogo.utils.SecurityContextHelper;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createUser(String email, String username, String password) throws EmailAlreadyRegistered, UsernameAlreadyRegistered {
        checkEmailIsFree(email);
        checkUsernameIsFree(username);
        User user = userRepository.save(new User(email, passwordEncoder.encode(password), username, Instant.now()));
        return UserDTO.getFromEntity(user);
    }

    @Transactional
    public UserDTO setCurrentUsername(String username) throws UsernameAlreadyRegistered {
        User user = userRepository.findByEmail(SecurityContextHelper.email()).get();
        checkUsernameIsFree(username);
        user.setUsername(username);
        return UserDTO.getFromEntity(user);
    }

    @Transactional
    public void setCurrentPassword(String password) {
        userRepository
                .findByEmail(SecurityContextHelper.email()).get()
                .setPassword(passwordEncoder.encode(password));
        SecurityContextHelper.setNotAuthenticated();
    }

    //TODO:Добавили поиск не только сущности но и DTO объекта
    public UserDTO getCurrentUserDto() {
        return UserDTO.getFromEntity(userRepository.findByEmail(SecurityContextHelper.email()).orElse(null));
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(SecurityContextHelper.email()).orElse(null);
    }
    public UserDTO getUser(Long userId) {
        return UserDTO.getFromEntity(userRepository.findById(userId).orElse(null));
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

    @Transactional(readOnly = true)
    public boolean findVisitedById(Long placeId){
        List<PlaceBriefDTO> userVisitedPlaces =  userRepository.findByEmail(SecurityContextHelper.email())
                .orElseThrow()
                .getVisitedPlaces()
                .stream().
                map(PlaceBriefDTO::getFromEntity).
                toList();

        for(PlaceBriefDTO place : userVisitedPlaces){
            if(Objects.equals(place.getId(), placeId)){
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean findFavouriteById(Long placeId){
        List<PlaceBriefDTO> userFavouritePlaces =  userRepository.findByEmail(SecurityContextHelper.email())
                .orElseThrow()
                .getFavouritePlaces()
                .stream().
                map(PlaceBriefDTO::getFromEntity).
                toList();

        for(PlaceBriefDTO place : userFavouritePlaces){
            if(Objects.equals(place.getId(), placeId)){
                return true;
            }
        }
        return false;
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

    @Transactional
    public void deleteVisited(Long placeId){
        if(placeId == null){
            return;
        }

        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getVisitedPlaces().removeIf(place -> Objects.equals(place.getId(), placeId)));

    }

    @Transactional
    public void deleteFavourite(Long placeId){
        if(placeId == null){
            return;
        }

        userRepository.findByEmail(SecurityContextHelper.email()).ifPresent(
                currentUser -> currentUser.getFavouritePlaces().removeIf(place -> Objects.equals(place.getId(), placeId)));
    }
}
