package com.example.controllers;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.clases.Utilities;
import com.example.entidades.Bache;
import com.example.entidades.Daño;
import com.example.entidades.Municipio;
import com.example.entidades.Sector;
import com.example.entidades.Segmento;
import com.example.entidades.TipoBache;
import com.google.gson.Gson;

import net.coobird.thumbnailator.Thumbnails;

@Controller
public class MntController {

	@RequestMapping(value = "/mnt_baches", method = RequestMethod.GET)
	public String mnt_baches(Model model) {
		Bache bache = new Bache();
		
		model.addAttribute("titulo","Mantenimiento de Baches");
		model.addAttribute("bache",bache);
		model.addAttribute("Municipios",Municipio.getMunicipios());
		model.addAttribute("TiposBache",TipoBache.getTiposBache());
	
		
		return "mnt_baches";
	
	}
	
	@RequestMapping(value = "/mnt_baches/guardar_bache", method = RequestMethod.POST)
	public String guardar_bache(@Valid Bache bache, BindingResult result, Model model, @RequestParam("file") MultipartFile foto,RedirectAttributes flash) {
		if(result.hasErrors()) {
			model.addAttribute("titulo","Mantenimiento de Baches");
			return "mnt_baches";
		}
		
		try {
			Bache.insert(bache);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(!foto.isEmpty()) {
			Path dirResources = Paths.get("src//main//resources//static//uploads");
			String rootPath = dirResources.toFile().getAbsolutePath();
			
			try {
				byte[] bytes = foto.getBytes();
				int id_bache = Bache.getLastId();
				String photo_name = id_bache+Utilities.getFileExtension(foto.getOriginalFilename());
				Path rutaCompleta = Paths.get(rootPath+"//"+photo_name);
				Files.write(rutaCompleta, bytes);
				Thumbnails.of(rootPath+"//"+photo_name).size(100, 100).toFile(rootPath+"//thumbnails//"+photo_name);
				try {
					Bache.actualizarFoto(photo_name, id_bache);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				flash.addFlashAttribute("info","Has subido correctamente la foto del bache "+Bache.getLastId());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/mnt_danos", method = RequestMethod.GET)
	public String mnt_daños(Model model) {
		Daño daño = new Daño();
		
		model.addAttribute("titulo","Mantenimiento de Daños");
		model.addAttribute("dano",daño);
		try {
			model.addAttribute("BachesxReparar",Bache.getBachesPorReparar());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
		}
		return "mnt_danos";
	
	}
	
	@RequestMapping(value="/mnt_danos/guardar_dano", method = RequestMethod.POST)
	public String guardar_danos(@Valid Daño daño,Model model,BindingResult result,RedirectAttributes flash) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo","Mantenimiento de Daños");
			return "mnt_danos";
		}
		
		try {
			Daño.insert(daño);
			return "redirect:/";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "mnt_danos";
	}
	
	@RequestMapping(value = "/asignacion_baches", method = RequestMethod.GET)
	public String pro_asignacionB(Model model) {
		Bache bache = new Bache();
		
		model.addAttribute("titulo","Proceso de Asignacion de Baches");
		model.addAttribute("Con", Utilities.getConection());
		try {
			model.addAttribute("BachesxReparar",Bache.getBaches_Puntuacion());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return "pro_asignacion_brigadas";
	
	}
}
