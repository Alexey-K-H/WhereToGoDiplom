package ru.nsu.fit.wheretogo.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.user.UserDTO;
import ru.nsu.fit.wheretogo.exception.user.EmailAlreadyRegistered;
import ru.nsu.fit.wheretogo.exception.user.UsernameAlreadyRegistered;
import ru.nsu.fit.wheretogo.service.user.UserService;
import ru.nsu.fit.wheretogo.utils.SecurityContextHelper;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDTO register(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password) throws EmailAlreadyRegistered, UsernameAlreadyRegistered {
        return userService.createUser(email, username, password);
    }

    @PostMapping("/username")
    public ResponseEntity<UserDTO> setUsername(@RequestParam(name = "username") String username) {
        try {
            var result = userService.setCurrentUsername(username);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/password")
    public ResponseEntity<Void> setPassword(@RequestParam(name = "password") String password) {
        try {
            userService.setCurrentPassword(password);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/logout")
    public void logout() {
        SecurityContextHelper.setNotAuthenticated();
    }

    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser() {
        return checkIfFound(userService.getCurrentUser());
    }

    private ResponseEntity<UserDTO> checkIfFound(UserDTO userDto) {
        return userDto != null ?
                ResponseEntity.ok(userDto) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/contain/visited")
    public ResponseEntity<Boolean> isUserHasVisited() {
        return new ResponseEntity<>(!userService.getVisited().isEmpty(), HttpStatus.OK);
    }

    @GetMapping("/contain/favourites")
    public ResponseEntity<Boolean> isUserHasFavourites() {
        return new ResponseEntity<>(!userService.getFavourites().isEmpty(), HttpStatus.OK);
    }

    @GetMapping("/contain/visited/{id}")
    public ResponseEntity<Boolean> isUserVisitCurrentPlace(
            @PathVariable(name = "id") Long placeId) {
        return new ResponseEntity<>(userService.findVisitedById(placeId), HttpStatus.OK);
    }

}
