package com.example.entidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.clases.Utilities;
import com.example.database.Db;

public class Daño {
	int id,id_bache,gasto;
	String descripcion,nombre_ciudadano,cedula,telefono;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_bache() {
		return id_bache;
	}
	public void setId_bache(int id_bache) {
		this.id_bache = id_bache;
	}
	public int getGasto() {
		return gasto;
	}
	public void setGasto(int gasto) {
		this.gasto = gasto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNombre_ciudadano() {
		return nombre_ciudadano;
	}
	public void setNombre_ciudadano(String nombre_ciudadano) {
		this.nombre_ciudadano = nombre_ciudadano;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public static void insert(Daño daño) throws SQLException{
		
		Db dbase = Utilities.getConection();
		String sql ="INSERT INTO public.daños( id_bache, descripcion, nombre_ciudadano, cedula, telefono, gasto) VALUES ( ?, ?, ?, ?, ?, ?);";
		PreparedStatement p = dbase.getConnection().prepareStatement(sql);
		p.setInt(1, daño.getId_bache());
		p.setString(2,daño.getDescripcion());
		p.setString(3, daño.getNombre_ciudadano());
		p.setString(4, daño.getCedula());
		p.setString(5, daño.getTelefono());
		p.setInt(6, daño.getGasto());
		p.execute();
		dbase.CerrarConexion();
	}
	
	public static int countDaños(int id_bache,Db dbase) throws SQLException {
		int cantDaños=0;
		String query = "select count(*) from daños where id_bache ="+id_bache;
		ResultSet rs = dbase.execSelect(query);
		
		if(rs.next()) 
			cantDaños = rs.getInt(1);
		
		
		return cantDaños;
	}
}
