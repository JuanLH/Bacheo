package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Db {
	public static Connection connection = null;
	
	  public Db(boolean autoCommit,String db,String user,String pass)   {
	        String url = "jdbc:postgresql://localhost:5434/"+db;
	        
	        try { 
	            Class.forName("org.postgresql.Driver");
	            connection=DriverManager.getConnection(url,user,pass);
	            connection.setAutoCommit(autoCommit);
	        } catch (SQLException exc){
	            //throw new SQLException(exc);
	            System.err.println("** Error de Base de datos 2**\n"+exc.getMessage());
	        } catch (ClassNotFoundException ex) {
	            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        
	    }
	  
	  public Connection getConnection(){
	       return connection;
	   }
	  
	  public ResultSet execSelect(String sql) throws SQLException
	    {   
	         try 
	         {
	            Statement comando = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	            ResultSet rs = comando.executeQuery(sql);
	            return rs;
	        } catch (SQLException ex) {
	           // Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
	            throw new SQLException(ex);
	            //System.err.println("** Error de Base de datos en el select **\n"+ex.getMessage());
	            //return null;
	        }
	    }
	        
	    public void executeQuery(String query) throws SQLException
	       {
	        try {
	            Statement stmt = connection.createStatement();
	            stmt.executeUpdate(query);
	        } catch (SQLException ex) {
	             throw new SQLException(ex);
	           // Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
	           // System.err.println("** Error de Base de datos **\n"+ex.getMessage());
	        }
	    }

	    public void CerrarConexion() {
	        try {

	            connection.close();
	            connection = null;
	        } catch (SQLException ex) {
	            //Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	    
	    public  String comilla(String st)
	    {
	       return "'"+st+"'"; 
	    }
	  
	  
}
