package es.gaitan.topografia.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import es.gaitan.topografia.constants.Constantes;
import es.gaitan.topografia.interfaces.Estadillo;

/**
 *	Clase dedicada al estadillo para las observaciones de la intersección inversa
 */
public class EstadilloObsIntersInversa implements Estadillo, Serializable {
	
	private static final long serialVersionUID = -8997122079393118600L;
	
	private static final Logger logger = Logger.getLogger(EstadilloObsIntersInversa.class);
	
	private List<ObsIntersInversa> listObsIntersInversa;
	
	public EstadilloObsIntersInversa(){
		listObsIntersInversa = new ArrayList<ObsIntersInversa>();
	}
	
	/**
	 * Añade una linea del fichero de observaciones pasada como parámetro para incluirla 
	 * en la lista de observaciones
	 */
	@Override
	public boolean anadirObservacion(String linea) {
		logger.debug(linea);
		
		boolean blReturn = true;
        String separador = "\t";
        String[] parametros;
        
        try {
	        parametros = linea.split(separador, 6);
	
	        ObsIntersInversa obs = new ObsIntersInversa();
	        
	        if (!parametros[0].isEmpty()) {
	        	obs.setIdEstacion(parametros[0]);
			}
	        if (!parametros[1].isEmpty()) {
	        	obs.setIdVisado(parametros[1]);
			}
	        if (!parametros[2].isEmpty()) {
	        	obs.setlH(new BigDecimal(parametros[2]));
			}
	        if (!parametros[3].isEmpty()) {
	        	obs.setlV(new BigDecimal(parametros[3]));
			}
//	        if (!parametros[4].isEmpty()) {
//	        	obs.setDistancia(new BigDecimal(parametros[4]));
//			}
	        if (!parametros[5].isEmpty()) {
	        	obs.setCodVisual(parametros[5]);
			}
	        
	        this.listObsIntersInversa.add(obs);
	        
        }catch(Exception exc) {
        	this.limpiarEstadillo();
        	blReturn = false;
        	logger.error("Error al insertar linea del fichero a la lista de observaciones", exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_FORMATO_INCORRECTO));
        }
        return blReturn;
	}
	
	/**
	 * Deja la lista de observaciones vacia
	 */
	@Override
	public void limpiarEstadillo() {
		this.listObsIntersInversa.clear();
	}
	
	/**
	 * Devuelve en formato de tipo primitivo int, el tamaño de la lista de observaciones
	 */
	@Override
	public int tamanioNubePuntos() {
		return this.listObsIntersInversa.size();
	}
	
	/**
	 * Devuelve la observación que se encuentra en la lista de observaciones cuya posición sea la pasada por parámetro
	 */
	public ObsIntersInversa getPunto(int index) {
		return listObsIntersInversa.get(index);
	}
	
	
	/*******************************************/
	/**          GETTER AND SETTER            **/
	/*******************************************/
	
	public List<ObsIntersInversa> getListObsIntersInversa() {
		return listObsIntersInversa;
	}
	public void setListObsIntersInversa(List<ObsIntersInversa> listObsIntersInversa) {
		this.listObsIntersInversa = listObsIntersInversa;
	}

}
