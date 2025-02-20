package com.api.movi3words.movie3words.model;

public class RequestCohere {

	private String titulo;
	private String genero;
	private String sinpsis;
	
	
	public RequestCohere(String titulo, String genero, String sinpsis) {
	
		this.titulo = titulo;
		this.genero = genero;
		this.sinpsis = sinpsis;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getSinopsis() {
		return sinpsis;
	}
	public void setSinopsis(String sinpsis) {
		this.sinpsis = sinpsis;
	}
	
}
