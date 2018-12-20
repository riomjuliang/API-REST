package com.tareas.core.entity;

public class Mensaje {
	private String titulo;
	private String detalle;
	
	public Mensaje(String titulo, String detalle) {
		super();
		this.titulo = titulo;
		this.detalle = detalle;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	@Override
	public String toString() {
		return "Mensaje [titulo=" + titulo + ", detalle=" + detalle + "]";
	}
	
	
	
}
