package com.example.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.clases.Utilities;
import com.example.database.Db;

public class Personal {
	int id,id_ocupacion,id_estado;
	String nombre,cedula,telefono,direccion;
	char sexo;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_ocupacion() {
		return id_ocupacion;
	}
	public void setId_ocupacion(int id_ocupacion) {
		this.id_ocupacion = id_ocupacion;
	}
	public int getId_estado() {
		return id_estado;
	}
	public void setId_estado(int id_estado) {
		this.id_estado = id_estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public char getSexo() {
		return sexo;
	}
	public void setSexo(char sexo) {
		this.sexo = sexo;
	}
	
	public static int getPersonal(String ocupacion) throws SQLException {
		int id_recurso=0;
		Db dbase = Utilities.getConection();
		String query ="SELECT p.id FROM public.personal as p inner join ocupacion as o\r\n" + 
				"  on p.id_ocupacion = o.id\r\n" + 
				"  where o.nombre = '"+ocupacion+"'\r\n" + 
				"  AND p.id_estado = 4 limit 1;";
		ResultSet rs = dbase.execSelect(query);
		if(rs.next()) {
			id_recurso = rs.getInt(1);
			setPersonalEnBrigada(id_recurso);
		}
		else {
			id_recurso = -1;// Esto es para que notifique cuando se acaban los recursos
		}
		//dbase.CerrarConexion();
		return id_recurso;
	}
	
	public static String getPersonalNombre(int idRecurso) throws SQLException {
		String  nombre=null;
		Db dbase = Utilities.getConection();
		String query ="SELECT nombre FROM public.personal where id = "+idRecurso+";";
		ResultSet rs = dbase.execSelect(query);
		if(rs.next()) {
			nombre = rs.getString(1);
		}
		
		dbase.CerrarConexion();
		//si tira null hay algo mal
		return nombre;
	}
	
	private static void setPersonalEnBrigada(int id_recurso) throws SQLException {
		Db dbase = Utilities.getConection();
		String sql = "UPDATE public.personal SET id_estado=5 WHERE id = "+id_recurso+";";
		dbase.executeQuery(sql);
		dbase.CerrarConexion();
	}
	
	public static void setPersonalDisponible(int id_recurso) throws SQLException {
		//pone el personal disponible para el proximo dia
		Db dbase = Utilities.getConection();
		String sql = "UPDATE public.personal SET id_estado=4 WHERE id = "+id_recurso+";";
		dbase.executeQuery(sql);
		dbase.CerrarConexion();
	}
	
}
