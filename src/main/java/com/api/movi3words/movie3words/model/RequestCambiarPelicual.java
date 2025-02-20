package com.api.movi3words.movie3words.model;

public class RequestCambiarPelicual {
	private String idRoom; // Cambia "idRoom" a "sala"
	private int dificultad;
    // Getter y 

	
    public String getIdRoom() {
        return idRoom;
    }

    public RequestCambiarPelicual() {
		
	}

    public RequestCambiarPelicual(String idRoom, int dificultad) {
		
		this.idRoom = idRoom;
		this.dificultad = dificultad;
	}

	public int getDificultad() {
		return dificultad;
	}

	public void setDificultad(int dificultad) {
		this.dificultad = dificultad;
	}

	public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }
	
}
