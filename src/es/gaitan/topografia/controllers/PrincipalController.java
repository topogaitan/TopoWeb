package es.gaitan.topografia.controllers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import es.gaitan.topografia.beans.EstadilloObsIntersDirecta;
import es.gaitan.topografia.beans.EstadilloObsIntersInversa;
import es.gaitan.topografia.beans.NubePuntos;
import es.gaitan.topografia.beans.Punto2D;
import es.gaitan.topografia.constants.Constantes;

/**
 * @author RGAITAN
 */
@ManagedBean(name="principalController")
@SessionScoped
public class PrincipalController implements Serializable {

	// TODO GENERAR DOCUMENTACION A PARTIR DE LA INFORMACION DEL CODIGO
	
	
	private static final long serialVersionUID = 5215987609794388569L;
	private static final Logger logger = Logger.getLogger(PrincipalController.class);
	
	/** LISTAS DE PUNTOS Y OBSERVACIONES **/
	private NubePuntos nubePuntos;
	private EstadilloObsIntersDirecta estadilloObsIntersDirecta;
	private EstadilloObsIntersInversa estadilloObsIntersInversa;
	
	private MapModel polygonModel;
	private Punto2D polygonCentroide;
	
	
	/** BOOLEANOS PARA CONTROL DE LA VISTA **/
	private boolean esIntersecDirecta;
	private boolean esIntersecInversa;
	private boolean disabledButtonsToolbar;
	private boolean renderedGoogleMaps;

	@PostConstruct
	public void reset() {
		logger.info(Constantes.INI_METODO);
		
		nubePuntos = new NubePuntos();
		estadilloObsIntersDirecta = new EstadilloObsIntersDirecta();
		estadilloObsIntersInversa = new EstadilloObsIntersInversa();
		polygonModel = new DefaultMapModel();
		polygonCentroide = new Punto2D();
		
		esIntersecDirecta = false;
		esIntersecInversa = false;
		
		disabledButtonsToolbar = true;
		renderedGoogleMaps = false;
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public void activarIntersecDirecta() {
		logger.info(Constantes.INI_METODO);
		
		this.reset();
		esIntersecDirecta = true;
		disabledButtonsToolbar = false;
		renderedGoogleMaps = true;
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public void activarIntersecInversa() {
		logger.info(Constantes.INI_METODO);
		
		this.reset();
		esIntersecInversa = true;
		disabledButtonsToolbar = false;
		renderedGoogleMaps = true;
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public String doAccionBotonNuevo() {
		logger.info(Constantes.INI_METODO);
		
		this.reset();
		disabledButtonsToolbar = true;
		renderedGoogleMaps = false;
		
		logger.info(Constantes.FIN_METODO);
		return "";
	}
	
	public void cargarFichero(FileUploadEvent event) {
		logger.info(Constantes.INI_METODO);
		
		UploadedFile uploadFile = event.getFile();
		String extensionFich = FilenameUtils.getExtension(uploadFile.getFileName());
		boolean esFicheroObservaciones = extensionFich.equalsIgnoreCase("obs") ? true : false;
		boolean esFicheroCoordenadas = extensionFich.equalsIgnoreCase("pto") ? true : false;
		
		logger.info("esFicheroObservaciones --> " + esFicheroObservaciones + ", esFicheroCoordenadas --> " + esFicheroCoordenadas);
		if (esFicheroObservaciones) {
			cargaFicheroObservaciones(uploadFile);
		} else if (esFicheroCoordenadas) {
			cargaFicheroCoordenadas(uploadFile);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					Constantes.ATENCION, Constantes.EXTENSION_FICHERO_INCORRECTA));
		}
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public String doObtenerCalculos() {
		logger.info(Constantes.INI_METODO);
		
		// TODO Realizar toda la logica para obtener los calculos que se piden
		
		List<LatLng> listPuntosLatLng = new ArrayList<LatLng>();
		
		//Shared coordinates
		LatLng coord1 = new LatLng(40.602057, -3.707517);
		LatLng coord2 = new LatLng(40.609630, -3.725441);
		LatLng coord3 = new LatLng(40.616663, -3.684893);
		
		listPuntosLatLng.add(coord1);
		listPuntosLatLng.add(coord2);
		listPuntosLatLng.add(coord3);
		
		//Polygon
		Polygon polygon = new Polygon();
		polygon.getPaths().add(coord1);
		polygon.getPaths().add(coord2);
		polygon.getPaths().add(coord3);
		
		polygon.setStrokeColor("#FF9900");
		polygon.setFillColor("#FF9900");
		polygon.setStrokeOpacity(0.7);
		polygon.setFillOpacity(0.7);
		
		polygonModel.addOverlay(polygon);
		
		obtenerCoordCentroidePoligono(listPuntosLatLng);
		
		logger.info(Constantes.FIN_METODO);
		return "";
	}
	
	
	/*******************************************/
	/**          METODOS PRIVADOS             **/
	/*******************************************/
	
	private void cargaFicheroObservaciones(UploadedFile uploadFile) {
		logger.info(Constantes.INI_METODO);
		
		try {
			String rutaCompleta = obtenerRutaFicheroCompleta(uploadFile);
        
            BufferedReader br = obtenerBufferedReader(uploadFile, rutaCompleta);
            
            String linea;
            boolean estaAgregada = true;
            
            if (esIntersecDirecta) {
            	estadilloObsIntersDirecta.limpiarEstadillo();
	            while ((linea = br.readLine()) != null && estaAgregada) {
		            	estaAgregada = estadilloObsIntersDirecta.anadirObservacion(linea);
	            }
            } else if (esIntersecInversa) {
            	estadilloObsIntersInversa.limpiarEstadillo();
        		while ((linea = br.readLine()) != null && estaAgregada) {
	            	estaAgregada = estadilloObsIntersInversa.anadirObservacion(linea);
        		}
            }
            
            // Se cierra la conexion del tunel de datos
            br.close();
            logger.info(Constantes.FIN_METODO);
            
        } catch (Exception exc) {
        	if (esIntersecDirecta) {
            	estadilloObsIntersDirecta.limpiarEstadillo();
            } else if (esIntersecInversa) {
            	estadilloObsIntersInversa.limpiarEstadillo();
            }
        	logger.error("Error tipo Exception", exc);
        }
    }
	
	private void cargaFicheroCoordenadas(UploadedFile uploadFile) {
		logger.info(Constantes.INI_METODO);
		
		// Se limpia nubePuntos para que no se añadan las coordenadas de distintos ficheros
		nubePuntos.limpiarNubePuntos();
		
		// TODO ¿Es necesario discrimir la nubePuntos dependiendo del tipo de interseccion?
		
		try {
			String rutaCompleta = obtenerRutaFicheroCompleta(uploadFile);
        
            BufferedReader br = obtenerBufferedReader(uploadFile, rutaCompleta);
            
            String linea;
            boolean estaAgregado = true;
            
            if (esIntersecDirecta) {
	            while ((linea = br.readLine()) != null && estaAgregado) {
	            	estaAgregado = nubePuntos.anadirPunto(linea);
	            }
            } else if (esIntersecInversa) {
        		while ((linea = br.readLine()) != null && estaAgregado) {
        			estaAgregado = nubePuntos.anadirPunto(linea);
        		}
            }
            
            // Se cierra la conexion del tunel de datos
            br.close();
            logger.info(Constantes.FIN_METODO);
            
        } catch (Exception exc) {
        	nubePuntos.limpiarNubePuntos();
        	logger.error("Error tipo Exception", exc);
        }
	}
	
	private String obtenerRutaFicheroCompleta(UploadedFile uploadFile) throws Exception {
		logger.info(Constantes.INI_METODO);
		String rutaCompleta = Constantes.CADENA_VACIA;
		
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String realPath = ec.getRealPath("fich/in/");
        rutaCompleta = realPath + uploadFile.getFileName();
        
        logger.info(rutaCompleta);
        logger.info(Constantes.FIN_METODO);
        
		return rutaCompleta;
	}
	
	private BufferedReader obtenerBufferedReader(UploadedFile uploadFile, String rutaCompleta) {
		logger.info(Constantes.INI_METODO);
		BufferedReader br = null;
		
		try {
			// Se transforma el objeto UploadedFile en un FileInputStream para poder ser tratado
			FileInputStream in = (FileInputStream) uploadFile.getInputstream();
	        FileOutputStream out = new FileOutputStream(rutaCompleta);
	        byte[] buffer = new byte[(int) uploadFile.getSize()];
	        int contador = 0;
	        
	        // Se almacena el fichero con la informacion en la ruta indicada
	        while ((contador = in.read(buffer)) != -1) {
	            out.write(buffer, 0, contador);
	        }
	        
	        // Se lee el fichero y se almacena en un objeto para su tratado
	        br = new BufferedReader(new FileReader(rutaCompleta));
	        
	        // Se cierran las conexiones de I/O de los ficheros
            in.close();
            out.close();
            logger.info(Constantes.FIN_METODO);
            
		} catch (FileNotFoundException fnfExc) {
			logger.error("Error tipo FileNotFoundException --> Fichero no encontrado", fnfExc);
		} catch (IOException ioEcx) {
			logger.error("Error tipo IOException --> El fichero no se ha cargado correctamente", ioEcx);
		}  catch (Exception exc) {
        	logger.error("Error tipo Exception", exc);
        }

       return br;
	}
	
	private void obtenerCoordCentroidePoligono(List<LatLng> listPuntosLatLng) {
		logger.info(Constantes.INI_METODO);
		int numPuntos = listPuntosLatLng.size();
		double sumatorioLat = 0;
		double sumatorioLng = 0;
		double centroideLat = 0;
		double centroideLng = 0;
		
		for (LatLng latLng : listPuntosLatLng) {
			sumatorioLat += latLng.getLat();
			sumatorioLng += latLng.getLng();
		}
		
		centroideLat = sumatorioLat / numPuntos;
		centroideLng = sumatorioLng / numPuntos;
		
		// Centrar el mapa en funcion del centroide del poligono
		polygonCentroide.setCoordX(new BigDecimal(centroideLat));
		polygonCentroide.setCoordY(new BigDecimal(centroideLng));
		
		logger.info("Centroide latitud --> " + centroideLat);
		logger.info("Centroide longitud --> " + centroideLng);
		logger.info(Constantes.FIN_METODO);
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
	public MapModel getPolygonModel() {
		return polygonModel;
	}
	public void setPolygonModel(MapModel polygonModel) {
		this.polygonModel = polygonModel;
	}
	public Punto2D getPolygonCentroide() {
		return polygonCentroide;
	}
	public void setPolygonCentroide(Punto2D polygonCentroide) {
		this.polygonCentroide = polygonCentroide;
	}

}
