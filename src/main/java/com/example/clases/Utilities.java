package com.example.clases;

import java.sql.Connection;

import com.example.database.Db;

public class Utilities {
	private static Connection cnn = null;
	
	public static Db getConection(){
        Db dbase = new Db("Bacheo","postgres","letmein");
        cnn = dbase.getConnection();
        return dbase;
    }
	
	public static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
	


	public static String getFileExtension(String Pathfile) {
	    //String name = file.getName();
	    int lastIndexOf = Pathfile.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return ""; // empty extension
	    }
	    return Pathfile.substring(lastIndexOf);
	}


}
