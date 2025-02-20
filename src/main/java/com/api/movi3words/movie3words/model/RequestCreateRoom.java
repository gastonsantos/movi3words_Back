package com.api.movi3words.movie3words.model;

public class RequestCreateRoom {

	private String sala;
	private PeliculaModel pelicula;
	
	
	public RequestCreateRoom() {
		
		
	}
	public RequestCreateRoom(String sala, PeliculaModel pelicula) {
		
		this.sala = sala;
		this.pelicula = pelicula;
	}
	public String getSala() {
		return sala;
	}
	public void setSala(String sala) {
		this.sala = sala;
	}
	public PeliculaModel getPelicula() {
		return pelicula;
	}
	public void setPelicula(PeliculaModel pelicula) {
		this.pelicula = pelicula;
	}
	
	
}
