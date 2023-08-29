package ru.nsu.fit.wheretogo.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "user_not_found")
public class UserNotFoundException extends Exception {
}
