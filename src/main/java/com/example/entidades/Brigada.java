package com.example.entidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;

import com.example.clases.Utilities;
import com.example.database.Db;

public class Brigada {
	int id,id_tipo_brigada,id_estado,capacidad_diaria;
	String nombre;
	public ArrayList<Bache> bachesAsignados= new ArrayList<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_tipo_brigada() {
		return id_tipo_brigada;
	}
	public void setId_tipo_brigada(int id_tipo_brigada) {
		this.id_tipo_brigada = id_tipo_brigada;
	}
	public int getId_estado() {
		return id_estado;
	}
	public void setId_estado(int id_estado) {
		this.id_estado = id_estado;
	}
	public int getCapacidad_diaria() {
		return capacidad_diaria;
	}
	public void setCapacidad_diaria(int capacidad_diaria) {
		this.capacidad_diaria = capacidad_diaria;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/*Este metodo se encarga de buscar una brigada por fecha para asignarle un bache
	 * , si no la encuentra disponible pues la creara,
	 * si no la pudo crear pues retorna null*/
	public static Brigada buscarBrigada(Bache  bache,Calendar fecha,Db dbase) throws SQLException {
		ArrayList<Brigada> lista_brigadas;
		ArrayList<Bache> lista_baches;
		int id_segmento;
		
		//Bache bache = Bache.getBache(idBache, dbase);
		id_segmento = bache.getId_segmento();
		Segmento segmento = Segmento.getSegmento(id_segmento);
		
		if(segmento.getId_tipo_material() == 1) {//Asfalto
			if(bache.getId_tipo_bache() == 1 || bache.getId_tipo_bache() == 2) {//Brigada Especial
				lista_brigadas = getBrigadaXTipo(3); // 3 - Brigadas especial asfalto
			}
			else {//Brigada Estandar
				lista_brigadas = getBrigadaXTipo(1); //Brigadas estandar asfalto
			}
		}
		else {//Concreto
			if(bache.getId_tipo_bache() == 1 || bache.getId_tipo_bache() == 2) {//Brigada Especial
				lista_brigadas = getBrigadaXTipo(4);//Brigadas Especiales de concreto
			}
			else {//Brigada Estandar
				lista_brigadas = getBrigadaXTipo(2);//Brigadas estandar de concreti
			}
		}
		
		for(Brigada brigada:lista_brigadas) {
			System.out.println("Entro a buscar la capacidad");
			int capacidad = getCapacidadDiariaAcum(brigada, fecha);
			int capacidadAcum = bache.getTam_bache()+capacidad;
			if(brigada.getCapacidad_diaria()>=capacidadAcum) {
				System.out.println("retorno brigada");
				return brigada;
			}
		}
		
		System.out.println("Tiene que meterse por aqui para iniciar----------------------------");
		//Si no retorna al buscar una brigada entonces aqui se  crea una brigada nueva si no se crea retorna null
		Brigada bri;
		if(segmento.getId_tipo_material() == 1) {//Asfalto
			if(bache.getId_tipo_bache() == 1 || bache.getId_tipo_bache() == 2) {//Brigada Especial
				bri = crearBrigada(3,dbase); // 3 - Brigadas especial asfalto
			}
			else {//Brigada Estandar
				bri = crearBrigada(1,dbase); //Brigadas estandar asfalto
			}
		}
		else {//Concreto
			if(bache.getId_tipo_bache() == 1 || bache.getId_tipo_bache() == 2) {//Brigada Especial concreto
				bri = crearBrigada(4,dbase);//Brigadas Especiales de concreto
			}
			else {//Brigada Estandar
				bri = crearBrigada(2,dbase);//Brigadas estandar de concreto
			}
		}
		
		return bri;
	}
	
	public static int getCapacidadDiariaAcum(Brigada brigada, Calendar fecha) throws SQLException {
		ArrayList<Bache> lista_baches = Asignacion.getBachesAsignado(brigada, fecha);
		
		int capacidad=0;
		for(Bache b : lista_baches) {
			capacidad += b.getTam_bache();
		}
		return capacidad;
	}
	
	//Este metodo tiene las plantillas para crear las brigadas solo se le envia el tipo de brigada que quieres y listo
	private static Brigada crearBrigada(int tipoBrigada,Db dbase) throws SQLException {
		Map<String, Integer> personal = new HashMap<String, Integer>();
		Map<String, Integer> equipos = new HashMap<String, Integer>();
		
		if(tipoBrigada == 1) {
			//PLANTILLA Brigada Estandar Asfalto
			personal.put("SUPERVISOR", 1);
			personal.put("CHOFER", 1);
			personal.put("OBRERO", 2); 
			personal.put("CAPACIDAD_DIARIA", 6);
			
			equipos.put("CORTADORA DE ASFALTO", 1);
			equipos.put("PICO", 2);
			equipos.put("CARRETILLA", 1);
			equipos.put("CUBO", 2);
			equipos.put("CAMION CAMA LARGA", 1);
			equipos.put("COMPRESOR DE AIRE", 1);
			equipos.put("PALA DE CORTE", 2);
			equipos.put("PLANCHA COMPACTADORA", 1);
			equipos.put("RASTRILLO DE NIVELACION", 1);
			
			return crearBrigada(personal, equipos, tipoBrigada);
		}
		
		if(tipoBrigada == 3) {
			//PLANTILLA Brigada Especial Asfalto
			personal.put("SUPERVISOR", 1);
			personal.put("INGENIERO", 1);
			personal.put("CHOFER", 1);
			personal.put("OBRERO", 4); 
			personal.put("CAPACIDAD_DIARIA", 10);
			
			equipos.put("CORTADORA DE ASFALTO", 1);
			equipos.put("PICO", 3);
			equipos.put("CARRETILLA", 2);
			equipos.put("CUBO", 3);
			equipos.put("CAMION CAMA LARGA", 1);
			equipos.put("COMPRESOR DE AIRE", 1);
			equipos.put("PALA DE CORTE", 3);
			equipos.put("PLANCHA COMPACTADORA", 1);
			equipos.put("RASTRILLO DE NIVELACION", 2);
			equipos.put("PAVIMENTADORA DE ASFALTO", 1);
			equipos.put("COMPACTADOR VIBRATORIO DE RODILLO", 1);
			
			return crearBrigada(personal, equipos, tipoBrigada);
			
		}
		
		if(tipoBrigada == 2) {
			//PLANTILLA Brigada Estandar concreto
			personal.put("SUPERVISOR", 1);
			personal.put("CHOFER", 1);
			personal.put("OBRERO", 2);
			personal.put("CAPACIDAD_DIARIA", 6);
			
			equipos.put("MEZCLADORA DE CONCRETO", 1);
			equipos.put("PICO", 2);
			equipos.put("CARRETILLA", 1);
			equipos.put("CUBO", 2);
			equipos.put("CAMION CAMA LARGA", 1);
			equipos.put("PALA DE MEZCLAR", 2);
			
			return crearBrigada(personal, equipos, tipoBrigada);
		}
		
		if(tipoBrigada == 4) {
			//PLANTILLA Brigada Especial concreto
			personal.put("SUPERVISOR", 1);
			personal.put("INGENIERO", 1);
			personal.put("CHOFER", 2);
			personal.put("OBRERO", 4); 
			personal.put("CAPACIDAD_DIARIA", 10);
			
			equipos.put("TROMPO MESCLADOR DE CONCRETO", 1);
			equipos.put("PICO", 2);
			equipos.put("CARRETILLA", 2);
			equipos.put("CUBO", 2);
			equipos.put("CAMION CAMA LARGA", 1);
			equipos.put("PALA DE MEZCLAR", 2);
			
			
			return crearBrigada(personal, equipos, tipoBrigada);
			
		}
		return null;
	}
	
	private static Brigada crearBrigada(Map<String,Integer> personal,Map<String,Integer> equipos,int tipo) throws SQLException {
		int id_brigada;
		boolean result=true;
		String nombreSupervisor = null;
		Db dbase = Utilities.getConection();
		String sql ="INSERT INTO public.brigada(\r\n" + 
				"    nombre, id_tipo_brigada, capacidad_diaria, estado)\r\n" + 
				"    VALUES ( ?, ?, ?, ?);";
		PreparedStatement p = dbase.getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		p.setString(1, "BRIGADA TIPO "+tipo);
		p.setInt(2, tipo);
		p.setInt(3,personal.get("CAPACIDAD_DIARIA"));
		p.setInt(4, 0);//Estado 0 sin actividad
		p.executeUpdate();
		ResultSet re = p.getGeneratedKeys();
		re.next();
		id_brigada = re.getInt(1);
		
		System.out.println("Id Brigada obtenido-->"+id_brigada);
		
		for(String key: personal.keySet()) {
			//Este mapa era para crear la brigada
			if(key.equals("CAPACIDAD_DIARIA"))
				continue;
			
			int tipo_recurso = 1;//Personal
			for(int x=0;x<personal.get(key);x++) {
				int id_recurso = buscarRecurso(key, tipo_recurso);
				
				if(id_recurso < 1) {
					result = false;
					break;
				}
				
				//Nombre del supervisor para nombrar la brigada
				if(key.equals("SUPERVISOR")) {
					nombreSupervisor = Personal.getPersonalNombre(id_recurso);
				}
				
				
				insertRecursoEnBrigada(id_brigada, id_recurso, tipo_recurso,dbase);
				System.out.println("Se inserto personal id "+id_recurso+" En Brigada "+id_brigada);	
			}
			
			if(!result)
				break;
		}
		
		
		for(String key: equipos.keySet()) {
			int tipo_recurso = 2;//Equipos
			for(int x=0;x<equipos.get(key);x++) {
				int id_recurso = buscarRecurso(key, tipo_recurso);
				if(id_recurso < 1) {
					result = false;
					break;
				}
				
				insertRecursoEnBrigada(id_brigada, id_recurso, tipo_recurso,dbase);
				System.out.println("Se inserto equipo id "+id_recurso+" En Brigada "+id_brigada);	
			}
			
			if(!result)
				break;
		}
		//Se actualiza el nombre de la brigada
		String updateBrigada = "UPDATE public.brigada SET  nombre=? WHERE id = ?;";
		PreparedStatement p2 = dbase.getConnection().prepareStatement(updateBrigada);
		p2.setString(1,TipoBrigada.getTipo(tipo)+" - "+nombreSupervisor);
		p2.setInt(2,id_brigada);
		p2.executeUpdate();
		
		//dbase.CerrarConexion();
		//Se retorna la brigada completa
		return getBrigada(id_brigada);
	}
	
	public static Brigada getBrigada(int id_brigada) throws SQLException {
		Db dbase = Utilities.getConection();
		String queryBrigada = "SELECT id, nombre, id_tipo_brigada, capacidad_diaria, estado FROM public.brigada where id = "+id_brigada+";";
		ResultSet rs = dbase.execSelect(queryBrigada);
		Brigada brigada = new Brigada();
		rs.next();
		brigada.setId(rs.getInt(1));
		brigada.setNombre(rs.getString(2));
		brigada.setId_tipo_brigada(rs.getInt(3));
		brigada.setCapacidad_diaria(rs.getInt(4));
		brigada.setId_estado(rs.getInt(5));
		dbase.CerrarConexion();
		
		return brigada;
	}
	
	public static ArrayList<Brigada> getBrigadaPorEstado(int estado,Db dbase) throws SQLException {
		//Db dbase = Utilities.getConection();
		String queryBrigada = "SELECT id, nombre, id_tipo_brigada, capacidad_diaria, estado FROM public.brigada where estado = "+estado+";";
		ResultSet rs = dbase.execSelect(queryBrigada);
		ArrayList<Brigada> brigadas = new ArrayList<>();
		
		while(rs.next()) {
			Brigada brigada = new Brigada();
			brigada.setId(rs.getInt(1));
			brigada.setNombre(rs.getString(2));
			brigada.setId_tipo_brigada(rs.getInt(3));
			brigada.setCapacidad_diaria(rs.getInt(4));
			brigada.setId_estado(rs.getInt(5));
			
			brigadas.add(brigada);
		}
		//dbase.CerrarConexion();
		
		return brigadas;
	}
	
	public static ArrayList<Brigada> getBrigadaXTipo(int tipo_brigada) throws SQLException {
		Db dbase = Utilities.getConection();
		String queryBrigada = "SELECT id, nombre, id_tipo_brigada, capacidad_diaria, estado FROM public.brigada where id_tipo_brigada = "+tipo_brigada+";";
		ResultSet rs = dbase.execSelect(queryBrigada);
		ArrayList<Brigada> list = new ArrayList<>();
		while(rs.next()) {
			Brigada brigada = new Brigada();
			brigada.setId(rs.getInt(1));
			brigada.setNombre(rs.getString(2));
			brigada.setId_tipo_brigada(rs.getInt(3));
			brigada.setCapacidad_diaria(rs.getInt(4));
			brigada.setId_estado(rs.getInt(5));
			list.add(brigada);
		}
		dbase.CerrarConexion();
		return list;
	}
	
	private static int buscarRecurso(String descripcion,int tipoRecurso) throws SQLException {
		int id_recurso=0;
		if(tipoRecurso == 1) {
			//Buscar Personal
			id_recurso = Personal.getPersonal(descripcion);
		}
		else {
			//Buscar Equipos
			id_recurso = Equipo.getEquipo(descripcion);
		}
		
		return id_recurso;
	}
	
	private static void insertRecursoEnBrigada(int id_brigada,int id_recurso,int tipo_recurso,Db dbase) throws SQLException {
		dbase = Utilities.getConection();
		String sql="INSERT INTO public.recursos_brigada(id_brigada, id_recurso, id_tipo_recurso)\r\n" + 
				"VALUES (?, ?, ?);";
		PreparedStatement p = dbase.getConnection().prepareStatement(sql);
		p.setInt(1, id_brigada);
		p.setInt(2, id_recurso);
		p.setInt(3, tipo_recurso);
		p.executeUpdate();
		//dbase.CerrarConexion();
	}
	
	public static void borrarBrigadas(ArrayList<Brigada> brigadas,Db dbase) throws SQLException {
		//Db dbase = Utilities.getConection();
		for(Brigada bri : brigadas) {
			String borrar = "DELETE FROM public.brigada WHERE id = "+bri.getId()+";";
			dbase.executeQuery(borrar);
		}
		//dbase.CerrarConexion();
	}
}
