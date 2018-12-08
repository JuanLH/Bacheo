package com.example.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.clases.Utilities;
import com.example.database.Db;

public class Segmento {
	int id,id_sector,id_calle,id_tipo_material;
	String descripcion;
	float puntaje;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_sector() {
		return id_sector;
	}
	public void setId_sector(int id_sector) {
		this.id_sector = id_sector;
	}
	public int getId_calle() {
		return id_calle;
	}
	public void setId_calle(int id_calle) {
		this.id_calle = id_calle;
	}
	public int getId_tipo_material() {
		return id_tipo_material;
	}
	public void setId_tipo_material(int id_tipo_material) {
		this.id_tipo_material = id_tipo_material;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public float getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(float puntaje) {
		this.puntaje = puntaje;
	}
	
	public static ArrayList<Segmento> getSegmentos(int sectorId) throws SQLException{
		Db dbase = Utilities.getConection();
		ArrayList<Segmento> list = new ArrayList<>();
		String query = "SELECT id,id_sector,id_calle,id_tipo_material,descripcion FROM public.segmentos where id_sector ="+sectorId+";";
		
		
		ResultSet rs=dbase.execSelect(query);
		
		while(rs.next()) {
			Segmento s = new Segmento();
			s.setId(rs.getInt(1));
			s.setId_sector(rs.getInt(2));
			s.setId_calle(rs.getInt(3));
			s.setId_tipo_material(rs.getInt(4));
			s.setDescripcion(Calle.getNombreCalle(s.getId_calle(),dbase)+" "+rs.getString(5));
			list.add(s);
		}
		
		return list;
	}
	
	public static Segmento getSegmento(int idSegmento) throws SQLException{
		Db dbase = Utilities.getConection();
		Segmento s = new Segmento();
		String query = "SELECT id,id_sector,id_calle,id_tipo_material,descripcion FROM public.segmentos where id ="+idSegmento+";";
		
		
		ResultSet rs=dbase.execSelect(query);
		
		rs.next();
			
		s.setId(rs.getInt(1));
		s.setId_sector(rs.getInt(2));
		s.setId_calle(rs.getInt(3));
		s.setId_tipo_material(rs.getInt(4));
		s.setDescripcion(Calle.getNombreCalle(s.getId_calle(),dbase)+" "+rs.getString(5));
	
		//dbase.CerrarConexion();
		return s;
	}
}
