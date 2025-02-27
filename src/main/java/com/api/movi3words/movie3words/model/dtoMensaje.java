package com.api.movi3words.movie3words.model;


public class dtoMensaje {

	private String mensaje;
	private String usuario;
	public dtoMensaje() {
		
		
	}
	public dtoMensaje(String mensaje, String usuario) {
		
		this.mensaje = mensaje;
		this.usuario = usuario;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
