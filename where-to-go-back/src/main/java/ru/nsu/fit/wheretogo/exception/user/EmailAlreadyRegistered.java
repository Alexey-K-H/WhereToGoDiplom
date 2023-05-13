package ru.nsu.fit.wheretogo.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "email_already_registered")
public class EmailAlreadyRegistered extends Exception {
}
