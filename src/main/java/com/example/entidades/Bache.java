package com.example.entidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.joda.time.Days;
import org.joda.time.Interval;

import com.example.clases.SortByPuntaje;
import com.example.clases.Utilities;
import com.example.database.Db;

public class Bache {
	
	private int id, id_segmento,id_tipo_bache,cant_servicios_afec;
	private boolean entaponamiento;
	private String  photo_name;
	private Date fecha_registro,fecha_reparacion;
	private float puntaje;
	private int peligrosidad,tam_bache;
	private static Db dbase=null;
	public Date fecha_asignacion;
	
	
	public void setFecha_asignacion(Date fecha_asignacion) {
		this.fecha_asignacion = fecha_asignacion;
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_segmento() {
		return id_segmento;
	}
	public void setId_segmento(int id_segmento) {
		this.id_segmento = id_segmento;
	}
	public int getId_tipo_bache() {
		return id_tipo_bache;
	}
	public void setId_tipo_bache(int id_tipo_bache) {
		this.id_tipo_bache = id_tipo_bache;
	}
	public int getCant_servicios_afec() {
		return cant_servicios_afec;
	}
	public void setCant_servicios_afec(int cant_servicios_afec) {
		this.cant_servicios_afec = cant_servicios_afec;
	}
	public boolean isEntaponamiento() {
		return entaponamiento;
	}
	public void setEntaponamiento(boolean entaponamiento) {
		this.entaponamiento = entaponamiento;
	}
	public Date getFecha_registro() {
		return fecha_registro;
	}
	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	public Date getFecha_reparacion() {
		return fecha_reparacion;
	}
	public void setFecha_reparacion(Date fecha_reparacion) {
		this.fecha_reparacion = fecha_reparacion;
	}
	public String getPhoto_name() {
		return photo_name;
	}
	public void setPhoto_name(String photo_name) {
		this.photo_name = photo_name;
	}
	public float getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(float puntaje) {
		this.puntaje = puntaje;
	}
	public int getPeligrosidad() {
		return peligrosidad;
	}
	public void setPeligrosidad(int peligrosidad) {
		this.peligrosidad = peligrosidad;
	}
	public int getTam_bache() {
		return tam_bache;
	}
	public void setTam_bache(int tam_bache) {
		this.tam_bache = tam_bache;
	}
	
	
	public static void insert(Bache bache) throws SQLException {
		Db dbase = Utilities.getConection();
		String sql ="INSERT INTO public.baches(fecha_registro, id_segmento, entaponamiento, cant_servicios_afec, id_tipo_bache, peligrosidad, tam_bache) VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		PreparedStatement p = dbase.getConnection().prepareStatement(sql);
		p.setDate(1, Utilities.convertUtilToSql(new java.util.Date()));
		p.setInt(2,bache.getId_segmento());
		p.setBoolean(3, bache.isEntaponamiento());
		p.setInt(4, bache.getCant_servicios_afec());
		p.setInt(5, bache.getId_tipo_bache());
		p.setInt(6, bache.getPeligrosidad());
		p.setInt(7, bache.getTam_bache());
		
		p.execute();
		dbase.CerrarConexion();	
	}
	
	public static ArrayList<Bache> getBachesPorReparar() throws SQLException{
		Db dbase = Utilities.getConection();
		ArrayList<Bache> list = new ArrayList<>();
		String query = "SELECT id, fecha_registro, id_segmento, entaponamiento, cant_servicios_afec, \r\n" + 
				"        id_tipo_bache, photo_name, peligrosidad, tam_bache\r\n" + 
				"  FROM public.baches where fecha_reparacion is null;";
		ResultSet rs = dbase.execSelect(query);
		while(rs.next()) {
			Bache b = new Bache();
			b.setId(rs.getInt(1));
			b.setFecha_registro(rs.getDate(2));
			b.setId_segmento(rs.getInt(3));
			b.setEntaponamiento(rs.getBoolean(4));
			b.setCant_servicios_afec(rs.getInt(5));
			b.setFecha_reparacion(null);
			b.setId_tipo_bache(rs.getInt(6));
			b.setPhoto_name(rs.getString(7));
			b.setPeligrosidad(rs.getInt(8));
			b.setTam_bache(rs.getInt(9));
			list.add(b);
		}
		dbase.CerrarConexion();
		return list;
	}
	
	public  static ArrayList<Bache> getBaches_Puntuacion() throws SQLException{
		Db dbase = Utilities.getConection();
		ArrayList<Bache> list = new ArrayList<>();
		String query = "SELECT id, fecha_registro, id_segmento, entaponamiento, cant_servicios_afec, \r\n" + 
				"        id_tipo_bache, photo_name, peligrosidad, tam_bache\r\n" + 
				"  FROM public.baches where fecha_reparacion is null;";
		ResultSet rs = dbase.execSelect(query);
		while(rs.next()) {
			Bache b = new Bache();
			b.setId(rs.getInt(1));
			b.setFecha_registro(rs.getDate(2));
			b.setId_segmento(rs.getInt(3));
			b.setEntaponamiento(rs.getBoolean(4));
			b.setCant_servicios_afec(rs.getInt(5));
			b.setFecha_reparacion(null);
			b.setId_tipo_bache(rs.getInt(6));
			b.setPhoto_name(rs.getString(7));
			b.setPeligrosidad(rs.getInt(8));
			b.setTam_bache(rs.getInt(9));
			b.setPuntaje(calcularPuntaje(b.getId(),dbase));
			list.add(b);
		}
		
		Collections.sort(list, new SortByPuntaje());
		Collections.reverse(list);
		
		dbase.CerrarConexion();
		return list;
	}
	
	public static int getLastId() {
		Db dbase = Utilities.getConection();
		String query = "SELECT id FROM public.baches ORDER BY id DESC LIMIT 1;";
		
		try {
			ResultSet rs = dbase.execSelect(query);
			if(rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void actualizarFoto(String name,int id_bache) throws SQLException {
		Db dbase = Utilities.getConection();
		String sql = "UPDATE public.baches SET  photo_name=? WHERE id = ?;";
		
		PreparedStatement p = dbase.getConnection().prepareStatement(sql);
		p.setString(1, name);
		p.setInt(2, id_bache);
		p.execute();
		dbase.CerrarConexion();
	}
	
	public String getTipo() {
		String sql = "SELECT nombre\r\n" + 
				"  FROM public.tipo_bache where id="+id_tipo_bache+";";
		try {
			ResultSet rs = dbase.execSelect(sql);
			if(rs.next()) {
				return rs.getString(1);
			}
		}
		catch(SQLException ex) {
			System.err.println("Error en descripcion del tipo de bache");
		}
		return "Bache";
	}
	
	public String toString() {
		dbase = Utilities.getConection();
		String sql = "Select mun.nombre ,sec.nombre, cal.nombre,seg.descripcion   from segmentos  as seg\r\n" + 
				"			inner join calles as cal on seg.id_calle = cal.id\r\n" + 
				"			inner join sectores as sec on seg.id_sector = sec.id\r\n" + 
				"			inner join municipios as mun on sec.id_municipio = mun.id\r\n" + 
				"			where seg.id = "+id_segmento+";";
		try {
			ResultSet rs = dbase.execSelect(sql);
			if(rs.next()) {
				return ""+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4);
			}
		}
		catch(SQLException ex) {
			System.err.println("Error en descripcion del bache");
		}
		return "Calle"; 
	}

	
	public static Bache getBache(int id_bache,Db dbase) throws SQLException {
		Bache bache = new Bache();
		//dbase = Utilities.getConection();
		String query = "select fecha_registro,id_segmento,entaponamiento,cant_servicios_afec,fecha_reparacion,id_tipo_bache,photo_name, peligrosidad, tam_bache \r\n" + 
				"from baches where id ="+id_bache;
		ResultSet rs = dbase.execSelect(query);
		if(rs.next()) {
			bache.setId(id_bache);
			bache.setFecha_registro(rs.getDate(1));
			bache.setId_segmento(rs.getInt(2));
			bache.setEntaponamiento(rs.getBoolean(3));
			bache.setCant_servicios_afec(rs.getInt(4));
			bache.setFecha_reparacion(rs.getDate(5));
			bache.setId_tipo_bache(rs.getInt(6));
			bache.setPhoto_name(rs.getString(7));
			bache.setPeligrosidad(rs.getInt(8));
			bache.setTam_bache(rs.getInt(9));
		}
		
		return bache;
	}
	
	public static float calcularPuntaje(int id_bache,Db dbase) throws SQLException {//Sumarle la peligrosidad
		float puntaje = 0.0f;
		
		String query = "select mun.puntaje,sec.puntaje,seg.puntaje,tma.puntaje,cal.puntaje,tca.puntaje,tba.puntaje from Municipios as mun \r\n" + 
				"inner join sectores as sec on sec.id_municipio = mun.id\r\n" + 
				"inner join segmentos as seg on seg.id_sector = sec.id\r\n" + 
				"inner join tipo_material as tma on seg.id_tipo_material = tma.id\r\n" + 
				"inner join calles as cal on seg.id_calle = cal.id\r\n" + 
				"inner join tipo_calle as tca on cal.id_tipo_calle = tca.id\r\n" + 
				"inner join baches as bac on bac.id_segmento = seg.id\r\n" + 
				"inner join tipo_bache tba on bac.id_tipo_bache = tba.id\r\n" + 
				"where bac.id ="+id_bache;
		ResultSet rs = dbase.execSelect(query);
		rs.next();
		puntaje += rs.getFloat(1);
		puntaje += rs.getFloat(2);
		puntaje += rs.getFloat(3);
		puntaje += rs.getFloat(4);
		puntaje += rs.getFloat(5);
		puntaje += rs.getFloat(7);
		
		Bache bache = getBache(id_bache, dbase);
		
		
		puntaje += Daño.countDaños(id_bache, dbase)*5.0;
		
		if(bache.isEntaponamiento())
			puntaje += 11.0;
		
		puntaje += bache.getCant_servicios_afec()*7.0;
		
		Interval interval = new Interval(bache.getFecha_registro().getTime(), new Date().getTime());
		long days = interval.toDuration().getStandardDays();
		puntaje += days *5.5; 
		
		puntaje += bache.getPeligrosidad()*7.36;
		
		
		return puntaje;
		  
	}
	
}
