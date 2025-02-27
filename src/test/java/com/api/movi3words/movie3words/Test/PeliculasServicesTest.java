package com.api.movi3words.movie3words.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.api.movi3words.movie3words.exception.ImagenesNoEncontradaExcepcion;
import com.api.movi3words.movie3words.exception.SalaNoEncontradaException;
import com.api.movi3words.movie3words.model.PeliculaModel;
import com.api.movi3words.movie3words.model.RequestCohere;
import com.api.movi3words.movie3words.model.RequestCreateRoom;
import com.api.movi3words.movie3words.model.dtoAdivinarPelicula;
import com.api.movi3words.movie3words.service.IPeliculaService;
import com.api.movi3words.movie3words.service.PeliculasService;

@ExtendWith(MockitoExtension.class)
class PeliculasServicesTest {
	private Map<String, PeliculaModel> rooms1 = new HashMap<>();
	
	@Mock 
	private IPeliculaService _peliculaService;
	@Mock
	private RestTemplate restTemplate;
	

	@InjectMocks
	private PeliculasService peliculaService;
	
	 @Mock
	 private Random random;

	  @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this); //Inicializa los Mock
	        rooms1 = new HashMap<>(); // Simulamos la estructura donde se guardan las salas
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
	    @Test
	    public void testQueNoObtieneLasImagenesDeLasPeliculas() {
	    	
	    	int idPelicula = 98989898;

	        // Simula que la API devuelve un 404 Not Found
	        when(restTemplate.getForObject(anyString(), eq(String.class)))
	            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

	        // Verifica que al llamar al método se lanza PeliculaNoEncontradaException
	        Exception exception = assertThrows(ImagenesNoEncontradaExcepcion.class, () -> {
	            peliculaService.obtenerImagenesPelicula(idPelicula);
	        });

	        // Verifica el mensaje de la excepción
	        assertEquals("No se pudo encontrar Imagen de la pelicula." , exception.getMessage());
	    
	    }
	    
	    @Test
	    public void testQueGeneraLaPelicula() {
	    	int dificultad=2;
	    	int idCompania = 3;
	    	int paginaAleatoria = 4;
	    	int randomMovie= 22;
	    	String [] imagenPelicula = new String[2];
	    	imagenPelicula[0]="imagen1";
	    	imagenPelicula[1]="imagen2";
	    	JSONObject randomMovie1 = new JSONObject();
	    	PeliculaModel pelicula2 = new PeliculaModel();
	    	pelicula2.setSinopsis("Sinopsis");
	    	pelicula2.setImagen("Imagen");
	    	pelicula2.setGenero("Genero");
	    	pelicula2.setNombre("Titulo");
	    	RequestCohere requestCohere = new RequestCohere("Nombre","Genero", "Sinopsis");

	    	when(_peliculaService.obtenerPelicula(dificultad)).thenReturn(pelicula2);
	    	PeliculaModel peliculaObtenida = _peliculaService.obtenerPelicula(dificultad);
	    	
	    	assertNotNull(peliculaObtenida);
	    	assertEquals("Titulo",peliculaObtenida.getNombre());
	    }
	    
	    @Test
	    public void testQueNoGeneraLaPelicula() {
	    	int dificultad=2;
	    	int idCompania = 3;
	    	int paginaAleatoria = 4;
	    	int randomMovie= 22;
	    	String [] imagenPelicula = new String[2];
	    	imagenPelicula[0]="imagen1";
	    	imagenPelicula[1]="imagen2";
	    	JSONObject randomMovie1 = new JSONObject();
	    	PeliculaModel pelicula2 = new PeliculaModel();
	    	pelicula2.setSinopsis("Sinopsis");
	    	pelicula2.setImagen("Imagen");
	    	pelicula2.setGenero("Genero");
	    	pelicula2.setNombre("Titulo");
	    	RequestCohere requestCohere = new RequestCohere("Nombre","Genero", "Sinopsis");

	    	when(_peliculaService.obtenerPelicula(dificultad)).thenReturn(null);
	    	PeliculaModel peliculaObtenida = _peliculaService.obtenerPelicula(dificultad);
	    	
	    	assertNull(peliculaObtenida);
	    	
	    }
	    @Test
	    public void testQueCreaUnaSala() {
	    	 
	    	
	    	 Integer dificultad= 1;
	    	 
	    	 PeliculaModel pelicula = new PeliculaModel();
	    	 pelicula.setNombre("Pelicula");
	    	 pelicula.setId(200);
	    	 pelicula.setGenero("Genero");
	    	
	    	 RequestCreateRoom respuesta = new RequestCreateRoom("123", pelicula);
	    	 
	    
	    	 when(_peliculaService.crearSala(dificultad)).thenReturn(respuesta);
	    	 
	    	 RequestCreateRoom request11 = _peliculaService.crearSala(dificultad);
	    	 rooms1.put("123", pelicula);
	    	 assertEquals("123", request11.getSala());
	    	 assertEquals("Pelicula", request11.getPelicula().getNombre());
	    	 assertTrue(rooms1.containsKey("123"));
	    	 assertTrue(rooms1.containsValue(pelicula));
	    	 
	    }
	    @Test
	    public void testQueCambiaDePelicula() {
	    	//Preparo los datos de entrada
	    	
	    	 Integer dificultad= 1;
	    	 String idRoom = "123";
	    	 
	    	 
	    	//Preparo los datos de la pelicula que estan en la sala
	    	 PeliculaModel peliculaEnSala = new PeliculaModel();
	    	 peliculaEnSala.setNombre("Pelicula");
	    	 peliculaEnSala.setId(200);
	    	 peliculaEnSala.setGenero("Genero");
	    	 
	    
	    	 PeliculaModel peliculaQuecambia = new PeliculaModel();
	    	 peliculaQuecambia.setNombre("Pelicula1");
	    	 peliculaQuecambia.setId(201);
	    	 peliculaQuecambia.setGenero("Genero");
	    	 
	    	 peliculaService.agregarSala(idRoom, peliculaEnSala);
	    	 
             when(_peliculaService.cambiarPelicula(idRoom,dificultad)).thenReturn(peliculaQuecambia);
             
             
             PeliculaModel pelicula1 = _peliculaService.cambiarPelicula(idRoom,dificultad);
             
             assertEquals(peliculaQuecambia.getNombre(), pelicula1.getNombre());
             assertNotEquals(peliculaEnSala.getNombre(), pelicula1.getNombre() );
             
	    }
	    @Test
	    public void testQueNoCambiaDePeliculaPorQueNoencuentraSala() {
	    	//Preparo los datos de entrada
	    	 Integer dificultad= 1;
	    	 String idRoom = "123";
	    	 String idRoomNoEncontrado="122";
	    	//Preparo los datos de la pelicula que estan en la sala
	    	 PeliculaModel peliculaEnSala = new PeliculaModel();
	    	 peliculaEnSala.setNombre("Pelicula");
	    	 peliculaEnSala.setId(200);
	    	 peliculaEnSala.setGenero("Genero");
	    	 rooms1.put(idRoom, peliculaEnSala); 
	    	 //Preparo la Pelicula Nueva
	    	 PeliculaModel peliculaNueva = new PeliculaModel();
	    	 peliculaNueva.setNombre("PeliculaNueva");
	    	 peliculaNueva.setId(201);
	    	 peliculaNueva.setGenero("Genero");
	    	 

	    	 assertFalse(rooms1.containsKey(idRoomNoEncontrado), "La sala debería no existir antes del test");

	    	 
             Exception exception = assertThrows(SalaNoEncontradaException.class, () -> {
            	 peliculaService.cambiarPelicula(idRoomNoEncontrado,dificultad);
 	        });

 	        // Verifica el mensaje de la excepción
 	        assertEquals("No se pudo encontrar la Sala." , exception.getMessage());
             
	    }
	    
	    
	    
}
