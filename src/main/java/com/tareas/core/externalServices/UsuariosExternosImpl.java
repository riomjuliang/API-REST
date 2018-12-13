package com.tareas.core.externalServices;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tareas.core.entity.Token;
import com.tareas.core.entity.Usuarios;

@Service("servicioUsuarios")
public class UsuariosExternosImpl implements UsuariosExternos{

	private static final Log logger = LogFactory.getLog(UsuariosExternosImpl.class);
	
	String url = "https://gentle-eyrie-95237.herokuapp.com/users";
	
	// OBTIENE TODOS LOS USUARIOS DE LA API EXTERNA
	@Override
	public List<Usuarios> findAll() {
		
		String obtenido = "";
		obtenido = obtenerToken().toString();
		
		if(obtenido.equals("")) {
			return null;
		}
		
		Gson g = new Gson(); 
		Token t = g.fromJson(obtenido, Token.class);
		
		RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("authorization", t.getToken());
	    
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
	    
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	    
	    logger.info(response.getBody());;
	    
	    List<Usuarios> lista = null;
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    try {
			lista = objectMapper.readValue(response.getBody().toString(), new TypeReference<List<Usuarios>>(){});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return lista;
	}

	@Override
	@Cacheable(value = "usersCache", key="#id")
	public Usuarios getById(int id) {
		
		String obtenido = "";
		obtenido = obtenerToken().toString();
		
		if(obtenido.equals("")) {
			return null;
		}
		
		Gson g = new Gson(); 
		Token t = g.fromJson(obtenido, Token.class);
		
		RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("authorization", t.getToken());
	    
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
	    
	    ResponseEntity<String> response = restTemplate.exchange(url + "/" + id, HttpMethod.GET, entity, String.class);
	    
	    try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			Usuarios usuarioObtenido = objectMapper.readValue(response.getBody(), Usuarios.class);
			
		    return usuarioObtenido != null ? usuarioObtenido : null;
	    }
	    catch(Exception e) {
	    	
	    	logger.info(e.getMessage());;
	    	
	    	return null;
	    }
	}

	@Override
	public String obtenerToken() {
		logger.info("OBTENIENDO TOKEN");
		
		String url = "https://gentle-eyrie-95237.herokuapp.com/login";
		
		RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	   
	    JsonObject objetoJson = new JsonObject();
	    
	    objetoJson.addProperty("username", "kinexo");
	    objetoJson.addProperty("password", "kinexo");
	    
	    logger.info("JSON ENVIADO: " + objetoJson.toString());
	    
	    HttpEntity<String> entity = new HttpEntity<String>(objetoJson.toString(), headers);
	    
	    logger.info("HTTP ENTITY: " + entity);
	    
	    ResponseEntity<String> response = restTemplate
	            .exchange(url, HttpMethod.POST, entity, String.class);
	    
	    logger.info("OBJETO OBTENIDO" + response.getBody());
	   
    	return response.getBody();
	}
}
