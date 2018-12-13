package com.tareas.core.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.xml.ws.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tareas.core.entity.Tareas;
import com.tareas.core.model.MTareas;
import com.tareas.core.service.TareaService;

@RestController
@RequestMapping("/v1")
public class TareasController {
	@Autowired
	@Qualifier("servicio")
	TareaService servicio;
	
	//POST
	@RequestMapping(value = "/tareas", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> AgregarTarea(@RequestBody @Valid Tareas tarea) {
		
		servicio.Crear(tarea);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	// PUT
	@RequestMapping(value = "/tareas", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ActualizarTarea(@RequestBody @Valid Tareas tarea) {
		
		if(servicio.Actualizar(tarea)) {			
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		else {
			
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}	
	}
	
	// BORRA UNA TAREA ESPECÍFICA
	@RequestMapping(value = "/tareas/{idTarea}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> BorrarTarea(@PathVariable("idTarea") int idTarea) {
		
		if(servicio.Borrar(idTarea)) {
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		
		
		
		return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}
	
	// OBTIENE UNA TAREA SEGÚN SU ID
	@RequestMapping(value = "/tareas/{idTarea}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ObtenerPorId(@PathVariable("idTarea") int idTarea) {	
		
		MTareas tareaObtenida = servicio.ObtenerPorId(idTarea);
		
		if(tareaObtenida != null) {
			
			Map<String, Object> respuesta = new HashMap<String, Object>();
			
			respuesta.put("data", tareaObtenida);
						
			return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
		}
		else {
				
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
	}
	
	//OBTIENE LAS TAREAS POR PÁGINA Y POR CANTIDAD DE REGISTROS
	@RequestMapping(value = "/tareas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTareas(Pageable pageable){
		
		List<MTareas> listaObt = servicio.obtenerPorPaginacion(pageable);
		
		if(listaObt.isEmpty()) {
			
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		
		Map<String, Object> responseObj = new HashMap<String, Object>();
		
		responseObj.put("data", listaObt);
		
		return new ResponseEntity<Object>(responseObj, HttpStatus.OK);
		
	}
	
	
}
