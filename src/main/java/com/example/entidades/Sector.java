package com.example.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.clases.Utilities;
import com.example.database.Db;

public class Sector {
	int id,id_municipio;
	String nombre;
	float puntaje;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_municipio() {
		return id_municipio;
	}
	public void setId_municipio(int id_municipio) {
		this.id_municipio = id_municipio;
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
	
	public static ArrayList<Sector> getSectores(int municipioId){
		Db dbase = Utilities.getConection();
		ArrayList<Sector> list = new ArrayList<>();
		String query ="SELECT id, id_municipio, nombre, puntaje FROM public.sectores where id_municipio="+municipioId+";";
		
		try {
			ResultSet rs=dbase.execSelect(query);
			
			while(rs.next()) {
				Sector s = new Sector();
				s.setId(rs.getInt(1));
				s.setId_municipio(rs.getInt(2));
				s.setNombre(rs.getString(3));
				s.setPuntaje(rs.getFloat(4));
				list.add(s);
			}
		} catch (SQLException e) {
			System.out.println("Error = "+e.getMessage());
		}
		
		return list;
	}
}
