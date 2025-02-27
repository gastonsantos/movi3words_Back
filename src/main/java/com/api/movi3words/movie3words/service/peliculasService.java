package com.api.movi3words.movie3words.service;

import java.util.HashMap;
import java.util.List;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.movi3words.movie3words.exception.ImagenesNoEncontradaExcepcion;
import com.api.movi3words.movie3words.exception.PeliculaNoEncontradaException;
import com.api.movi3words.movie3words.exception.SalaNoEncontradaException;
import com.api.movi3words.movie3words.model.PeliculaModel;
import com.api.movi3words.movie3words.model.RequestCohere;
import com.api.movi3words.movie3words.model.RequestCreateRoom;
import com.api.movi3words.movie3words.model.dtoAdivinarPelicula;
import com.api.movi3words.movie3words.model.dtoMensaje;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PeliculasService implements IPeliculaService {
     private static final String API_KEY = "34d7151285da1deeb4fb5ab7d8c0d1b3";
     private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
     private static final String LANGUAGE = "es-MX";
     private final Random random = new Random(); 
	 private Map<String, PeliculaModel> rooms1 = new HashMap<>();
	 private  String apiUrl="https://api.cohere.ai/generate";
	 private  String apiKey ="hlKdCGADTFU6Mc8zEwqdojIK6pet1cN16S3Fo0l0";
	 private String promptPalabras1 = "A partir de la siguiente sinopsis, título y género de una película, genera exactamente 3 palabras clave separadas por comas. La respuesta debe contener únicamente las palabras, sin explicaciones ni comentarios adicionales.";
	 private String promptPalabras ="Dado el título de una película, genera exactamente tres palabras clave separadas por comas. La respuesta debe contener solo las palabras clave, sin explicaciones ni comentarios adicionales, no uses generos de peliculas. Título de la película:";
	 	
 

	@Autowired
	 private RestTemplate restTemplate;
	

	@Override
	public PeliculaModel obtenerPelicula(int dificultad) {
	    PeliculaModel pelicula2 = new PeliculaModel();
	    boolean peliculaValida = false;

	  
	    do {
	    	Integer idCompania = devuelveCompania(dificultad) ;
	        Integer paginaAleatoria = devuelvePaginaAleatoria(idCompania);

	        JSONObject randomMovie = devuelvePeliculaEnJson(idCompania, paginaAleatoria);
	     
	        pelicula2 = devuelveLaPeliculaEnPeliculaModel(randomMovie); 
	        RequestCohere requestCohere = new RequestCohere(pelicula2.getNombre(), pelicula2.getGenero(), pelicula2.getSinopsis());
	        String palabras = obtenerTresPalabras(requestCohere);
	        
	        
	        if (!pelicula2.getSinopsis().isEmpty() && !pelicula2.getImagen().isEmpty()) {
	            pelicula2.setPalabras(palabras);
	            pelicula2.setImagenes(obtenerImagenesPelicula(pelicula2.getId()));
	            peliculaValida = true; 
	            System.out.println("La Movie es:"+pelicula2.getId() +" "+pelicula2.getNombre()+" "+pelicula2.getSinopsis());
	        } else {
	            System.out.println("Película descartada: Sinopsis o imagen no válidas.");
	        }

	    } while (!peliculaValida); 

	    return pelicula2;
	}
	public String[] obtenerImagenesPelicula(int idPelicula) {
	    
	    String url = String.format("https://api.themoviedb.org/3/movie/%d/images?api_key=%s",
	                               idPelicula, API_KEY);

	    try {
	      
	  
	        String response = restTemplate.getForObject(url, String.class);
	        
	        
	        JSONObject jsonResponse = new JSONObject(response);

	        
	        if (!jsonResponse.has("backdrops") || jsonResponse.getJSONArray("backdrops").length() == 0) {
	            System.out.println("No se encontraron imágenes para la película con ID: " + idPelicula);
	            return new String[0]; 
	        }

	       
	        JSONArray postersArray = jsonResponse.getJSONArray("backdrops");
	        List<String> imagenes = new ArrayList<>();

	        
	        int maxImages = Math.min(postersArray.length(), 5);
	        
	        for (int i = 0; i < maxImages; i++) {
	            JSONObject imageObject = postersArray.getJSONObject(i);
	            String imageUrl = "https://image.tmdb.org/t/p/w1280" + imageObject.optString("file_path", "");
	            if (!imageUrl.equals("https://image.tmdb.org/t/p/w1280")) { // Evita imágenes vacías
	                imagenes.add(imageUrl);
	            }
	        }

	        System.out.println("Imágenes encontradas: " + imagenes);
	        return imagenes.toArray(new String[0]);

	    } catch (Exception e) {
	        System.out.println("Error obteniendo imágenes: " + e.getMessage());
	       // return new String[0];
	        throw new ImagenesNoEncontradaExcepcion();
	    }
	}


    private String obtenerNombreGenero(int genreId) {
        switch (genreId) {
            case 16: return "Animación";
            case 12: return "Aventura";
            case 35: return "Comedia";
            case 10751: return "Familiar";
            case 14: return "Fantasía";
            default: return "Otro";
        }
    }
    
    @Override
    public RequestCreateRoom crearSala(int dificultad) {
    	   
	        String roomId;
	        do {
	            roomId = String.valueOf(random.nextInt(1000)); 
	        } while (rooms1.containsKey(roomId)); 
	        RequestCreateRoom dto = new RequestCreateRoom();
	       
	        PeliculaModel pelicula = obtenerPelicula(dificultad);
	        rooms1.put(roomId, pelicula);
		    dto.setSala(roomId);
		    dto.setPelicula(pelicula);        
	        
	        return dto;        	
    }
    
    @Override
    public PeliculaModel cambiarPelicula(String idRoom, int dificultad) {
    	PeliculaModel peliculaQueRetorna = null ;
    	
    	if(buscarSala(idRoom)) {
    		Boolean mismaPeli= true;
    	
    		PeliculaModel peliculaDeLaSala = rooms1.get(idRoom);
    		do {   
    			   PeliculaModel pelicula = obtenerPelicula(dificultad);
    			   if(!peliculaDeLaSala.getNombre().equals(pelicula.getNombre()))
    			   {
    				   mismaPeli = false;
    				   agregarSala(idRoom,pelicula);
    				   peliculaQueRetorna = pelicula;
    				  break;
    			     }
    			   
    		}while(mismaPeli == true);
    		return peliculaQueRetorna;
    	}
    	return null;	  
    }
    
    	
    public void agregarSala(String idRoom, PeliculaModel pelicula) {
        rooms1.put(idRoom, pelicula);
    }
    
    public Boolean buscarSala(String idRoom) {
    	if(!rooms1.containsKey(idRoom)) {
    		throw new SalaNoEncontradaException();
    	}
    	return true;
    }

    
    @Override
    public String adivinarPelicula(dtoAdivinarPelicula mensaje) {
    	 String sala = Integer.toString(mensaje.getSala()); 
 	     String intentoUsuario = mensaje.getPelicula(); 	 
 	     
 	   
 	    if (!rooms1.containsKey(sala)) {
 	    	throw new SalaNoEncontradaException();
 	    }
       
 	    PeliculaModel peliculaModel = rooms1.get(sala); 
 	    if(peliculaModel.equals(null) ) {
 	    	throw new PeliculaNoEncontradaException();
 	    }
 	   
 	    String peliculaNormalizada = normalizarCadena(peliculaModel.getNombre());
 	    //lo que le saco aca es todo lo q esta despues de los :
 	    String soloNombrePelicula = peliculaNormalizada.split(":")[0].trim();
 	   
 	    String peliculaCorrecta = peliculaModel.getNombre();
 	    
 	    if (intentoUsuario != null && intentoUsuario.equalsIgnoreCase(soloNombrePelicula)) {
 	        return "Correcto";
 	    } else {
 	        return "Incorrecto";
 	    }

    }
    protected static String normalizarCadena(String pelicula) {
        // Eliminar tildes y diacríticos
        String sinTildes = Normalizer.normalize(pelicula, Normalizer.Form.NFD)
                                    .replaceAll("\\p{M}", "");

        // Convertir a minúsculas
        return sinTildes.toLowerCase();
    }
      
    protected Integer obtenerIdDegeneroPelicula() {
    	
    	int [] companias =  {2, 3, 6194,33,4};
    	Random random = new Random();
        int indiceAleatorio = random.nextInt(companias.length); 
        int companiaAleatoria = companias[indiceAleatorio]; 
        System.out.println("Compania elegida"+ companiaAleatoria);
    	return companiaAleatoria;
    }
    
    protected Integer obtenerIdDegeneroPeliculaInfantil() {
    	
    	int [] companias =  {2,3};
    	Random random = new Random();
        int indiceAleatorio = random.nextInt(companias.length); 
        int companiaAleatoria = companias[indiceAleatorio]; 
        System.out.println("Compania elegida"+ companiaAleatoria);
    	return companiaAleatoria;
    }
    protected Integer obtenerIdDegeneroPeliculasDificil() {
    	int[] companias = {2, 3, 6194, 33, 4, 25, 5, 420, 521, 1632};
    
    	Random random = new Random();
        int indiceAleatorio = random.nextInt(companias.length); 
        int companiaAleatoria = companias[indiceAleatorio]; 
        System.out.println("Compania elegida"+ companiaAleatoria);
    	return companiaAleatoria;
    }
    
    
    @Override
	 public String obtenerTresPalabras(RequestCohere request) {

	       
		 	String text = null;
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Authorization", "Bearer " + apiKey);

	        
	        Map<String, Object> body = new HashMap<>();
	        //body.put("prompt",  promptPalabras + " el Titulo es: "+request.getTitulo()+"el Género es: "+request.getGenero()+"la sinopsis: "+request.getSinopsis());
	        body.put("prompt",  promptPalabras+request.getTitulo());
	        body.put("model", "command-r-plus");  
	        body.put("max_tokens", 20); 
	        body.put("temperature", 0.5);

	        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

	       
	        ResponseEntity<String> response = restTemplate.exchange(
	        		apiUrl,
	                HttpMethod.POST,
	                entity,
	                String.class
	        );
	        
	        
	        ObjectMapper objectMapper = new ObjectMapper();

	    
	        	 try {
					
				        JsonNode root = objectMapper.readTree(response.getBody());
				        text = root.get("text").asText();
				} catch (JsonProcessingException e) {
					
					
					e.printStackTrace();
				}
	        	
	        	 String respuesta = text;
	        
	        return respuesta;
	    }
    
    
    protected int devuelveCompania(Integer dificultad) {
    	int compania=0;
        switch(dificultad) {
	    case 1: 
	    	 compania = obtenerIdDegeneroPeliculaInfantil();
	    	break;
	    case 2:
	    	compania = obtenerIdDegeneroPelicula();
	    	break;
	    case 3: 
	    	compania = obtenerIdDegeneroPeliculasDificil();
	    	break;
	    default:
	    	compania= obtenerIdDegeneroPeliculaInfantil();
	    	System.out.println("No entro ningun numero valido");
	    	break;
	    }
        return compania;
    }
    protected int devuelvePaginaAleatoria(Integer compania) {
    
        String urlTotalPaginas = String.format("%s?api_key=%s&language=%s&with_companies=%d&sort_by=popularity.desc,vote_average.desc&with_runtime.gte=40",
                                              BASE_URL, API_KEY, LANGUAGE, compania);

      
        //Trae todas las peliculas de esa compania 
        String responseTotalPaginas = restTemplate.getForObject(urlTotalPaginas, String.class);
        JSONObject jsonResponseTotalPaginas = new JSONObject(responseTotalPaginas);
        int totalPaginas = jsonResponseTotalPaginas.getInt("total_pages");

       //Elige una pagina aleatoria
        Random random = new Random();
         int paginaAleatoria = random.nextInt(totalPaginas) + 1;
         
         return paginaAleatoria;
    }

    
    protected JSONObject devuelvePeliculaEnJson(Integer compania, Integer paginaAleatoria) {
    	boolean encontro= false;
    	JSONObject pelicula = new JSONObject();
    	do {
    		 String url = String.format("%s?api_key=%s&language=%s&with_companies=%d&sort_by=popularity.desc,vote_average.desc&page=%d&with_runtime.gte=40",
                     BASE_URL, API_KEY, LANGUAGE, compania, paginaAleatoria);
    		 
    		 	String response = restTemplate.getForObject(url, String.class);
    		 	JSONObject jsonResponse = new JSONObject(response);
    		 	JSONArray movies = jsonResponse.getJSONArray("results");
    		 	
 
    		 	
    		 	if(movies!= null && movies.length()>0) {
    		 		encontro = true;
    		 		pelicula =  movies.getJSONObject(random.nextInt(movies.length()));
    		 		System.out.println("LA PELICULA ES: "+pelicula);
    		 	}
    		 	
            	
    		} while(!encontro) ;
        	
       
        	return pelicula;
    }
    
    protected PeliculaModel devuelveLaPeliculaEnPeliculaModel(JSONObject randomMovie) {
    	  PeliculaModel pelicula2 = new PeliculaModel();
    	
    	  String nombre = randomMovie.getString("title");
	        String sinopsis = randomMovie.optString("overview", ""); // Si no hay sinopsis, se asigna una cadena vacía
	        String imagen = "https://image.tmdb.org/t/p/w500" + randomMovie.optString("poster_path", "");

	        int idPelicula = randomMovie.getInt("id");
	        String idPeliculaString = String.valueOf(idPelicula);

	        
	        String genero = "";
	        if (randomMovie.has("genre_ids") && randomMovie.getJSONArray("genre_ids").length() > 0) {
	            genero = obtenerNombreGenero(randomMovie.getJSONArray("genre_ids").getInt(0));
	        }
	        pelicula2.setGenero(genero);
	        pelicula2.setId(idPelicula);
	        pelicula2.setImagen(imagen);
	        pelicula2.setNombre(nombre);
	        pelicula2.setSinopsis(sinopsis);
	        
    	return pelicula2;
    }
}
    