package com.tareas.core.externalServices;

import java.util.List;

import org.json.JSONArray;

import com.google.gson.JsonArray;
import com.tareas.core.entity.Token;
import com.tareas.core.entity.Usuarios;

public interface UsuariosExternos {
	List<Usuarios> findAll();
	
	Usuarios getById(int id);
	
	Object obtenerToken();
}
