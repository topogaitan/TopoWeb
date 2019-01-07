package es.gaitan.topografia.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import es.gaitan.topografia.constants.Constantes;

public class NubePuntos implements Serializable {
	
	private static final long serialVersionUID = 8208789340900626741L;
	
	private static final Logger logger = Logger.getLogger(NubePuntos.class);
	
	private List<Punto3D> listPuntos;

	public NubePuntos() {
		super();
		listPuntos = new ArrayList<Punto3D>();
	}

	public boolean anadirPunto(String linea) {
		logger.debug(linea);
		
		boolean blReturn = true;
        String separador = "\t";
        String[] parametros;
        
        try {
	        parametros = linea.split(separador, 6);
	
	        
	        // TODO Deberían de ser los campos obligatorios sino fallará el cálculo
	        Punto3D pto = new Punto3D();
	        
	        if (!parametros[0].isEmpty()) {
	        	pto.setId(parametros[0]);
			}
	        
	        if (!parametros[1].isEmpty()) {
	        	pto.setCoordX(new BigDecimal(parametros[1]));
			}
	        
	        if (!parametros[2].isEmpty()) {
	        	pto.setCoordY(new BigDecimal(parametros[2]));
			}
	        
	        if (!parametros[3].isEmpty()) {
	        	pto.setCoordZ(new BigDecimal(parametros[3]));
			}
	        
	        if (!parametros[4].isEmpty()) {
	        	pto.setCodigo(parametros[4]);
			}
	        
	        this.listPuntos.add(pto);
	        
        } catch(Exception exc) {
        	this.listPuntos.clear();
        	blReturn = false;
        	logger.error("Error al agregar una linea del fichero a la lista de puntos", exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.ATENCION, Constantes.FORMATO_INCORRECTO));
        }
        return blReturn;
	}
	
	public void limpiarNubePuntos() {
		this.listPuntos.clear();
	}
	
	public int tamanioNubePuntos() {
		return this.listPuntos.size();
	}
	
	public Punto3D getPunto(int index) {
		return listPuntos.get(index);
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
