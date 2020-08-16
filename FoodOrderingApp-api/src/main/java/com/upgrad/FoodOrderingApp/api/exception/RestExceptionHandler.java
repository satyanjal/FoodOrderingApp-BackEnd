package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException exception) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> couponNotFounException(CouponNotFoundException exception){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()), HttpStatus.NOT_FOUND);
    }
}
