package ru.nsu.fit.wheretogo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.UserDTO;
import ru.nsu.fit.wheretogo.exception.EmailAlreadyRegistered;
import ru.nsu.fit.wheretogo.exception.UsernameAlreadyRegistered;
import ru.nsu.fit.wheretogo.service.UserService;
import ru.nsu.fit.wheretogo.utils.SecurityContextHelper;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password) throws EmailAlreadyRegistered, UsernameAlreadyRegistered {
        return userService.createUser(email, username, password);
    }

    @PostMapping("/username")
    public UserDTO setUsername(@RequestParam(name = "username") String username) throws UsernameAlreadyRegistered {
        return userService.setCurrentUsername(username);
    }

    @PostMapping("/password")
    public void setPassword(@RequestParam(name = "password") String password) {
        userService.setCurrentPassword(password);
    }

    @PostMapping("/logout")
    public void logout() {
        SecurityContextHelper.setNotAuthenticated();
    }

    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser() {
        return checkIfFound(userService.getCurrentUserDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id") Long userId) {
        UserDTO user = userService.getUser(userId);
        if (user != null) {
            user.setEmail(null).setCreatedAt(null);
        }
        return checkIfFound(user);
    }

    private ResponseEntity<UserDTO> checkIfFound(UserDTO userDto) {
        return userDto != null ?
                ResponseEntity.ok(userDto) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/contain/visited")
    private ResponseEntity<Boolean> isUserHasVisited() {
        return new ResponseEntity<>(!userService.getVisited().isEmpty(), HttpStatus.OK);
    }

    @GetMapping("/contain/favourites")
    private ResponseEntity<Boolean> isUserHasFavourites() {
        return new ResponseEntity<>(!userService.getFavourite().isEmpty(), HttpStatus.OK);
    }

    @GetMapping("/contain/visited/{id}")
    private ResponseEntity<Boolean> isPlaceInUserVisited(
            @PathVariable(name = "id") Long placeId) {
        return new ResponseEntity<>(userService.isPlaceInVisited(placeId), HttpStatus.OK);
    }
}
