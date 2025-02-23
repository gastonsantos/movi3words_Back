package com.api.movi3words.movie3words.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.movi3words.movie3words.model.PeliculaModel;
import com.api.movi3words.movie3words.model.RequestCambiarPelicual;
import com.api.movi3words.movie3words.model.RequestCreateRoom;
import com.api.movi3words.movie3words.model.dtoAdivinarPelicula;
import com.api.movi3words.movie3words.model.dtoDificultad;
import com.api.movi3words.movie3words.service.IPeliculaService;


@RestController
@RequestMapping("api/pelicula")
public class peliculasController {

	@Autowired
	private IPeliculaService _peliculaService;
	
	public peliculasController(IPeliculaService _peliculaService) {
		this._peliculaService = _peliculaService;
	}
	  /*
	 @GetMapping("/obtenerPelicula")
	 @CrossOrigin(origins = "*", allowedHeaders = "*")
	    public PeliculaModel obtenerPelicula() {
	        
		 return   _peliculaService.cambiarPelicula(null);
	    }
	    */
	  @PostMapping(value="/obtenerPelicula")
	  @CrossOrigin(origins = "*", allowedHeaders = "*")
		public PeliculaModel obtenerPelicula(@RequestBody RequestCambiarPelicual IdRoom) {
		  
		  System.out.println("Sala de Controller:  " +IdRoom.getIdRoom());
			return  _peliculaService.cambiarPelicula(IdRoom.getIdRoom(), IdRoom.getDificultad());
		}
	  
	//En veremos
	  
	  @PostMapping(value="/obtenerPalabrasParaPelicula")
	  @CrossOrigin(origins = "*", allowedHeaders = "*")
		public PeliculaModel obtenerPalabrasParaPelicula(@RequestBody RequestCambiarPelicual IdRoom) {
		  
		  System.out.println("Sala de Controller:  " +IdRoom.getIdRoom());
			return  _peliculaService.cambiarPelicula(IdRoom.getIdRoom(), IdRoom.getDificultad());
		}
	  
	  @PostMapping(value="/crearSala")
	  @CrossOrigin(origins = "*", allowedHeaders = "*")
		public RequestCreateRoom crearSala(@RequestBody dtoDificultad dificultad) {
		  
			return  _peliculaService.crearSala(dificultad.getDificultad());
		}
	  
	  @PostMapping(value="/adivinarPelicula")
	  @CrossOrigin(origins = "*", allowedHeaders = "*")
	    public String adivinarPelicula(@RequestBody dtoAdivinarPelicula adivinar) {
	    	    System.out.println("Sala: " + adivinar.getSala());
	    	    System.out.println("Intento de adivinanza: " + adivinar.getPelicula());

	    	    return _peliculaService.adivinarPelicula(adivinar);

	    }
	
}
