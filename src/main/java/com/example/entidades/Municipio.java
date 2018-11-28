package com.example.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.clases.Utilities;
import com.example.database.Db;

public class Municipio {
	int id;
	String nombre;
	float puntaje;
	
	
	public Municipio() {

	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	public static ArrayList<Municipio> getMunicipios(){
		Db dbase = Utilities.getConection();
		ArrayList<Municipio> list = new ArrayList<>();
		String query = "SELECT id, nombre, puntaje FROM public.municipios;";
		
		
		try {
			ResultSet rs=dbase.execSelect(query);
			
			while(rs.next()) {
				Municipio m = new Municipio();
				m.setId(rs.getInt(1));
				m.setNombre(rs.getString(2));
				m.setPuntaje(rs.getFloat(3));
				list.add(m);
			}
		} catch (SQLException e) {
			System.out.println("Error = "+e.getMessage());
		}
		
		return list;
	}
	
	
	
}
