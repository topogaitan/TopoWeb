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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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
import es.gaitan.topografia.beans.ObsIntersDirecta;
import es.gaitan.topografia.beans.Punto2D;
import es.gaitan.topografia.beans.Punto3D;
import es.gaitan.topografia.constants.Constantes;
import es.gaitan.topografia.utilities.Utilidades;

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
	private NubePuntos nubePuntosCalculados;
	
	/** Inicio Atributos Interseccion Directa **/
	private BigDecimal acimutDI;
	private BigDecimal acimutID;
	private BigDecimal acimutDV;
	private BigDecimal acimutIV;
	private Punto3D pEstD;
	private Punto3D pEstI;
	private Punto3D pCalDesdeD;
	private Punto3D pCalDesdeI;
	/** Fin Atributos Interseccion Directa **/
	
	
	private MapModel polygonModel;
	private Punto2D polygonCentroide;
	
	
	/** BOOLEANOS PARA CONTROL DE LA VISTA **/
	private boolean esIntersecDirectaAngular;
	private boolean esIntersecDirectaDistancias;
	private boolean esIntersecInversa;
	private boolean disabledButtonsToolbar;
	private boolean renderedCargaFichero;
	private boolean renderedGoogleMaps;
	private boolean mostrarResultados;

	@PostConstruct
	public void reset() {
		logger.info(Constantes.INI_METODO);
		
		nubePuntos = new NubePuntos();
		estadilloObsIntersDirecta = new EstadilloObsIntersDirecta();
		estadilloObsIntersInversa = new EstadilloObsIntersInversa();
		nubePuntosCalculados = new NubePuntos();
		polygonModel = new DefaultMapModel();
		polygonCentroide = new Punto2D();
		
		esIntersecDirectaAngular = false;
		esIntersecDirectaDistancias = false;
		esIntersecInversa = false;
		
		disabledButtonsToolbar = true;
		renderedCargaFichero = false;
		renderedGoogleMaps = false;
		mostrarResultados = false;
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public void activarIntersecDirectaAngular() {
		logger.info(Constantes.INI_METODO);
		
		this.reset();
		esIntersecDirectaAngular = true;
		disabledButtonsToolbar = false;
		renderedCargaFichero = true;
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public void activarIntersecDirectaDistancias() {
		logger.info(Constantes.INI_METODO);
		
		this.reset();
		esIntersecDirectaDistancias = true;
		disabledButtonsToolbar = false;
		renderedCargaFichero = true;
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public void activarIntersecInversa() {
		logger.info(Constantes.INI_METODO);
		
		this.reset();
		esIntersecInversa = true;
		disabledButtonsToolbar = false;
		renderedCargaFichero = true;
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public String doAccionBotonNuevo() {
		logger.info(Constantes.INI_METODO);
		
		this.reset();
		disabledButtonsToolbar = true;
		
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
		
		// TODO El codigo de cada punto y observacion debe llegar informado
		
		// Se calcula el ACIMUT
		// Se localizan las estaciones D e I mediante el codigo del punto
		for (int i = 0; i < nubePuntos.tamanioNubePuntos(); i++) {
            if ("D".equals(nubePuntos.getPunto(i).getCodigo())) {
                pEstD = nubePuntos.getPunto(i);
            } else {
                pEstI = nubePuntos.getPunto(i);
            }
		}

		acimutDI = pEstD.acimut(pEstI);
		acimutID = Utilidades.acimutReciproco(acimutDI);
		
		// Se calcula la DESORIENTACION con las observaciones
		BigDecimal desorEstD = BigDecimal.ZERO;
		BigDecimal desorEstI = BigDecimal.ZERO;
		
		int numeroObservaciones = estadilloObsIntersDirecta.tamanioNubePuntos();
		for (int i = 0; i < numeroObservaciones; i++) {
			ObsIntersDirecta observacion = estadilloObsIntersDirecta.getPunto(i);
		    if (observacion.getIdVisado().equals(pEstI.getId())) {
		        desorEstD = pEstD.desorientacion(observacion.getlH(), acimutDI);
		    }
		    if (observacion.getIdVisado().equals(pEstD.getId())) {
		        desorEstI = pEstI.desorientacion(observacion.getlH(), acimutID);
		    }
		}
		
		
//		BigDecimal base_EstDEstI = pEstD.distancia(pEstI);
		
		
		BigDecimal anguloD;
		BigDecimal anguloI;
		BigDecimal alpha;

		ObsIntersDirecta obsDI = new ObsIntersDirecta();
		ObsIntersDirecta obsDV = new ObsIntersDirecta();
		ObsIntersDirecta obsID = new ObsIntersDirecta();
		ObsIntersDirecta obsIV = new ObsIntersDirecta();
		
		for (int i = 0; i < numeroObservaciones; i++) {
			ObsIntersDirecta observacion = estadilloObsIntersDirecta.getPunto(i);
			
			if ("DI".equals(observacion.getCodVisual())) {
				obsDI.setIdEstacion(observacion.getIdEstacion());
				obsDI.setIdVisado(observacion.getIdVisado());
				obsDI.setlH(observacion.getlH());
				obsDI.setlV(observacion.getlV());
				obsDI.setCodVisual(observacion.getCodVisual());
				
			} else if ("DV".equals(observacion.getCodVisual())) {
				obsDV.setIdEstacion(observacion.getIdEstacion());
				obsDV.setIdVisado(observacion.getIdVisado());
				obsDV.setlH(observacion.getlH());
				obsDV.setlV(observacion.getlV());
				obsDV.setCodVisual(observacion.getCodVisual());
				
			} else if ("ID".equals(observacion.getCodVisual())) {
				obsID.setIdEstacion(observacion.getIdEstacion());
				obsID.setIdVisado(observacion.getIdVisado());
				obsID.setlH(observacion.getlH());
				obsID.setlV(observacion.getlV());
				obsID.setCodVisual(observacion.getCodVisual());
				
			} else if ("IV".equals(observacion.getCodVisual())) {
				obsIV.setIdEstacion(observacion.getIdEstacion());
				obsIV.setIdVisado(observacion.getIdVisado());
				obsIV.setlH(observacion.getlH());
				obsIV.setlV(observacion.getlV());
				obsIV.setCodVisual(observacion.getCodVisual());
			}
		}

		if (obsDI.getlH().compareTo(obsDV.getlH()) > 0) {
		    anguloD = obsDI.getlH().subtract(obsDV.getlH());
		} else {
		    anguloD = obsDV.getlH().subtract(obsDI.getlH());
		}
		
		if (obsIV.getlH().compareTo(obsID.getlH()) > 0) {
		    anguloI = obsIV.getlH().subtract(obsID.getlH());
		} else {
		    anguloI = obsID.getlH().subtract(obsIV.getlH());
		}
		
		alpha = Constantes.BIGDEC_200.subtract(anguloD).subtract(anguloI);
		
		BigDecimal comprobacion = alpha.add(anguloD).add(anguloI);
		
//		// Calcular los lados del triangulo
//		double ladoDV = (Math.Sin(Utiles.convertRadian(anguloI)) * baseEst) / (Math.Sin(Utiles.convertRadian(alpha)));
//		double ladoIV = (Math.Sin(Utiles.convertRadian(anguloD)) * baseEst) / (Math.Sin(Utiles.convertRadian(alpha)));
//		
//		double acimutDV = desorEst1 + obsDV.LH;
//		double acimutIV = desorEst2 + obsIV.LH;
//		
//		// Calcular los incrementos de coordenadas
//		double axD = ladoDV * Math.Sin(Utiles.convertRadian(acimutDV));
//		double ayD = ladoDV * Math.Cos(Utiles.convertRadian(acimutDV));
//		
//		double axI = ladoIV * Math.Sin(Utiles.convertRadian(acimutIV));
//		double ayI = ladoIV * Math.Cos(Utiles.convertRadian(acimutIV));
//		
//		// Calcular coordenadas de los puntos radiados
//		pVisD.X = Math.Round((pEst1.X + axD), 3);
//		pVisD.Y = Math.Round((pEst1.Y + ayD), 3);
//		
//		pVisI.X = Math.Round((pEst2.X + axI), 3);
//		pVisI.Y = Math.Round((pEst2.Y + ayI), 3);
//		
//		// Promedio del punto calculado
//		cPunto3D pVisProm = new cPunto3D();
//		pVisProm.ID = obsDV.vis;
//		pVisProm.X = Math.Round((pVisD.X + pVisI.X) / cConstantes.DOS, 3);
//		pVisProm.Y = Math.Round((pVisD.Y + pVisI.Y) / cConstantes.DOS, 3);
//		pVisProm.Z = 0;
//		
//		puntosCalculados.Añadir(pVisProm);
//		dgvCalCoordInters.Rows.Add(pVisProm.ID, pVisProm.X, pVisProm.Y, pVisProm.Z);
		
		
		
		
		
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
		polygon.setFillColor("#FFECC1");
		polygon.setStrokeOpacity(1);
		polygon.setFillOpacity(0.6);
		polygon.setStrokeWeight(2);
		
		polygonModel.addOverlay(polygon);
		
		obtenerCoordCentroidePoligono(listPuntosLatLng);
		renderedGoogleMaps = true;
		mostrarResultados = true;
		
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
            
            if (esIntersecDirectaAngular || esIntersecDirectaDistancias) {
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
        	if (esIntersecDirectaAngular || esIntersecDirectaDistancias) {
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
            
            if (esIntersecDirectaAngular || esIntersecDirectaDistancias) {
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
	public NubePuntos getNubePuntosCalculados() {
		return nubePuntosCalculados;
	}
	public void setNubePuntosCalculados(NubePuntos nubePuntosCalculados) {
		this.nubePuntosCalculados = nubePuntosCalculados;
	}
	public BigDecimal getAcimutDI() {
		return acimutDI;
	}
	public void setAcimutDI(BigDecimal acimutDI) {
		this.acimutDI = acimutDI;
	}
	public BigDecimal getAcimutID() {
		return acimutID;
	}
	public void setAcimutID(BigDecimal acimutID) {
		this.acimutID = acimutID;
	}
	public BigDecimal getAcimutDV() {
		return acimutDV;
	}
	public void setAcimutDV(BigDecimal acimutDV) {
		this.acimutDV = acimutDV;
	}
	public BigDecimal getAcimutIV() {
		return acimutIV;
	}
	public void setAcimutIV(BigDecimal acimutIV) {
		this.acimutIV = acimutIV;
	}
	public Punto3D getpEstD() {
		return pEstD;
	}
	public void setpEstD(Punto3D pEstD) {
		this.pEstD = pEstD;
	}
	public Punto3D getpEstI() {
		return pEstI;
	}
	public void setpEstI(Punto3D pEstI) {
		this.pEstI = pEstI;
	}
	public Punto3D getpCalDesdeD() {
		return pCalDesdeD;
	}
	public void setpCalDesdeD(Punto3D pCalDesdeD) {
		this.pCalDesdeD = pCalDesdeD;
	}
	public Punto3D getpCalDesdeI() {
		return pCalDesdeI;
	}
	public void setpCalDesdeI(Punto3D pCalDesdeI) {
		this.pCalDesdeI = pCalDesdeI;
	}
	public boolean isDisabledButtonsToolbar() {
		return disabledButtonsToolbar;
	}
	public void setDisabledButtonsToolbar(boolean disabledButtonsToolbar) {
		this.disabledButtonsToolbar = disabledButtonsToolbar;
	}
	public boolean isRenderedCargaFichero() {
		return renderedCargaFichero;
	}
	public void setRenderedCargaFichero(boolean renderedCargaFichero) {
		this.renderedCargaFichero = renderedCargaFichero;
	}
	public boolean isRenderedGoogleMaps() {
		return renderedGoogleMaps;
	}
	public void setRenderedGoogleMaps(boolean renderedGoogleMaps) {
		this.renderedGoogleMaps = renderedGoogleMaps;
	}
	public boolean isMostrarResultados() {
		return mostrarResultados;
	}
	public void setMostrarResultados(boolean mostrarResultados) {
		this.mostrarResultados = mostrarResultados;
	}
	public boolean isEsIntersecDirectaAngular() {
		return esIntersecDirectaAngular;
	}
	public void setEsIntersecDirectaAngular(boolean esIntersecDirectaAngular) {
		this.esIntersecDirectaAngular = esIntersecDirectaAngular;
	}
	public boolean isEsIntersecDirectaDistancias() {
		return esIntersecDirectaDistancias;
	}
	public void setEsIntersecDirectaDistancias(boolean esIntersecDirectaDistancias) {
		this.esIntersecDirectaDistancias = esIntersecDirectaDistancias;
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
