package com.example.controllers;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entidades.Asignacion;
import com.example.entidades.Bache;
import com.example.entidades.Sector;
import com.example.entidades.Segmento;
import com.google.gson.Gson;

@Controller
public class ApiController {

	
	@RequestMapping(value = "/api/getSectores/{id_municipio}", method = RequestMethod.GET)
	@ResponseBody
	public String getSectores(@PathVariable("id_municipio") String idMunicipio) {
		Gson gson = new Gson();
		return gson.toJson(Sector.getSectores(Integer.parseInt(idMunicipio)));
	
	}
	
	@RequestMapping(value = "/api/getSegmentos/{id_sector}", method = RequestMethod.GET)
	@ResponseBody
	public String getSegmentos(@PathVariable("id_sector") String idSector) {
		Gson gson = new Gson();
		try {
			return gson.toJson(Segmento.getSegmentos(Integer.parseInt(idSector)));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
	}
	
	@RequestMapping(value = "/api/test/", method = RequestMethod.GET)
	@ResponseBody
	public String getTest() {
		Gson gson = new Gson();
		String json = "";
		try {
			 json = gson.toJson(Asignacion.asignarBrigadas(Bache.getBaches_Puntuacion()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
}
