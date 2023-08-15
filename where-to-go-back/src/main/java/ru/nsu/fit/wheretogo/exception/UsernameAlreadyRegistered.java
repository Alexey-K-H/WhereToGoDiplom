package ru.nsu.fit.wheretogo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "username_already_registered")
public class UsernameAlreadyRegistered extends Exception {
}
