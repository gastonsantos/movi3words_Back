package com.api.movi3words.movie3words.service;

import com.api.movi3words.movie3words.cotroller.dtoMensaje;
import com.api.movi3words.movie3words.model.PeliculaModel;
import com.api.movi3words.movie3words.model.RequestCohere;
import com.api.movi3words.movie3words.model.RequestCreateRoom;
import com.api.movi3words.movie3words.model.dtoAdivinarPelicula;

public interface IPeliculaService {
	public PeliculaModel obtenerPelicula(int dificultad);
	public RequestCreateRoom crearSala(int dificultad);
	public String adivinarPelicula(dtoAdivinarPelicula mensaje);
	public PeliculaModel cambiarPelicula(String idRoom, int dificultad);
	public String obtenerTresPalabras(RequestCohere request);
	public String[] obtenerImagenesPelicula(int idPelicula);
}
