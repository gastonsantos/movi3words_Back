package com.api.movi3words.movie3words.exception;

import org.springframework.http.HttpStatus;

public class SalaYaExisteException extends ExceptionGeneric {
	public SalaYaExisteException(){
		
		this.setCode("405");
		this.setMessage("No se pudo crear sala, Sala ya existente.");
		this.setStatus(HttpStatus.BAD_REQUEST);
	}
}
