package com.api.movi3words.movie3words.cotroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.api.movi3words.movie3words.exception.ExceptionGeneric;
import com.api.movi3words.movie3words.model.ErrorDto;

@RestControllerAdvice
	public class ControllerAdvice {
		@ExceptionHandler(value = ExceptionGeneric.class)
		public ResponseEntity<ErrorDto> handleExceptionGeneric(ExceptionGeneric ex) {
		    ErrorDto error = new ErrorDto(ex.getCode(), ex.getMessage());
		    return new ResponseEntity<>(error, ex.getStatus());
		}

}
