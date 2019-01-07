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

public class EstadilloObsIntersDirecta implements Estadillo, Serializable {
	
	private static final long serialVersionUID = -8997122079393118600L;
	
	private static final Logger logger = Logger.getLogger(EstadilloObsIntersDirecta.class);
	
	private List<ObsIntersDirecta> listObsIntersDirecta;
	
	public EstadilloObsIntersDirecta(){
		listObsIntersDirecta = new ArrayList<ObsIntersDirecta>();
	}
	
	@Override
	public boolean anadirObservacion(String linea) {
		logger.debug(linea);
		
		boolean blReturn = true;
        String separador = "\t";
        String[] parametros;
        
        try {
	        parametros = linea.split(separador, 6);
	
	        ObsIntersDirecta obs = new ObsIntersDirecta();
	        obs.setIdEstacion(parametros[0]);
	        obs.setIdVisado(parametros[1]);
	        obs.setlH(new BigDecimal(parametros[2]));
	        obs.setlV(new BigDecimal(parametros[3]));
	        obs.setDistancia(new BigDecimal(parametros[4]));
	        obs.setCodVisual(parametros[5]);
	        
	        this.listObsIntersDirecta.add(obs);
	        
        } catch(Exception exc) {
        	this.limpiarEstadillo();
        	blReturn = false;
        	logger.error("Error al insertar linea del fichero a la lista de observaciones", exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.ATENCION, Constantes.FORMATO_INCORRECTO));
        }
        return blReturn;
	}
	
	
	@Override
	public void limpiarEstadillo() {
		this.listObsIntersDirecta.clear();
	}
	
	@Override
	public int tamanioNubePuntos() {
		return this.listObsIntersDirecta.size();
	}
	
	public ObsIntersDirecta getPunto(int index) {
		return listObsIntersDirecta.get(index);
	}
	
	/*******************************************/
	/**          GETTER AND SETTER            **/
	/*******************************************/
	
	public List<ObsIntersDirecta> getListObsIntersDirecta() {
		return listObsIntersDirecta;
	}
	public void setListObsIntersDirecta(List<ObsIntersDirecta> listObsIntersDirecta) {
		this.listObsIntersDirecta = listObsIntersDirecta;
	}

}
