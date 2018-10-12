package es.gaitan.topografia.beans;

import java.math.BigDecimal;

public class Punto3D extends Punto2D {

	private static final long serialVersionUID = 4193150332407928359L;
	
	private BigDecimal coordZ;

	public Punto3D() {
		super();
	}

	
	
	/*******************************************/
	/**          GETTER AND SETTER            **/
	/*******************************************/
	
	public BigDecimal getCoordZ() {
		return coordZ;
	}
	public void setCoordZ(BigDecimal coordZ) {
		this.coordZ = coordZ;
	}

}
