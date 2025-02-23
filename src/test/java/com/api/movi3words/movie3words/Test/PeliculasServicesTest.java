package com.api.movi3words.movie3words.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.api.movi3words.movie3words.model.PeliculaModel;
import com.api.movi3words.movie3words.model.dtoAdivinarPelicula;
import com.api.movi3words.movie3words.service.IPeliculaService;
import com.api.movi3words.movie3words.service.peliculasService;

@ExtendWith(MockitoExtension.class)
class PeliculasServicesTest {
	private Map<String, PeliculaModel> rooms1 = new HashMap<>();
	
	@Mock // Simula la interfaz
	private IPeliculaService _peliculaService;
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private peliculasService peliculaService;

	  @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this); //Inicializa los Mock
	    }

	

	    @Test
	    public void testQueAdivinaPelicula() {
		PeliculaModel pelicula = new PeliculaModel();
		pelicula.setId(1);
		pelicula.setNombre("IT");
		rooms1.put("1", pelicula);
		
		dtoAdivinarPelicula dtoAdivinarPelicula = new dtoAdivinarPelicula(1,"IT");
		
		//Lo que hacemos aca es simular que el servicio devuelve el String "Correcto", como si hubiera aceratado
		when(_peliculaService.adivinarPelicula(dtoAdivinarPelicula)).thenReturn("Correcto");
		//Estamos llamando el Metodo que estamos testeando
		 String valorObtenido = _peliculaService.adivinarPelicula(dtoAdivinarPelicula);
		 
		assertEquals("Correcto", valorObtenido);
		
	    //Verificamos que el mocK se llamo solo una vez
		verify(_peliculaService, times(1)).adivinarPelicula(dtoAdivinarPelicula);
		
	    }

	    @Test
	    public void testQueNoAdivinaPelicula() {
	    	PeliculaModel pelicula = new PeliculaModel();
			pelicula.setId(1);
			pelicula.setNombre("IT");
			rooms1.put("1", pelicula);
			
			dtoAdivinarPelicula dtoAdivinarPelicula = new dtoAdivinarPelicula(1,"IT");
			
			//Lo que hacemos aca es simular que el servicio devuelve el String "Correcto", como si hubiera aceratado
			when(_peliculaService.adivinarPelicula(dtoAdivinarPelicula)).thenReturn("Incorrecto");
			//Estamos llamando el Metodo que estamos testeando
			 String valorObtenido = _peliculaService.adivinarPelicula(dtoAdivinarPelicula);
			 
			assertEquals("Incorrecto", valorObtenido);
			
		    //Verificamos que el mocK se llamo solo una vez
			verify(_peliculaService, times(1)).adivinarPelicula(dtoAdivinarPelicula);
	    }
	    
	    @Test
	    public void testQueObtieneLasImagenesDeLasPeliculas() {
	    	
	    	 int idPelicula = 123;
	    	 String respuestaJson = """
	    			 {
	    			     "backdrops": [
	    			         {"file_path": "/TXSxV23MWYkezZ3219gtgcSX6n.jpg"},
	    			         {"file_path": "/x9McE1WFKnAHludiY7xfd7modDC.jpg"},
	    			         {"file_path": "/rBe5PsSJsghKuM2bvRzjXFvKH3S.jpg"},
	    			         {"file_path": "/jOuCWdh0BE6XPu2Vpjl08wDAeFz.jpg"},
	    			         {"file_path": "/5fz2r6YlO8noWH48slH1GJncs8T.jpg"}
	    			     ]
	    			 }
	    			 """;

	    	 when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(respuestaJson);
	    	 
	        
	    	 String[] recibido = peliculaService.obtenerImagenesPelicula(idPelicula);
	    	
	    
	    	 for (String string : recibido) {
				System.out.println("La imagen del test son: "+string);
			}
	    	 String[] esperado = {
	    			    "https://image.tmdb.org/t/p/w1280/TXSxV23MWYkezZ3219gtgcSX6n.jpg",
	    			    "https://image.tmdb.org/t/p/w1280/x9McE1WFKnAHludiY7xfd7modDC.jpg",
	    			    "https://image.tmdb.org/t/p/w1280/rBe5PsSJsghKuM2bvRzjXFvKH3S.jpg",
	    			    "https://image.tmdb.org/t/p/w1280/jOuCWdh0BE6XPu2Vpjl08wDAeFz.jpg",
	    			    "https://image.tmdb.org/t/p/w1280/5fz2r6YlO8noWH48slH1GJncs8T.jpg"
	    			};
  	
	    	 
	    	  assertArrayEquals(recibido, esperado);

	    	 
	    	 
	    }
	    
}
