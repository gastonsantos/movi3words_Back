package com.api.movi3words.movie3words.cotroller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.api.movi3words.movie3words.model.PeliculaModel;
import com.api.movi3words.movie3words.model.RequestCreateRoom;
import com.api.movi3words.movie3words.service.IPeliculaService;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

@Controller
public class WebSocketController {
	
    
    @Autowired
    private IPeliculaService _peliculasService;
    
    
	 public WebSocketController(IPeliculaService _peliculaService) {
		 this._peliculasService = _peliculaService;
		
	}
	@MessageMapping("/chat/{roomId}")
	 @SendTo("/topic/{roomId}")
	 public dtoMensaje chat(@DestinationVariable String roomId, dtoMensaje mensaje) {
		 
		 return new dtoMensaje(mensaje.getMensaje(), mensaje.getUsuario());
	 }

	 @MessageMapping("/createRoom/{dificultad}")
	 @SendTo("/topic/roomCreated/{dificultad}")
	    public RequestCreateRoom createRoom(@DestinationVariable int dificultad) {
		 		System.out.println("El nivel de dificultad es: "+dificultad);
	        return _peliculasService.crearSala(dificultad);
	    }

	    @MessageMapping("/joinRoom")
	    @SendTo("/topic/guessResult")
	    public String joinRoom(dtoMensaje mensaje) {
	    	    System.out.println("Sala: " + mensaje.getUsuario());
	    	    System.out.println("Intento de adivinanza: " + mensaje.getMensaje());

	    	    return _peliculasService.adivinarPelicula(mensaje);

	    }
}

