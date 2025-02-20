package com.api.movi3words.movie3words.service;

import com.api.movi3words.movie3words.cotroller.dtoMensaje;
import com.api.movi3words.movie3words.model.PeliculaModel;
import com.api.movi3words.movie3words.model.RequestCohere;
import com.api.movi3words.movie3words.model.RequestCreateRoom;

public interface IPeliculaService {
	public PeliculaModel obtenerPelicula(int dificultad);
	public RequestCreateRoom crearSala(int dificultad);
	public String adivinarPelicula(dtoMensaje mensaje);
	public PeliculaModel cambiarPelicula(String idRoom, int dificultad);
	public String obtenerTresPalabras(RequestCohere request);
}
