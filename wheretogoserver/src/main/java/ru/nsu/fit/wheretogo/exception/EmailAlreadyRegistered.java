package ru.nsu.fit.wheretogo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "email_already_registered")
public class EmailAlreadyRegistered extends Exception {
}
