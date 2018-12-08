package com.example.entidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	
	public static int getPersonal(String ocupacion,Db dbase) throws SQLException {
		int id_recurso=0;
		//dbase = Utilities.getConection();
		
		String query ="SELECT p.id FROM public.personal as p inner join ocupacion as o\r\n" + 
				"  on p.id_ocupacion = o.id\r\n" + 
				"  where o.nombre = '"+ocupacion+"'\r\n" + 
				"  AND p.id_estado = 4 limit 1;";
		ResultSet rs = dbase.execSelect(query);
		if(rs.next()) {
			id_recurso = rs.getInt(1);
			setPersonalEnBrigada(id_recurso,dbase);
		}
		else {
			id_recurso = -1;// Esto es para que notifique cuando se acaban los recursos
		}
		//dbase.CerrarConexion();
		return id_recurso;
	}
	
	public static String getPersonalNombre(int idRecurso,Db dbase) throws SQLException {
		String  nombre=null;
		//dbase = Utilities.getConection();
		String query ="SELECT nombre FROM public.personal where id = "+idRecurso+";";
		ResultSet rs = dbase.execSelect(query);
		if(rs.next()) {
			nombre = rs.getString(1);
		}
		
		//dbase.CerrarConexion();
		//si tira null hay algo mal
		return nombre;
	}
	
	private static void setPersonalEnBrigada(int id_recurso,Db dbase) throws SQLException {
		//Db dbase = Utilities.getConection();
		String sql = "UPDATE public.personal SET id_estado=5 WHERE id = "+id_recurso+";";
		dbase.executeQuery(sql);
		//dbase.CerrarConexion();
		
	}
	
	public static void setPersonalDisponible(int id_recurso,Db dbase) throws SQLException {
		//pone el personal disponible para el proximo dia
		//dbase = Utilities.getConection();
		String sql = "UPDATE public.personal SET id_estado=4 WHERE id = "+id_recurso+";";
		dbase.executeQuery(sql);
		System.out.println("Se actualizo a disponible el personal "+id_recurso);
		//dbase.CerrarConexion();
	}
	public static ArrayList<PersonalEnBrigada> getPersonalEnBrigada(String nombre_brigada) throws SQLException{
		ArrayList<PersonalEnBrigada> list = new ArrayList<>();
		String query = "Select oc.nombre,per.nombre from ocupacion as oc \r\n" + 
				"inner join personal as per on  oc.id = per.id_ocupacion\r\n" + 
				"inner join recursos_brigada as rb on rb.id_recurso = per.id and rb.id_tipo_recurso = 1\r\n" + 
				"inner join brigada as bri on bri.id = rb.id_brigada\r\n" + 
				"where bri.nombre = '"+nombre_brigada+"';";
		Db dbase = Utilities.getConection();
		ResultSet rs = dbase.execSelect(query);
		while(rs.next()) {
			PersonalEnBrigada pb = new PersonalEnBrigada();
			pb.setOcupacion(rs.getString(1));
			pb.setNombre(rs.getString(2));
			list.add(pb);
		}
		return list;
	}
	 
	private static class PersonalEnBrigada{
		String ocupacion,nombre;

		public String getOcupacion() {
			return ocupacion;
		}

		public void setOcupacion(String ocupacion) {
			this.ocupacion = ocupacion;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		
		 
	}
	
}
