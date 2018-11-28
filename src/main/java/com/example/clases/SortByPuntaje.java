package com.example.clases;

import java.util.Comparator;

import com.example.entidades.Bache;

public class SortByPuntaje implements Comparator<Bache>  {

	@Override
	public int compare(Bache o1, Bache o2) {
			return Float.compare(o1.getPuntaje() , o2.getPuntaje());
		
	}

}
