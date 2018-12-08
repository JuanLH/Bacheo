package com.example.entidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.clases.BrigadaList;
import com.example.clases.Utilities;
import com.example.database.Db;

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
	
	public static ArrayList<Brigada> asignarBrigadas(ArrayList<Bache> bachesPorPuntacion) throws SQLException{
		Db dbase = Utilities.getConection();
		
		BrigadaList brigadas = new BrigadaList();
		Calendar fecha ;
		limpiarAsignaciones(dbase);
		
		for(Bache ba:bachesPorPuntacion) {
			Brigada bri=null;
			int intentos_busqueda = 0;
			fecha = Calendar.getInstance();
			
			while(bri == null && intentos_busqueda<=2) {
				
				bri = Brigada.buscarBrigada(ba, fecha,dbase);	
				if(bri != null) {
					bri.bachesAsignados.add(ba);
					//insertar asignacion aqui
					insertAsignacion(bri.getId(), ba.getId(), fecha, dbase);
					bri.bachesAsignados.get(0).setFecha_asignacion(fecha.getTime());
					brigadas.add(bri);
				}
				else{
					System.err.println("Brigada nula");
					System.err.println("-------------------------Retorno null se le suma un dia + 1");
					fecha.add(Calendar.DAY_OF_MONTH, 1);
					intentos_busqueda++;
				}
			}
		}
		dbase.CerrarConexion();//se cierra la conexion
		return brigadas;
	}
	
	public static ArrayList<Bache> getBachesAsignado(Brigada brigada,Calendar fecha,Db dbase) throws SQLException{
		ArrayList<Bache> list = new ArrayList<>();
		//Db dbase = Utilities.getConection();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String query = "SELECT id, id_brigada, id_bache, fecha, estado\r\n" + 
				"  FROM public.asignaciones \r\n" + 
				"  where id_brigada = "+brigada.getId()+" and fecha = '"+format1.format(fecha.getTime())+"';";
		
		System.out.println("------------->Query para buscar asignaciones de una brigada en una fecha especifica \n"+query);
		
		ResultSet rs = dbase.execSelect(query);
		while(rs.next()) {
			Bache bache;
			bache = Bache.getBache(rs.getInt(3), dbase);
			list.add(bache);
		}
		return list;
	}

	
	
	/*Este metodo resetea todas las tablas que tienen que ver con las asignaciones de
	 * Brigada, habilita el personal y equipos para nuevas asignaciones
	 * borra las brigadas que esten en la tabla y los recursos asignados y
	 * Borra la tabla de asignaciones*/
	private static void limpiarAsignaciones(Db dbase) throws SQLException {
		//Esto es para borrar las asignaciones que no se han comenzado a trabajar
		String borrarAsignaciones = "DELETE FROM public.asignaciones WHERE  estado = 0;";
		dbase.executeQuery(borrarAsignaciones);
		ArrayList<Brigada> brigadas = Brigada.getBrigadaPorEstado(0,dbase);//Obtengo las brigadas que no han comenzado a trabajar
		System.out.println("Existen "+brigadas.size()+" Brigadas");
		for(Brigada bri:brigadas) {
			ArrayList<RecursosBrigada> recursosB = RecursosBrigada.getRecursos(bri.getId(),dbase);//Recursos que tiene cada brigada
			
			System.out.println("La brigada "+bri.getNombre()+" Posee "+recursosB.size()+" recursos");
			
			for(RecursosBrigada recurso : recursosB) {
				if(recurso.getId_tipo_recurso() == 1) {//Personal
					Personal.setPersonalDisponible(recurso.getId_recurso(),dbase);
				}
				else {//Equipos
					Equipo.setEquiposDisponibles(recurso.getId_recurso(),dbase);
				}
			}
			//BorrarRecursos
			RecursosBrigada.borrarRecursos(bri.getId(),dbase);
		}
		//BorrarBrigadas
		Brigada.borrarBrigadas(brigadas,dbase);
		
	}
	
	private static void insertAsignacion(int id_brigada,int id_bache,Calendar fecha,Db dbase) throws SQLException {
		dbase = Utilities.getConection();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		System.err.println("Se inserto una asignacion ========="+format1.format(fecha.getTime()));
		String sql = "INSERT INTO public.asignaciones(id_brigada, id_bache, fecha, estado)\r\n" + 
				"    VALUES (?, ?, ?, ?);";
		PreparedStatement p = dbase.getConnection().prepareStatement(sql);
		p.setInt(1, id_brigada);
		p.setInt(2, id_bache);
		p.setDate(3, new java.sql.Date(fecha.getTime().getTime()));
		p.setInt(4, 0);//Estado 0 porque no han comenzado a trabajar
		p.executeUpdate();
		
	}
}
