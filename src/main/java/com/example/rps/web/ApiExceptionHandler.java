package com.example.rps.web;

import com.example.rps.game.Choice;
import com.example.rps.game.MatchOverException;
import com.example.rps.web.dto.ErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Turns exceptions from the game API into JSON errors with the right status code.
 */
@RestControllerAdvice(assignableTypes = GameController.class)
public class ApiExceptionHandler {

    private static final String CHOICES = Arrays.toString(Choice.values());

    @ExceptionHandler(MatchOverException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse matchOver(MatchOverException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidRequest(MethodArgumentNotValidException e) {
        return new ErrorResponse("choice is required, one of " + CHOICES);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse unreadableRequest(HttpMessageNotReadableException e) {
        return new ErrorResponse("Request body must be JSON like {\"choice\": \"ROCK\"}, with choice one of " + CHOICES);
    }
}
