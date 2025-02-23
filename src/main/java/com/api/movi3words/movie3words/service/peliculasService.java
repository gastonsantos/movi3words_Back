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

import com.api.movi3words.movie3words.cotroller.dtoMensaje;
import com.api.movi3words.movie3words.model.PeliculaModel;
import com.api.movi3words.movie3words.model.RequestCohere;
import com.api.movi3words.movie3words.model.RequestCreateRoom;
import com.api.movi3words.movie3words.model.dtoAdivinarPelicula;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class peliculasService implements IPeliculaService {
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
	        
	        String urlTotalPaginas = String.format("%s?api_key=%s&language=%s&with_companies=%d&sort_by=popularity.desc,vote_average.desc&with_runtime.gte=40",
	                                              BASE_URL, API_KEY, LANGUAGE, compania);

	      
	        //Trae todas las peliculas de esa compania 
	        String responseTotalPaginas = restTemplate.getForObject(urlTotalPaginas, String.class);
	        JSONObject jsonResponseTotalPaginas = new JSONObject(responseTotalPaginas);
	        int totalPaginas = jsonResponseTotalPaginas.getInt("total_pages");

	       //Elige una pagina aleatoria
	        Random random = new Random();
	        int paginaAleatoria = random.nextInt(totalPaginas) + 1;

	        // trae una pagina en especifica de paginaAlearoria
	        String url = String.format("%s?api_key=%s&language=%s&with_companies=%d&sort_by=popularity.desc,vote_average.desc&page=%d&with_runtime.gte=40",
	                                  BASE_URL, API_KEY, LANGUAGE, compania, paginaAleatoria);

	       // aca si trae la pelicula
	        String response = restTemplate.getForObject(url, String.class);
	        JSONObject jsonResponse = new JSONObject(response);
	        JSONArray movies = jsonResponse.getJSONArray("results");

	        if (movies.length() == 0) {
	            continue; 
	        }

	       // Elige una pelicula de esa pagina
	        JSONObject randomMovie = movies.getJSONObject(random.nextInt(movies.length()));

	      
	        String nombre = randomMovie.getString("title");
	        String sinopsis = randomMovie.optString("overview", ""); // Si no hay sinopsis, se asigna una cadena vacía
	        String imagen = "https://image.tmdb.org/t/p/w500" + randomMovie.optString("poster_path", "");

	        int idPelicula = randomMovie.getInt("id");
	        String idPeliculaString = String.valueOf(idPelicula);

	        
	        String genero = "";
	        if (randomMovie.has("genre_ids") && randomMovie.getJSONArray("genre_ids").length() > 0) {
	            genero = obtenerNombreGenero(randomMovie.getJSONArray("genre_ids").getInt(0));
	        }

	     
	        RequestCohere requestCohere = new RequestCohere(nombre, genero, sinopsis);
	        String palabras = obtenerTresPalabras(requestCohere);

	        
	        if (!sinopsis.isEmpty() && !imagen.isEmpty()) {
	            System.out.println("Película encontrada: " + idPeliculaString + " " + nombre + " " + sinopsis + " " + palabras);
	            pelicula2.setId(idPelicula);
	            pelicula2.setGenero(genero);
	            pelicula2.setNombre(nombre);
	            pelicula2.setSinopsis(sinopsis);
	            pelicula2.setPalabras(palabras);
	            pelicula2.setImagen(imagen);
	            pelicula2.setImagenes(obtenerImagenesPelicula(pelicula2.getId()));
	            peliculaValida = true; 
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
	        
	        
	        System.out.println("Respuesta JSON: " + response);

	        
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
	        return new String[0];
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
    	    String roomId = String.valueOf(random.nextInt(1000));
	        PeliculaModel pelicula = obtenerPelicula(dificultad);
	        
	        rooms1.put(roomId, pelicula);
	        RequestCreateRoom dto = new RequestCreateRoom();
	        dto.setSala(roomId);
	        dto.setPelicula(pelicula);
	        return dto;        	
    }
    @Override
    public PeliculaModel cambiarPelicula(String idRoom, int dificultad) {
    	 System.out.println("Entra a Cambiar Pelicual");
    	
    	 PeliculaModel pelicula = obtenerPelicula(dificultad);
   
  
    	 rooms1.put(idRoom, pelicula);
    	 PeliculaModel pelicula2 = rooms1.get(idRoom);
    	 System.out.println("La Sala es: "+idRoom);
    	 System.out.println("La nueva pelicula es: " + pelicula2.getNombre());
    	 return pelicula;
    	  
    }
    
    @Override
    public String adivinarPelicula(dtoAdivinarPelicula mensaje) {
    	 String sala = Integer.toString(mensaje.getSala()); 
 	     String intentoUsuario = mensaje.getPelicula(); 
 	     System.out.println("La sala es: " + sala);
 	     System.out.println("La pelicula es: " +intentoUsuario);
 	     
 	   /*
 	    if (!rooms1.containsKey(sala)) {
 	        return "La sala no existe.";
 	    }
*/
 	    PeliculaModel peliculaModel = rooms1.get(sala); 
 	    System.out.println("SALA: "+sala);
 	    System.out.println("Intento usuario: "+intentoUsuario);
 	    System.out.println("Pelicula: "+peliculaModel.getNombre());
 	   
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
    private static String normalizarCadena(String pelicula) {
        // Eliminar tildes y diacríticos
        String sinTildes = Normalizer.normalize(pelicula, Normalizer.Form.NFD)
                                    .replaceAll("\\p{M}", "");

        // Convertir a minúsculas
        return sinTildes.toLowerCase();
    }
      
    private Integer obtenerIdDegeneroPelicula() {
    	
    	int [] companias =  {2, 3, 6194,33,4};
    	Random random = new Random();
        int indiceAleatorio = random.nextInt(companias.length); 
        int companiaAleatoria = companias[indiceAleatorio]; 
        System.out.println("Compania elegida"+ companiaAleatoria);
    	return companiaAleatoria;
    }
    
    private Integer obtenerIdDegeneroPeliculaInfantil() {
    	
    	int [] companias =  {2,3};
    	Random random = new Random();
        int indiceAleatorio = random.nextInt(companias.length); 
        int companiaAleatoria = companias[indiceAleatorio]; 
        System.out.println("Compania elegida"+ companiaAleatoria);
    	return companiaAleatoria;
    }
    private Integer obtenerIdDegeneroPeliculasDificil() {
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
    



}