package com.tareas.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.tareas.core.entity.Tareas;

public class MTareas {
	
	private int idTarea;
	private String descripcion;
	private String nombre;
	private int idUsuario;
	private Date fecha_creacion;
	
	public MTareas(Tareas tarea) {
		this.idTarea = tarea.getIdTarea();
		this.descripcion = tarea.getDescripcion();
		this.nombre = tarea.getNombre();
		this.idUsuario = tarea.getIdUsuario();
		this.fecha_creacion = tarea.getFecha_creacion();
	}
	
	public MTareas(int idTarea, String descripcion, String nombre, int idUsuario, Date fecha_creacion) {
		this.idTarea = idTarea;
		this.descripcion = descripcion;
		this.nombre = nombre;
		this.idUsuario = idUsuario;
		this.fecha_creacion = fecha_creacion;
	}
	public int getIdTarea() {
		return idTarea;
	}
	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Date getFecha_creacion() {
		return fecha_creacion;
	}
	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	
	
}
