package es.gaitan.topografia.beans;

import java.math.BigDecimal;

public class ObsIntersDirecta extends ObsGenerales {

	private static final long serialVersionUID = -6037032831412479606L;
	
	private BigDecimal distancia;
	
	public ObsIntersDirecta() {
		
	}


	
	/*******************************************/
	/**          GETTER AND SETTER            **/
	/*******************************************/

	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

	
}
