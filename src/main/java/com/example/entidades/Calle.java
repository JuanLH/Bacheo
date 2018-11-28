package com.example.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.clases.Utilities;
import com.example.database.Db;

public class Calle {
	int id,id_tipo_calle;
	String nombre;
	float puntaje;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_tipo_calle() {
		return id_tipo_calle;
	}
	public void setId_tipo_calle(int id_tipo_calle) {
		this.id_tipo_calle = id_tipo_calle;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public float getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(float puntaje) {
		this.puntaje = puntaje;
	}
	
	public static String getNombreCalle(int calleId,Db dbase) throws SQLException {
		
		String query = "SELECT nombre FROM public.calles where  id = "+calleId+";";
		ResultSet rs=dbase.execSelect(query);
		rs.next();
		String nombre = rs.getString(1);
		return nombre;
		
	}
	
	
}
