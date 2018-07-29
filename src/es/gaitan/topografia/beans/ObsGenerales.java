package es.gaitan.topografia.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class ObsGenerales implements Serializable{
	
	private static final long serialVersionUID = 5144653986606702307L;

	private String idEstacion;
	
	private String idVisado;
	
	private BigDecimal lH;
	
	private BigDecimal lV;
	
	private String nombreEstacion;
	
	private String codVisual;
	
	public ObsGenerales(){
		
	}

	
	/*******************************************/
	/**          GETTER AND SETTER            **/
	/*******************************************/

	public String getIdEstacion() {
		return idEstacion;
	}

	public void setIdEstacion(String idEstacion) {
		this.idEstacion = idEstacion;
	}

	public String getIdVisado() {
		return idVisado;
	}

	public void setIdVisado(String idVisado) {
		this.idVisado = idVisado;
	}

	public BigDecimal getlH() {
		return lH;
	}

	public void setlH(BigDecimal lH) {
		this.lH = lH;
	}

	public BigDecimal getlV() {
		return lV;
	}

	public void setlV(BigDecimal lV) {
		this.lV = lV;
	}

	public String getNombreEstacion() {
		return nombreEstacion;
	}

	public void setNombreEstacion(String nombreEstacion) {
		this.nombreEstacion = nombreEstacion;
	}

	public String getCodVisual() {
		return codVisual;
	}

	public void setCodVisual(String codVisual) {
		this.codVisual = codVisual;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ObsGenerales [idEstacion=");
		builder.append(idEstacion);
		builder.append(", idVisado=");
		builder.append(idVisado);
		builder.append(", lH=");
		builder.append(lH);
		builder.append(", lV=");
		builder.append(lV);
		builder.append(", nombreEstacion=");
		builder.append(nombreEstacion);
		builder.append(", codVisual=");
		builder.append(codVisual);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
