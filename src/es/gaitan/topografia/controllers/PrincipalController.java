package es.gaitan.topografia.controllers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import es.gaitan.topografia.beans.EstadilloObsIntersDirecta;
import es.gaitan.topografia.beans.EstadilloObsIntersInversa;
import es.gaitan.topografia.beans.NubePuntos;
import es.gaitan.topografia.constants.Constantes;

/**
 * @author RGAITAN
 */
@ManagedBean(name="principalController")
@SessionScoped
public class PrincipalController implements Serializable{

	private static final long serialVersionUID = 5215987609794388569L;
	private static final Logger logger = Logger.getLogger(PrincipalController.class);
	
	/** LISTAS DE PUNTOS Y OBSERVACIONES **/
	private NubePuntos nubePuntos;
	private EstadilloObsIntersDirecta estadilloObsIntersDirecta;
	private EstadilloObsIntersInversa estadilloObsIntersInversa;
	
	
	/** BOOLEANOS PARA CONTROL DE LA VISTA **/
	private boolean esIntersecDirecta;
	private boolean esIntersecInversa;
	private boolean disabledButtonsToolbar;
	private boolean renderedGoogleMaps;

	@PostConstruct
	public void reset(){
		logger.debug(Constantes.INI_METODO);
		
		nubePuntos = new NubePuntos();
		estadilloObsIntersDirecta = new EstadilloObsIntersDirecta();
		estadilloObsIntersInversa = new EstadilloObsIntersInversa();
		
		esIntersecDirecta = false;
		esIntersecInversa = false;
		
		disabledButtonsToolbar = true;
		renderedGoogleMaps = false;
	}
	
	public void activarIntersecDirecta(){
		logger.info(Constantes.INI_METODO);
		this.reset();
		esIntersecDirecta = true;
		disabledButtonsToolbar = false;
		renderedGoogleMaps = true;
	}
	
	public void activarIntersecInversa(){
		logger.info(Constantes.INI_METODO);
		this.reset();
		esIntersecInversa = true;
		disabledButtonsToolbar = false;
		renderedGoogleMaps = true;
	}
	
	public String accionBotonNuevo(){
		logger.info(Constantes.INI_METODO);
		this.reset();
		disabledButtonsToolbar = true;
		renderedGoogleMaps = false;
		return "";
	}
	
	public void cargaFicheroObservaciones(FileUploadEvent event) {
		logger.info(Constantes.INI_METODO);
		
		if(esIntersecDirecta) {
	        UploadedFile uploadFile = event.getFile();
	        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	        String realPath = ec.getRealPath("fich/in/");
	        String pathDefinition = realPath + uploadFile.getFileName();
	        logger.info(pathDefinition);
	        try {
	            FileInputStream in = (FileInputStream) uploadFile.getInputstream();
	            
	            // Se almacena el fichero con la informacion en la ruta indicada
	            FileOutputStream out = new FileOutputStream(pathDefinition);
	            byte[] buffer = new byte[(int) uploadFile.getSize()];
	            int contador = 0;
	            while ((contador = in.read(buffer)) != -1) {
	                out.write(buffer, 0, contador);
	            }
	
	            // Se lee el fichero y se almacena en un objeto para su tratado
	            BufferedReader br = new BufferedReader(new FileReader(pathDefinition));
	            String linea;
	            boolean estaAgregada = true;
	            while ((linea = br.readLine()) != null && estaAgregada) {
 	            	estaAgregada = estadilloObsIntersDirecta.anadirObservacion("INTERSEC_DIRECTA", linea);
	            }
	
	            in.close();
	            out.close();
	            br.close();
	        } catch (FileNotFoundException fnfExc) {
	        	logger.error("Fichero no encontrado");
	        	fnfExc.printStackTrace();
	        } catch (IOException ioEcx) {
	        	logger.error("El fichero no se ha cargado correctamente");
	        	ioEcx.printStackTrace();
	        }
        }
		if(esIntersecInversa) {
	        UploadedFile uploadFile = event.getFile();
	        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	        String realPath = ec.getRealPath("fich/in/");
	        String pathDefinition = realPath + uploadFile.getFileName();
	        logger.info(pathDefinition);
	        try {
	            FileInputStream in = (FileInputStream) uploadFile.getInputstream();
	            
	            // Se almacena el fichero con la informacion en la ruta indicada
	            FileOutputStream out = new FileOutputStream(pathDefinition);
	            byte[] buffer = new byte[(int) uploadFile.getSize()];
	            int contador = 0;
	            while ((contador = in.read(buffer)) != -1) {
	                out.write(buffer, 0, contador);
	            }
	
	            // Se lee el fichero y se almacena en un objeto para su tratado
	            BufferedReader br = new BufferedReader(new FileReader(pathDefinition));
	            String linea;
	            boolean estaAgregada = true;
	            while ((linea = br.readLine()) != null && estaAgregada) {
	            	estaAgregada = estadilloObsIntersInversa.anadirObservacion("INTERSEC_INVERSA", linea);
	            }
	
	            in.close();
	            out.close();
	            br.close();
	        } catch (FileNotFoundException fnfExc) {
	        	logger.error("Fichero no encontrado");
	        	fnfExc.printStackTrace();
	        } catch (IOException ioEcx) {
	        	logger.error("El fichero no se ha cargado correctamente");
	        	ioEcx.printStackTrace();
	        }
        }
    }
	
	

	/*******************************************/
	/**          GETTER AND SETTER            **/
	/*******************************************/
	
	public NubePuntos getNubePuntos() {
		return nubePuntos;
	}

	public void setNubePuntos(NubePuntos nubePuntos) {
		this.nubePuntos = nubePuntos;
	}
	
	public EstadilloObsIntersDirecta getEstadilloObsIntersDirecta() {
		return estadilloObsIntersDirecta;
	}

	public void setEstadilloObsIntersDirecta(EstadilloObsIntersDirecta estadilloObsIntersDirecta) {
		this.estadilloObsIntersDirecta = estadilloObsIntersDirecta;
	}
	
	public EstadilloObsIntersInversa getEstadilloObsIntersInversa() {
		return estadilloObsIntersInversa;
	}

	public void setEstadilloObsIntersInversa(EstadilloObsIntersInversa estadilloObsIntersInversa) {
		this.estadilloObsIntersInversa = estadilloObsIntersInversa;
	}

	public boolean isDisabledButtonsToolbar() {
		return disabledButtonsToolbar;
	}

	public void setDisabledButtonsToolbar(boolean disabledButtonsToolbar) {
		this.disabledButtonsToolbar = disabledButtonsToolbar;
	}

	public boolean isRenderedGoogleMaps() {
		return renderedGoogleMaps;
	}

	public void setRenderedGoogleMaps(boolean renderedGoogleMaps) {
		this.renderedGoogleMaps = renderedGoogleMaps;
	}

	public boolean isEsIntersecDirecta() {
		return esIntersecDirecta;
	}

	public void setEsIntersecDirecta(boolean esIntersecDirecta) {
		this.esIntersecDirecta = esIntersecDirecta;
	}

	public boolean isEsIntersecInversa() {
		return esIntersecInversa;
	}

	public void setEsIntersecInversa(boolean esIntersecInversa) {
		this.esIntersecInversa = esIntersecInversa;
	}
	
	

}
