package com.api.movi3words.movie3words.exception;

import org.springframework.http.HttpStatus;

public class PeliculaNoEncontradaException extends ExceptionGeneric {

	public PeliculaNoEncontradaException(){
		this.setCode("402");
		this.setMessage("No se pudo encontrar Pelicula");
		this.setStatus(HttpStatus.BAD_REQUEST);
	}
	}
	
