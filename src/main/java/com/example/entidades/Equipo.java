package com.example.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.clases.Utilities;
import com.example.database.Db;

public class Equipo {
	int id,id_tipo_equipo,id_estado;
	String descripcion;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_tipo_equipo() {
		return id_tipo_equipo;
	}
	public void setId_tipo_equipo(int id_tipo_equipo) {
		this.id_tipo_equipo = id_tipo_equipo;
	}
	public int getId_estado() {
		return id_estado;
	}
	public void setId_estado(int id_estado) {
		this.id_estado = id_estado;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public static int getEquipo(String descripcion,Db dbase) throws SQLException{
		int id_recurso = 0;
		//dbase = Utilities.getConection();
		String query = "SELECT id \r\n" + 
				"  FROM public.equipos where descripcion = '"+descripcion+"' "
						+ "and id_estado = 1 limit 1;";
		System.err.println(query);
		ResultSet rs = dbase.execSelect(query);
		if(rs.next()) {
			id_recurso = rs.getInt(1);
			setEquiposEnBrigada(id_recurso,dbase);
		}
		else {
			id_recurso = -1;// Esto es para que notifique cuando se acaban los recursos
		}
		
		//dbase.CerrarConexion();
		return id_recurso;
		
	}
	
	private static void setEquiposEnBrigada(int id_recurso,Db dbase) throws SQLException {
		//Db dbase = Utilities.getConection();
		String sql = "UPDATE public.equipos SET id_estado=2 WHERE id="+id_recurso+" ;";
		dbase.executeQuery(sql);
		//dbase.CerrarConexion();
	}
	
	public static void setEquiposDisponibles(int id_recurso,Db dbase) throws SQLException {
		//pone los equipos disponible para el proximo dia
		//dbase = Utilities.getConection();
		String sql = "UPDATE public.equipos SET id_estado=1 WHERE id="+id_recurso+";";
		dbase.executeQuery(sql);
		System.out.println("Se actualizo a disponible  el equipo "+id_recurso);
		//dbase.CerrarConexion();
	}
	
	public static ArrayList<EquiposEnBrigada> getEquiposEnBrigada(String nombreBrigada) throws SQLException {
		ArrayList<EquiposEnBrigada> list = new ArrayList<>();
		String query = "select  eq.descripcion,tipe.tipo from equipos as eq inner join tipo_equipo as tipe on eq.id_tipo_equipo = tipe.id\r\n" + 
				"inner join recursos_brigada as rb on eq.id = rb.id_recurso and rb.id_tipo_recurso = 2\r\n" + 
				"inner join brigada as bri on bri.id = rb.id_brigada\r\n" + 
				"where bri.nombre = '"+nombreBrigada+"'";
		Db dbase = Utilities.getConection();
		ResultSet rs = dbase.execSelect(query);
		while(rs.next()) {
			EquiposEnBrigada eb = new EquiposEnBrigada();
			eb.setDescripcion(rs.getString(1));
			eb.setTipo(rs.getString(2));
			list.add(eb);
		}
		return list;
	}
	 private static class EquiposEnBrigada{
		private String descripcion;
		private String tipo;
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
	}
	
}
