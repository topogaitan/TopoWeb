package es.gaitan.topografia.beans;

import java.math.BigDecimal;

public class Punto3D extends Punto2D {

	private static final long serialVersionUID = 4193150332407928359L;
	
	private BigDecimal coordZ;

	public Punto3D() {
		super();
	}

	
	public BigDecimal distancia(Punto3D p2) {
        BigDecimal Ax = p2.getCoordX().subtract(this.getCoordX());
        BigDecimal Ay = p2.getCoordY().subtract(this.getCoordY());
        BigDecimal Az = p2.getCoordZ().subtract(this.getCoordZ());
        BigDecimal dist = BigDecimal.valueOf(Math.sqrt(Ax.pow(2).add(Ay.pow(2)).add(Az.pow(2)).doubleValue()));
        
        return dist;

    }
	
	public double Acimut(Punto3D p2) {
        double Ax;
        double Ay;
        double acimut;
        Ax = p2.X - this.X; //con this llamamos a métodos, objetos y todo
        Ay = p2.Y - this.Y;

        if (Ax == 0 && Ay == 0) 
            return(-1); //solución provisional: que devuelva -1
        acimut = Math.Round(Math.Atan2(Ax, Ay) * 200 / Math.PI, 4); //con esto lo dejamos en cente
        if (Ax < 0) 
            return (acimut + 400); // si Ax es menor que cero sumamos 400 al acimut
        
        return (acimut); //para que devuelva el resto de valores de los acimutes
    }

    public static double Acimut(Punto3D p1, Punto3D p2) {
        return p1.Acimut(p2);
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
