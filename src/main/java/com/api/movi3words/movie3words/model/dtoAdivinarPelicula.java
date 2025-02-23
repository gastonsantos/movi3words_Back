package com.api.movi3words.movie3words.model;

public class dtoAdivinarPelicula {
	private int sala;
	private String pelicula;
	
	
	
	public dtoAdivinarPelicula() {
		
	}
	public dtoAdivinarPelicula(int sala, String pelicula) {

		this.sala = sala;
		this.pelicula = pelicula;
	}
	public int getSala() {
		return sala;
	}
	public void setSala(int sala) {
		this.sala = sala;
	}
	public String getPelicula() {
		return pelicula;
	}
	public void setPelicula(String pelicula) {
		this.pelicula = pelicula;
	}
	

}
