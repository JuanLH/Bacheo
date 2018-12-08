package com.example.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.clases.Utilities;
import com.example.database.Db;

public class TipoBrigada {
	int id;
	String tipo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public static String getTipo(int id_tipo,Db dbase) throws SQLException {
		String tipo="Null";
		//Db dbase = Utilities.getConection();
		String query = "SELECT tipo FROM public.tipo_brigada where id = "+id_tipo+";";
		ResultSet rs = dbase.execSelect(query);
		
		if(rs.next()) {
			tipo = rs.getString(1);
		}
		
		//dbase.CerrarConexion();
		return tipo;
	}
	
}
