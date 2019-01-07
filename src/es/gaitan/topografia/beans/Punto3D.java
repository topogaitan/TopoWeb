package es.gaitan.topografia.beans;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Punto3D extends Punto2D {

	private static final long serialVersionUID = 4193150332407928359L;
	
	private BigDecimal coordZ;

	public Punto3D() {
		super();
	}

	
	public BigDecimal distancia(Punto3D p2) {
        BigDecimal incX = p2.getCoordX().subtract(this.getCoordX());
        BigDecimal incY = p2.getCoordY().subtract(this.getCoordY());
        BigDecimal incZ = p2.getCoordZ().subtract(this.getCoordZ());
        
        BigDecimal dist = BigDecimal.valueOf(Math.sqrt(incX.pow(2).add(incY.pow(2)).add(incZ.pow(2)).doubleValue()));
        
        return dist;

    }
	
	public BigDecimal acimut(Punto3D p2) {
		BigDecimal incX;
		BigDecimal incY;
		BigDecimal acimut;
		
		incX = p2.getCoordX().subtract(this.getCoordX());
		incY = p2.getCoordY().subtract(this.getCoordY());

        if (incX.equals(0) && incY.equals(0)) {
        	//solución provisional: que devuelva -1
        	return new BigDecimal(-1);
        }
        // Con este calculo se obtienen grados centesimales
        acimut = BigDecimal.valueOf(Math.atan2(incX.doubleValue(), incY.doubleValue()) * 200 / Math.PI);
        
        if (incX.doubleValue() < 0) {
        	// si incX es menor que cero se suma 400 al acimut
        	acimut = acimut.add(new BigDecimal(400));
        }
        
        acimut = acimut.setScale(4, RoundingMode.HALF_DOWN);
        
        return acimut;
    }

    public static BigDecimal acimut(Punto3D p1, Punto3D p2) {
        return p1.acimut(p2);
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
