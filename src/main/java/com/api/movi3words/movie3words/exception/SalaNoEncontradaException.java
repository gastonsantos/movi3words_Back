package com.api.movi3words.movie3words.exception;

import org.springframework.http.HttpStatus;

public class SalaNoEncontradaException extends ExceptionGeneric{
	
	public SalaNoEncontradaException(){
		this.setCode("400");
		this.setMessage("No se pudo encontrar la Sala.");
		this.setStatus(HttpStatus.CONFLICT);
	}
}
