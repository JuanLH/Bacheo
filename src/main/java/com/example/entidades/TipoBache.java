package com.example.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.clases.Utilities;
import com.example.database.Db;

public class TipoBache {
	int id;
	String nombre;
	float puntaje;
	
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
	
	public static ArrayList<TipoBache> getTiposBache(){
		Db dbase = Utilities.getConection();
		ArrayList<TipoBache> list = new ArrayList<>();
		String query ="SELECT id, nombre, puntaje FROM public.tipo_bache;";
		
		try {
			ResultSet rs=dbase.execSelect(query);
			
			while(rs.next()) {
				TipoBache tb = new TipoBache();
				tb.setId(rs.getInt(1));
				tb.setNombre(rs.getString(2));
				tb.setPuntaje(rs.getFloat(3));
				list.add(tb);
			}
		} catch (SQLException e) {
			System.out.println("Error = "+e.getMessage());
		}
		
		return list;
	}
}
