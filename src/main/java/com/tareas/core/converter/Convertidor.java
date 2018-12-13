package com.tareas.core.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tareas.core.entity.Tareas;
import com.tareas.core.model.MTareas;

@Component("convertidor")
public class Convertidor {
	public List<MTareas> convertirLista(List<Tareas> tareas){
		List<MTareas> mtareas = new ArrayList<>();
		
		for(Tareas tarea : tareas) {
			mtareas.add(new MTareas(tarea));
		}
		
		return mtareas;
	}
}
