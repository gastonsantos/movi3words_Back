package com.api.movi3words.movie3words.exception;

import org.springframework.http.HttpStatus;

public class ImagenesNoEncontradaExcepcion extends ExceptionGeneric{
	public ImagenesNoEncontradaExcepcion(){
		
		this.setCode("403");
		this.setMessage("No se pudo encontrar Imagen de la pelicula.");
		this.setStatus(HttpStatus.BAD_REQUEST);
	}
	
}
