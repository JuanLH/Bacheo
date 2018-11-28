package com.example.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.entidades.Municipio;

@Controller
public class IndexController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(Model model) {
		model.addAttribute("titulo","Inicio");
		
		return "index";
	
	}
	
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("titulo","Inicio");
		List<Municipio> listM = Municipio.getMunicipios();
		model.addAttribute("muniList",listM);
		return "test";
	
	}
}
