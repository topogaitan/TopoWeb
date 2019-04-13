package es.gaitan.topografia.beans;

import java.math.BigDecimal;

/**
 *	Clase de observaciones para la intersección directa
 */
public class ObsIntersDirecta extends ObsGeneral {

	private static final long serialVersionUID = -6037032831412479606L;
	
	private BigDecimal distancia;
	
	public ObsIntersDirecta() {
		super();
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
