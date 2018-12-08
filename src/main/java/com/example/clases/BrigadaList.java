package com.example.clases;

import java.util.ArrayList;
import java.util.List;

import com.example.entidades.Brigada;

public class BrigadaList extends ArrayList<Brigada> {

	public boolean add(Brigada bri) {
		
		if(this.isEmpty()) {
			super.add(bri);
			return true;
		}
		else {
			for(Brigada b:this) {
				if(b.getId() == bri.getId()) {
					b.bachesAsignados.add(bri.bachesAsignados.get(0));
					return true;
				}
				
			}
			super.add(bri);
			return true;
		}
	}
}
