package es.gaitan.topografia.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class Punto2D implements Serializable {

	private static final long serialVersionUID = 400873602651185848L;
	
	private String id;
	private BigDecimal coordX;
	private BigDecimal coordY;
	private String codigo;
	
	public Punto2D() {
		super();
	}
	
	public BigDecimal acimut(BigDecimal lH, BigDecimal desorienta) {
		BigDecimal acimut;
        acimut = desorienta.add(lH);
        
        if (acimut.doubleValue() > 400) {
        	acimut.subtract(new BigDecimal(400));
        }
        
        return acimut;
    }
	
    public BigDecimal desorientacion(BigDecimal lH, BigDecimal acimut) {
    	BigDecimal desorientacion = acimut.subtract(lH);
    	
        if (desorientacion.doubleValue() < 0) {
        	desorientacion = desorientacion.add(new BigDecimal(400));
        }

        return desorientacion;
    }
	
	
	
	/*******************************************/
	/**          GETTER AND SETTER            **/
	/*******************************************/

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getCoordX() {
		return coordX;
	}
	public void setCoordX(BigDecimal coordX) {
		this.coordX = coordX;
	}
	public BigDecimal getCoordY() {
		return coordY;
	}
	public void setCoordY(BigDecimal coordY) {
		this.coordY = coordY;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Punto2D [id=");
		builder.append(id);
		builder.append(", coordX=");
		builder.append(coordX);
		builder.append(", coordY=");
		builder.append(coordY);
		builder.append(", codigo=");
		builder.append(codigo);
		builder.append("]");
		return builder.toString();
	}
	
}
