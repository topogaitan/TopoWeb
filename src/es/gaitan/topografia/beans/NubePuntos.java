package es.gaitan.topografia.beans;

import java.util.ArrayList;
import java.util.List;

public class NubePuntos {
	
	List<Punto3D> listPuntos;

	public NubePuntos() {
		super();
		listPuntos = new ArrayList<Punto3D>();
	}

	
	
	/*******************************************/
	/**          GETTER AND SETTER            **/
	/*******************************************/

	public List<Punto3D> getListPuntos() {
		return listPuntos;
	}
	public void setListPuntos(List<Punto3D> listPuntos) {
		this.listPuntos = listPuntos;
	}
	
}
