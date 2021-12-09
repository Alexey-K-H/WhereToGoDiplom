package ru.nsu.fit.wheretogo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.wheretogo.dto.UserDto;
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
    public UserDto register(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password) throws EmailAlreadyRegistered, UsernameAlreadyRegistered {
        return userService.createUser(email, username, password);
    }

    @PostMapping("/username")
    public UserDto setUsername(@RequestParam(name = "username") String username) throws UsernameAlreadyRegistered {
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
    public ResponseEntity<UserDto> getCurrentUser() {
        return checkIfFound(userService.getCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "id") Long userId) {
        UserDto user = userService.getUser(userId);
        if (user != null) {
            user.setEmail(null).setCreatedAt(null);
        }
        return checkIfFound(user);
    }

    private ResponseEntity<UserDto> checkIfFound(UserDto userDto) {
        return userDto != null ?
                ResponseEntity.ok(userDto) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
