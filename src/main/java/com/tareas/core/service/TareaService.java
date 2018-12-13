package com.tareas.core.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tareas.core.converter.Convertidor;
import com.tareas.core.entity.Tareas;
import com.tareas.core.entity.Usuarios;
import com.tareas.core.model.MTareas;
import com.tareas.core.repository.TareasRepositorio;

@Service("servicio")
public class TareaService {
	@Autowired
	@Qualifier("repositorio")
	private TareasRepositorio repositorio;
	
	@Autowired
	@Qualifier("convertidor")
	private Convertidor convertidor;
	
	private static final Log logger = LogFactory.getLog(TareaService.class);
	
	public boolean Crear(Tareas tarea) {
		logger.info("CREANDO TAREA");
		try {
			tarea.setFecha_creacion(new Date());
			
			repositorio.save(tarea);
			
			logger.info("TAREA CREADA SATISFACTORIAMENTE.");
			
			return true;
		}
		catch(Exception e) {
			
			logger.error("ERROR AL CREAR NUEVA TAREA: " + tarea.getNombre().toUpperCase());
			
			return false;
		}
	}
	
	public boolean Actualizar(Tareas tarea) {
		try {
			
			Tareas tareaN = repositorio.findByIdTarea(tarea.getIdTarea());
			
			if(tareaN != null) {
				logger.info("ACTUALIZANDO TAREA: " + tarea.getIdTarea());
				
				repositorio.save(tarea);
				
				logger.info("TAREA " + tarea.getNombre().toUpperCase() + " ACTUALIZADA.");
				
				return true;
			}
			else {
				return false;
			}
			
		}
		catch(Exception e) {
			logger.info("ERROR AL ACTUALIZAR TAREA: " + tarea.getIdTarea());
			
			return false;
		}
	}
	
	public boolean Borrar(int idTarea) {
		try {
			Tareas tareaO = repositorio.findByIdTarea(idTarea);
			repositorio.delete(tareaO);
			
			logger.info("TAREA " + idTarea + " BORRADA.");
			
			return true;
		}
		catch(Exception e) {
			logger.info("ERROR AL ELIMINAR TAREA: " + idTarea);
			return false;
		}
	}
	
	public List<MTareas> Obtener(){
		logger.info("TAREAS OBTENIDAS");
		return convertidor.convertirLista(repositorio.findAll());
	}
	
	public MTareas ObtenerPorId(int idTarea) {
		
		Tareas tareaObt = repositorio.findByIdTarea(idTarea);
		
		if(tareaObt != null) {
			logger.info("TAREA " + idTarea + " OBTENIDA");
			
			return new MTareas(tareaObt);
		}
		else {
			logger.info("TAREA NO ENCONTRADA");
			
			return null;
		}
	}
	
	public List<MTareas> obtenerPorPaginacion(Pageable pageable){
		return convertidor.convertirLista(repositorio.findAll(pageable).getContent());
	}
	
}
