package com.example.clases;

import java.sql.Connection;
import java.sql.SQLException;

import com.example.database.Db;

public class Utilities {
	private static Connection cnn = null;
	private static Db dbase = null; 
	public static Db getConection(){
        dbase = new Db(true,"Bacheo","postgres","letmein");
        cnn = dbase.getConnection();
        return dbase;
    }
	
	public static Db getTransConection(){
        Db dbase = new Db(false,"Bacheo","postgres","letmein");
        return dbase;
    }
	
	public static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
	


	public static String getFileExtension(String Pathfile) {
	    int lastIndexOf = Pathfile.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return ""; 
	    }
	    return Pathfile.substring(lastIndexOf);
	}


}
