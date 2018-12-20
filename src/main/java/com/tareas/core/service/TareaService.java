package com.tareas.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tareas.core.entity.Tareas;
import com.tareas.core.entity.Usuarios;
import com.tareas.core.externalServices.UsuariosExternos;
import com.tareas.core.repository.TareasRepositorio;

@Service("servicio")
public class TareaService {
	@Autowired
	@Qualifier("repositorio")
	private TareasRepositorio repositorio;
	
	@Autowired
	@Qualifier("servicioUsuarios")
	UsuariosExternos servicioUsuarios;
	
	private static final Log logger = LogFactory.getLog(TareaService.class);
	
	public String Crear(Tareas tarea, int idUsuario) {
		logger.info("CREANDO TAREA");
		
		try {
			String message = "";	
			ArrayList<Integer> listaUsuariosNoExistentes = new ArrayList<>();		
			List<Usuarios> usuarios = servicioUsuarios.findAll();
		
			HashSet<Integer> hs = new HashSet<Integer>(tarea.getUsuarios());
			List<Integer> listaUsuarios = new ArrayList<>(hs);
			
			// RECORRE LA LISTA DE LOS USUARIOS A COMPARTIR LA TAREA
			
			for(Integer i : listaUsuarios) {
				if(i == idUsuario) {
					message = "No se puede compartir la tarea con el usuario que la crea. ID = " + i + " incorrecto. \n";
				}
				
				if(!usuarios.isEmpty()) {
					boolean encontrado = false;
					
					for(Usuarios user : usuarios) {
						if(i == user.getId()) {
							encontrado = true;
						}
					}
						
					if(encontrado == false) {
						listaUsuariosNoExistentes.add(i);
					}
				}
			}
			
			if(!listaUsuariosNoExistentes.isEmpty()) {
				message += "El/los usuario/s con ID: " + listaUsuariosNoExistentes.toString() + " no existe/n";
			}
			
			if(message.equals("")) {
				tarea.setFecha_creacion(new Date());
				tarea.setIdUsuario(idUsuario);
				repositorio.save(tarea);				
				
				logger.info("TAREA CREADA: " + tarea.toString());			
			}
			
			return message;
		}
		catch(Exception e) {			
			logger.error("ERROR AL CREAR NUEVA TAREA: " + tarea.getNombre().toUpperCase() + ", DETALLE: " + e.toString());
			
			return e.toString();
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
			logger.info("ERROR AL ACTUALIZAR TAREA: " + tarea.getIdTarea() + "\n"
					+ "DETALLE: " + e.toString());
			
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
	
	public List<Tareas> Obtener(){
		logger.info("TAREAS OBTENIDAS");
		return repositorio.findAll();
	}
	
	@Cacheable(value = "taskCache", key="#idTarea")
	public Tareas ObtenerPorId(int idTarea) {	
		Tareas tareaObt = repositorio.findByIdTarea(idTarea);	
		if(tareaObt != null) {
			logger.info(tareaObt.toString());
			
			return tareaObt;
		}
		else {
			logger.info("TAREA NO ENCONTRADA");
			
			return null;
		}
	}
	
	public List<Tareas> obtenerPorPaginacion(Pageable pageable){
		return repositorio.findAll(pageable).getContent();
	}
	
	public String compartirTarea(int idTarea, ArrayList<Integer> usuarios) {
		String message = "";
		
		logger.info("PUNTO 1");
		
		Tareas tarea = repositorio.findByIdTarea(idTarea);
		if(tarea!=null) {
			ArrayList<Integer> usuariosRepetidos = new ArrayList<>();
			ArrayList<Integer> listaUsuariosNoExistentes = new ArrayList<>();
			List<Usuarios> usuariosAPI = servicioUsuarios.findAll();
			
			HashSet<Integer> hs = new HashSet<Integer>(tarea.getUsuarios());
			// LISTA DE USUARIOS DE LA TAREA
			List<Integer> listaUsuarios = new ArrayList<>(hs);
			
			logger.info(listaUsuarios);
			
			if(!listaUsuarios.isEmpty()) {
				for(Integer i : listaUsuarios) {
					for(Integer u : usuarios) {
						if(i == u) {
							usuariosRepetidos.add(i);
						}
						
						if(!usuariosAPI.isEmpty()) {
							boolean encontrado = false;
							
							for(Usuarios user : usuariosAPI) {
								if(u == user.getId()) {
									logger.info("IDUSUARIO: " + u);
									encontrado = true;
								}
							}
								
							if(encontrado == false) {
								listaUsuariosNoExistentes.add(u);
							}
						}		
					}										
				}
				
				if(!usuariosRepetidos.isEmpty()) {
					message = "El/los usuario/s con ID: " + usuariosRepetidos.toString() + " ya comparten la tarea. \n";
				}
				
				if(!listaUsuariosNoExistentes.isEmpty()) {
					message += "El/los usuario/s con ID: " + listaUsuariosNoExistentes.toString() + " no existe/n";
				}
				
				if(message.equals("")) {
					tarea.setUsuarios(usuarios);
					repositorio.save(tarea);
				}
			}
			else {
				tarea.setUsuarios(usuarios);
				repositorio.save(tarea);
			}
		}
		
		return message;
	}
	
}
