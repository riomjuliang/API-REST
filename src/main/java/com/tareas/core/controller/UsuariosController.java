package com.tareas.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tareas.core.entity.Token;
import com.tareas.core.entity.Usuarios;
import com.tareas.core.externalServices.UsuariosExternos;

@Controller
@RequestMapping("/v1")
public class UsuariosController {
	
	@Autowired
	@Qualifier("servicioUsuarios")
	UsuariosExternos servicio;
	
	// OBTIENE EL TOKEN DE AUTENTICACIÓN PARA LA API DE USUARIOS
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerToken() {	
		try {
			String obtenido = "";
			obtenido = servicio.obtenerToken().toString();
			
			Gson g = new Gson(); 
			Token t = g.fromJson(obtenido, Token.class);
		
			return new ResponseEntity<Object>(t, HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}	
	}
	
	// OBTIENE TODOS LOS USUARIOS DE LA API EXTERNA 
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<Usuarios>> findAll() {
		try {
			List<Usuarios> arr = servicio.findAll();
			
			if(arr.isEmpty()) {
				return new ResponseEntity<List<Usuarios>>(HttpStatus.NO_CONTENT);
			}
		
			return new ResponseEntity<List<Usuarios>>(arr, HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<List<Usuarios>>(HttpStatus.NO_CONTENT);
		}		
	}	
	
	// OBTIENE UN USUARIO POR SU ID EN LA API EXTERNA
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Usuarios> getById(@PathVariable("id") int id) {		
		try {
			Usuarios user = servicio.getById(id);
			
			return user != null 
					? new ResponseEntity<Usuarios>(user, HttpStatus.OK)
					: new ResponseEntity<Usuarios>(HttpStatus.NO_CONTENT);
				
			}
			catch(Exception e) {
				return new ResponseEntity<Usuarios>(HttpStatus.NO_CONTENT);
			}	
	}
	
}
