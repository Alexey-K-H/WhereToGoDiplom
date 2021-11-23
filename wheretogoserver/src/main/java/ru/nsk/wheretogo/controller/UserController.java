package ru.nsk.wheretogo.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsk.wheretogo.dto.UserDto;
import ru.nsk.wheretogo.service.UserService;
import ru.nsk.wheretogo.utils.SecurityContextHelper;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getCurrentUser() {
        return checkIfFound(userService.getCurrentUser());
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto user) {
        if (!SecurityContextHelper.userId().equals(user.getId())) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        userService.saveUser(user);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/delete")
    public void deleteCurrent() {
        userService.deleteCurrentUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "id") Integer userId) {
    UserDto user = userService.getUser(userId);
    if (user != null)
    {
        user.setEmail(null).setCreated_at(null);
    }
     return checkIfFound(user);
    }

    private ResponseEntity<UserDto> checkIfFound(UserDto userDto) {
        return userDto != null ?
                ResponseEntity.ok(userDto) :
                new ResponseEntity<>(HttpStatus.OK);
    }
}

