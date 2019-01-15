package com.tareas.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name="TAREAS")
@Entity
public class Tareas implements Serializable{
	@GeneratedValue
	@Id
	@Column(name="IDTAREA")
	private int idTarea;
	
	@Column(name="DESCRIPCION")
	private String descripcion;
	
	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="IDUSUARIO")
	private int idUsuario;
	
	@Column(name="FECHA_CREACION")
	private Date fecha_creacion;
	
	@Column(name="USUARIOS")
	private ArrayList<Integer> usuarios = new ArrayList<Integer>();

	
	public Tareas() {}

	public Tareas(int idTarea, String descripcion, String nombre, int idUsuario, Date fecha_creacion,
			ArrayList<Integer> usuarios) {
		super();
		this.idTarea = idTarea;
		this.descripcion = descripcion;
		this.nombre = nombre;
		this.idUsuario = idUsuario;
		this.fecha_creacion = fecha_creacion;
		this.usuarios = usuarios;
	}
	

	@Override
	public String toString() {
		return "Tareas [idTarea=" + idTarea + ", descripcion=" + descripcion + ", nombre=" + nombre + ", idUsuario="
				+ idUsuario + ", fecha_creacion=" + fecha_creacion + ", usuarios=" + usuarios + "]";
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
	public void setFecha_creacion(Date date) {
		this.fecha_creacion = date;
	}

	public ArrayList<Integer> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Integer> usuarios) {
		this.usuarios = usuarios;
	}

	
	
}
