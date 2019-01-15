package com.tareas.core.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.tareas.core.entity.Mensaje;
import com.tareas.core.entity.Tareas;
import com.tareas.core.entity.Usuarios;
import com.tareas.core.externalServices.UsuariosExternosImpl;
import com.tareas.core.service.TareaService;

@RestController
@RequestMapping("/v1")
@EnableCaching
public class TareasController{
	
	private static final Log logger = LogFactory.getLog(TareaService.class);
	
	@Autowired
	@Qualifier("servicio")
	TareaService servicio;
	
	@Autowired
	@Qualifier("servicioUsuarios")
	UsuariosExternosImpl servicioUsuarios;
	
	//POST (CREAR TAREA)
	@RequestMapping(value = "/tareas", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> AgregarTarea(@RequestBody @Valid Tareas tarea, @RequestHeader(value="X-Caller-Id") int idUsuario) {
		Usuarios usuarioObtenido = new Usuarios();
		usuarioObtenido = servicioUsuarios.getById(idUsuario);
		if(usuarioObtenido != null) {
			String message = "";
			message = servicio.Crear(tarea, idUsuario);
			if(message.equals("")) {				
				return new ResponseEntity<Object>(HttpStatus.OK);
			}
			else {
				Mensaje mensajeNuevo = new Mensaje("ERROR AL GUARDAR", message);
				
				Map<String, Object> respuesta = new HashMap<String, Object>();
				respuesta.put("errors", mensajeNuevo);
				
				return new ResponseEntity<Object>(respuesta, HttpStatus.BAD_REQUEST);
			}
		}
		else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	// PUT (ACTUALIZAR TAREA)
	@RequestMapping(value = "/tareas", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ActualizarTarea(@RequestBody @Valid Tareas tarea, @RequestHeader(value="X-Caller-Id") int idUsuario) {
		Usuarios usuarioObtenido = new Usuarios();
		usuarioObtenido = servicioUsuarios.getById(idUsuario);
		if(usuarioObtenido != null) {
			if(servicio.Actualizar(tarea)) {			
				return new ResponseEntity<Object>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		}
		else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	// BORRA UNA TAREA ESPECÍFICA
	@RequestMapping(value = "/tareas/{idTarea}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> BorrarTarea(@PathVariable("idTarea") int idTarea, @RequestHeader(value="X-Caller-Id") int idUsuario) {
		Usuarios usuarioObtenido = new Usuarios();
		usuarioObtenido = servicioUsuarios.getById(idUsuario);
		if(usuarioObtenido != null) {
			if(servicio.Borrar(idTarea)) {
				return new ResponseEntity<Object>(HttpStatus.OK);
			}
			else{
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		}
		else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	// OBTIENE UNA TAREA SEGÚN SU ID
	@RequestMapping(value = "/tareas/{idTarea}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ObtenerPorId(@PathVariable("idTarea") int idTarea, @RequestHeader(value="X-Caller-Id") int idUsuario) {	
		Usuarios usuarioObtenido = new Usuarios();
		usuarioObtenido = servicioUsuarios.getById(idUsuario);	
		if(usuarioObtenido != null) {
			Tareas tareaObtenida = servicio.ObtenerPorId(idTarea);		
			if(tareaObtenida != null) {
				Map<String, Object> respuesta = new HashMap<String, Object>();
				respuesta.put("data", tareaObtenida);
							
				return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		}
		else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}	
	}
	
// OBTIENE UNA TAREA CON LOS USUARIOS QUE TIENEN ACCESO A ELLA SEGÚN SU ID
	@RequestMapping(value = "/tareas/{idTarea}/usuarios", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ObtenerUsuariosPorTarea(@PathVariable("idTarea") int idTarea, @RequestHeader(value="X-Caller-Id") int idUsuario) {	
		Usuarios usuarioObtenido = new Usuarios();
		usuarioObtenido = servicioUsuarios.getById(idUsuario);			
		if(usuarioObtenido != null) {
			Tareas tareaObtenida = servicio.ObtenerPorId(idTarea);
			if(tareaObtenida != null) {
				Map<String, Object> respuesta = new HashMap<String, Object>();
				
				List<Usuarios> listadoUsersAPI = null;
				listadoUsersAPI = servicioUsuarios.findAll();
				
				List<Usuarios> listadoUsersTarea = new ArrayList<Usuarios>();
				
				if(!listadoUsersAPI.isEmpty()) {
					if(!tareaObtenida.getUsuarios().isEmpty()) {
						for(Usuarios user : listadoUsersAPI) {
							boolean encontrado = false;
							
							for(Integer i : tareaObtenida.getUsuarios()) {					
								if(i == user.getId()) {
									encontrado = true;
								}
							}
						
							if(encontrado == true) {
								listadoUsersTarea.add(user);
							}
						}
					}
				}
				
				respuesta.put("data", listadoUsersTarea);
							
				return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
			}
			else {					
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		}
		else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}	
	}
	
	//OBTIENE LAS TAREAS POR PÁGINA Y POR CANTIDAD DE REGISTROS
	@RequestMapping(value = "/tareas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTareas(Pageable pageable, @RequestHeader(value="X-Caller-Id") int idUsuario){
		Usuarios usuarioObtenido = new Usuarios();		
		usuarioObtenido = servicioUsuarios.getById(idUsuario);
		if(usuarioObtenido != null) {
			List<Tareas> listaObt = servicio.obtenerPorPaginacion(pageable);			
			if(listaObt.isEmpty()) {
				
				return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
			}
			
			Map<String, Object> responseObj = new HashMap<String, Object>();
			responseObj.put("data", listaObt);
			
			return new ResponseEntity<Object>(responseObj, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}			
	}
	
	// COMPARTIR TAREA CON OTROS USUARIOS
	@RequestMapping(value = "/tareas/{idTarea}/usuarios", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> CompartirTarea(@RequestBody @Valid ArrayList<Integer> listaUsuarios, @PathVariable("idTarea") int idTarea, @RequestHeader(value="X-Caller-Id") int idUsuario) {	
		Usuarios usuarioObtenido = new Usuarios();
		usuarioObtenido = servicioUsuarios.getById(idUsuario);			
		if(usuarioObtenido != null) {
			try {
				String message = "";
				message = servicio.compartirTarea(idTarea, listaUsuarios);
				if(message.equals("")) {
					return new ResponseEntity<Object>(HttpStatus.OK);
				}
				else {
					Map<String, Object> respuesta = new HashMap<String, Object>();
					Mensaje mensajeNuevo = new Mensaje("ERROR AL COMPARTIR TAREA", message);
					
					respuesta.put("data", mensajeNuevo);
					
					return new ResponseEntity<Object>(respuesta, HttpStatus.BAD_REQUEST);
				}
			}
			catch(Exception e) {
				logger.info(e.toString());
				
				return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
			}
		}
		else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}	
	}
}
