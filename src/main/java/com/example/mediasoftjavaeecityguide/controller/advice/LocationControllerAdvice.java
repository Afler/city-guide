package com.example.mediasoftjavaeecityguide.controller.advice;

import com.example.mediasoftjavaeecityguide.service.LocationNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Hidden
@RestControllerAdvice
public class LocationControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(exception = LocationNotFoundException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(WebRequest request) {
        String locationName = request.getParameter("locationName");
        return new ResponseEntity<>("Достопримечательность %s не найдена.".formatted(locationName), HttpStatusCode.valueOf(500));
    }

}
