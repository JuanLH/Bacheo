package com.example.entidades;

import java.util.ArrayList;
import java.util.Date;

public class Asignacion {
	int id,id_brigada,id_bache;
	Date fecha;
	boolean estado;
	
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
	public int getId_bache() {
		return id_bache;
	}
	public void setId_bache(int id_bache) {
		this.id_bache = id_bache;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public static ArrayList<Bache> getBacheAsignado(Brigada brigada,Date fecha){
		
	}
}
