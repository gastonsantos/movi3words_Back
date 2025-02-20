package com.api.movi3words.movie3words.model;

public class PeliculaModel {
	
private int id;
private String nombre;
private String sinopsis;
private String imagen;
private String genero;
private String palabras;
private String [] imagenes;

public PeliculaModel() {

}
public PeliculaModel(int id, String nombre, String sinopsis, String imagen, String genero, String palabras, String[] imagenes) {
	super();
	this.id = id;
	this.nombre = nombre;
	this.sinopsis = sinopsis;
	this.imagen = imagen;
	this.genero = genero;
	this.palabras = palabras;
	this.imagenes = imagenes;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String[] getImagenes() {
	return imagenes;
}
public void setImagenes(String[] imagenes) {
	this.imagenes = imagenes;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getSinopsis() {
	return sinopsis;
}
public void setSinopsis(String sinopsis) {
	this.sinopsis = sinopsis;
}
public String getImagen() {
	return imagen;
}
public void setImagen(String imagen) {
	this.imagen = imagen;
}
public String getGenero() {
	return genero;
}
public void setGenero(String genero) {
	this.genero = genero;
}
public String getPalabras() {
	return palabras;
}
public void setPalabras(String palabras) {
	this.palabras = palabras;
}



}
