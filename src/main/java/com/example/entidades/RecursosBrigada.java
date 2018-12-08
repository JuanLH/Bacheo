package com.example.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.clases.Utilities;
import com.example.database.Db;

public class RecursosBrigada {

	int id,id_brigada,id_recurso,id_tipo_recurso;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_brigada() {
		return id_brigada;
	}

	public void setId_brigada(int id_brigada) {
		this.id_brigada = id_brigada;
	}

	public int getId_recurso() {
		return id_recurso;
	}

	public void setId_recurso(int id_recurso) {
		this.id_recurso = id_recurso;
	}

	public int getId_tipo_recurso() {
		return id_tipo_recurso;
	}

	public void setId_tipo_recurso(int id_tipo_recurso) {
		this.id_tipo_recurso = id_tipo_recurso;
	}
	
	public static ArrayList<RecursosBrigada> getRecursos(int id_brigada,Db dbase) throws SQLException{
		//Db dbase = Utilities.getConection();
		String queryRecurso = "SELECT id, id_brigada, id_recurso, id_tipo_recurso\r\n" + 
				"  FROM public.recursos_brigada where id_brigada = "+id_brigada+";";
		ResultSet rs = dbase.execSelect(queryRecurso);
		ArrayList<RecursosBrigada> recursos = new ArrayList<>();
		
		while(rs.next()) {
			RecursosBrigada recurso = new RecursosBrigada();
			recurso.setId(rs.getInt(1));
			recurso.setId_brigada(rs.getInt(2));
			recurso.setId_recurso(rs.getInt(3));
			recurso.setId_tipo_recurso(rs.getInt(4));
			
			
			recursos.add(recurso);
		}
		//dbase.CerrarConexion();
		
		return recursos;
	}
	
	public static void borrarRecursos(int id_brigada,Db dbase) throws SQLException {
		//dbase = Utilities.getConection();
		//for(RecursosBrigada rec : recursos) {
			String borrar = "DELETE FROM public.recursos_brigada WHERE id_brigada = "+id_brigada+";";
			dbase.executeQuery(borrar);
		//}
		//dbase.CerrarConexion();
	}
	
}
