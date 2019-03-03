package es.gaitan.topografia.controllers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import es.gaitan.topografia.beans.ObsIntersInversa;
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
	
	/** Inicio Atributos Interseccion Inversa **/
	private BigDecimal acimutAB;
	private BigDecimal acimutBC;
	private BigDecimal acimutAC;
	private Punto3D pEstA;
	private Punto3D pEstB;
	private Punto3D pEstC;
	private BigDecimal alpha;
	private BigDecimal beta;
	private BigDecimal delta;
	private Punto3D pCalDesdeA;
	private Punto3D pCalDesdeC;
	/** Fin Atributos Interseccion Inversa **/
	
	
	private MapModel polygonModel;
	private Punto2D polygonCentroide;
	private String escalaGoogle;
	
	
	/** BOOLEANOS PARA CONTROL DE LA VISTA **/
	private boolean esIntersecDirectaAngular;
	private boolean esIntersecDirectaDistancias;
	private boolean esIntersecInversaPothenot;
	private boolean esIntersecInversaTienstra;
	private boolean disabledButtonsToolbar;
	private boolean renderedCargaFichero;
	private boolean renderedGoogleMaps;
	private boolean mostrarResultados;

	@PostConstruct
	public void reset() {
		logger.info(Constantes.INI_METODO);
		
		try {
			nubePuntos = new NubePuntos();
			estadilloObsIntersDirecta = new EstadilloObsIntersDirecta();
			estadilloObsIntersInversa = new EstadilloObsIntersInversa();
			nubePuntosCalculados = new NubePuntos();
			polygonModel = new DefaultMapModel();
			polygonCentroide = new Punto2D();
			escalaGoogle = "10";
			
			esIntersecDirectaAngular = false;
			esIntersecDirectaDistancias = false;
			esIntersecInversaPothenot = false;
			esIntersecInversaTienstra = false;
			
			disabledButtonsToolbar = true;
			renderedCargaFichero = false;
			renderedGoogleMaps = false;
			mostrarResultados = false;
			
		} catch(Exception exc) {
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_ERROR_GENERICO));
        }
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public void activarIntersecDirectaAngular() {
		logger.info(Constantes.INI_METODO);
		
		try {
			this.reset();
			acimutDI = new BigDecimal(Constantes.int_0);
			acimutID = new BigDecimal(Constantes.int_0);
			acimutDV = new BigDecimal(Constantes.int_0);
			acimutIV = new BigDecimal(Constantes.int_0);
			pEstD = new Punto3D();
			pEstI = new Punto3D();
			pCalDesdeD = new Punto3D();
			pCalDesdeI = new Punto3D();
			
			esIntersecDirectaAngular = true;
			disabledButtonsToolbar = false;
			renderedCargaFichero = true;
			
		} catch(Exception exc) {
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_ERROR_GENERICO));
        }
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public void activarIntersecDirectaDistancias() {
		logger.info(Constantes.INI_METODO);
		
		try {
			this.reset();
			acimutDI = new BigDecimal(Constantes.int_0);
			acimutID = new BigDecimal(Constantes.int_0);
			acimutDV = new BigDecimal(Constantes.int_0);
			acimutIV = new BigDecimal(Constantes.int_0);
			pEstD = new Punto3D();
			pEstI = new Punto3D();
			pCalDesdeD = new Punto3D();
			pCalDesdeI = new Punto3D();
			
			esIntersecDirectaDistancias = true;
			disabledButtonsToolbar = false;
			renderedCargaFichero = true;
			
		} catch(Exception exc) {
			logger.error(Constantes.ERROR_EXCEPTION, exc);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_ERROR_GENERICO));
		}
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public void activarIntersecInversaPothenot() {
		logger.info(Constantes.INI_METODO);
		
		try {
			this.reset();
			acimutAB = new BigDecimal(Constantes.int_0);
			acimutBC = new BigDecimal(Constantes.int_0);
			acimutAC = new BigDecimal(Constantes.int_0);
			pEstA = new Punto3D();
			pEstB = new Punto3D();
			pEstC = new Punto3D();
			alpha = new BigDecimal(Constantes.int_0);
			beta = new BigDecimal(Constantes.int_0);
			delta = new BigDecimal(Constantes.int_0);
			pCalDesdeA = new Punto3D();
			pCalDesdeC = new Punto3D();

			
			esIntersecInversaPothenot = true;
			disabledButtonsToolbar = false;
			renderedCargaFichero = true;
			
		} catch(Exception exc) {
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_ERROR_GENERICO));
        }
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public void activarIntersecInversaTienstra() {
		logger.info(Constantes.INI_METODO);
		
		try {
			this.reset();
			acimutAB = new BigDecimal(Constantes.int_0);
			acimutBC = new BigDecimal(Constantes.int_0);
			acimutAC = new BigDecimal(Constantes.int_0);
			pEstA = new Punto3D();
			pEstB = new Punto3D();
			pEstC = new Punto3D();
			alpha = new BigDecimal(Constantes.int_0);
			beta = new BigDecimal(Constantes.int_0);
			delta = new BigDecimal(Constantes.int_0);
			pCalDesdeA = new Punto3D();
			pCalDesdeC = new Punto3D();
			
			esIntersecInversaTienstra = true;
			disabledButtonsToolbar = false;
			renderedCargaFichero = true;
			
		} catch(Exception exc) {
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_ERROR_GENERICO));
        }
		
		logger.info(Constantes.FIN_METODO);
	}
	
	public String doAccionBotonNuevo() {
		logger.info(Constantes.INI_METODO);
		
		try {
			this.reset();
			disabledButtonsToolbar = true;
			
		} catch(Exception exc) {
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_ERROR_GENERICO));
        }
		
		logger.info(Constantes.FIN_METODO);
		return "";
	}
	
	public void cargarFichero(FileUploadEvent event) {
		logger.info(Constantes.INI_METODO);
		
		try {
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
					Constantes.MSG_ATENCION, Constantes.MSG_EXTENSION_FICHERO_INCORRECTA));
			}
		
		} catch(Exception exc) {
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_ERROR_GENERICO));
        }
			
		logger.info(Constantes.FIN_METODO);
	}
	
	public String doObtenerCalculos() {
		logger.info(Constantes.INI_METODO);
		
		polygonModel = new DefaultMapModel();
		polygonCentroide = new Punto2D();
		nubePuntosCalculados.limpiarNubePuntos();
		
		try {
			if (esIntersecDirectaAngular) {
				calcularIntersecDirectaAngular();
				
			} else if (esIntersecDirectaDistancias) {
				calcularIntersecDirectaDistancias();
			} else if (esIntersecInversaPothenot) {
				calcularIntersecInversaPothenot();
			} else if (esIntersecInversaTienstra) {
				calcularIntersecInversaTienstra();
			}
			
			List<LatLng> listPuntosLatLng = new ArrayList<LatLng>();
			for (Punto3D punto : nubePuntos.getListPuntos()) {
				LatLng coordLatLog = Utilidades.transformarUTMaGeodesicas(punto.getCoordX(), punto.getCoordY());
				listPuntosLatLng.add(coordLatLog);
			}
			for (Punto3D punto : getNubePuntosCalculados().getListPuntos()) {
				LatLng coordLatLog = Utilidades.transformarUTMaGeodesicas(punto.getCoordX(), punto.getCoordY());
				listPuntosLatLng.add(coordLatLog);
			}
			
			obtenerPoligonoParaGoogleMaps(listPuntosLatLng);
			
			mostrarResultados = true;
		
		} catch(Exception exc) {
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_ERROR_GENERICO));
        }
		
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
            } else if (esIntersecInversaPothenot || esIntersecInversaTienstra) {
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
            } else if (esIntersecInversaPothenot || esIntersecInversaTienstra) {
            	estadilloObsIntersInversa.limpiarEstadillo();
            }
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
				Constantes.MSG_ATENCION, Constantes.MSG_ERROR_GENERICO));
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
            } else if (esIntersecInversaPothenot || esIntersecInversaTienstra) {
        		while ((linea = br.readLine()) != null && estaAgregado) {
        			estaAgregado = nubePuntos.anadirPunto(linea);
        		}
            }
            
            // Se cierra la conexion del tunel de datos
            br.close();
            logger.info(Constantes.FIN_METODO);
            
        } catch (Exception exc) {
        	nubePuntos.limpiarNubePuntos();
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
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
			logger.error("FileNotFoundException --> Fichero no encontrado", fnfExc);
		} catch (IOException ioEcx) {
			logger.error("IOException --> El fichero no se ha cargado correctamente", ioEcx);
		}  catch (Exception exc) {
        	logger.error(Constantes.ERROR_EXCEPTION, exc);
        }

       return br;
	}
	
	private void calcularIntersecDirectaAngular() {
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
		
		BigDecimal base_EstDEstI = pEstD.distancia(pEstI);
		
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
		
		logger.debug("Comprobación sumatorio 200g --> " + comprobacion);
		
		// Calcular los lados del triangulo
		BigDecimal ladoDV = new BigDecimal((Math.sin(Utilidades.convertToRadian(anguloI).doubleValue()) * base_EstDEstI.doubleValue()) / 
				(Math.sin(Utilidades.convertToRadian(alpha).doubleValue())));
		BigDecimal ladoIV = new BigDecimal((Math.sin(Utilidades.convertToRadian(anguloD).doubleValue()) * base_EstDEstI.doubleValue()) / 
				(Math.sin(Utilidades.convertToRadian(alpha).doubleValue())));
		
		acimutDV = desorEstD.add(obsDV.getlH());
		acimutIV = desorEstI.add(obsIV.getlH());
		
		// Calcular los incrementos de coordenadas
		BigDecimal axD = new BigDecimal(ladoDV.doubleValue() * Math.sin(Utilidades.convertToRadian(acimutDV).doubleValue()));
		BigDecimal ayD = new BigDecimal(ladoDV.doubleValue() * Math.cos(Utilidades.convertToRadian(acimutDV).doubleValue()));
		
		BigDecimal axI = new BigDecimal(ladoIV.doubleValue() * Math.sin(Utilidades.convertToRadian(acimutIV).doubleValue()));
		BigDecimal ayI = new BigDecimal(ladoIV.doubleValue() * Math.cos(Utilidades.convertToRadian(acimutIV).doubleValue()));
		
		// Calcular coordenadas
		pCalDesdeD = new Punto3D();
		pCalDesdeI = new Punto3D();
		
		pCalDesdeD.setCoordX(pEstD.getCoordX().add(axD));
		pCalDesdeD.setCoordY(pEstD.getCoordY().add(ayD));
		
		pCalDesdeI.setCoordX(pEstI.getCoordX().add(axI));
		pCalDesdeI.setCoordY(pEstI.getCoordY().add(ayI));
		
		// Promedio del punto calculado
		Punto3D pVisProm = new Punto3D();
		pVisProm.setId(obsDV.getIdVisado());
		pVisProm.setCoordX(pCalDesdeD.getCoordX().add(pCalDesdeI.getCoordX()).divide(Constantes.BIGDEC_2).setScale(3, RoundingMode.HALF_DOWN));
		pVisProm.setCoordY(pCalDesdeD.getCoordY().add(pCalDesdeI.getCoordY()).divide(Constantes.BIGDEC_2).setScale(3, RoundingMode.HALF_DOWN));
		pVisProm.setCoordZ(BigDecimal.ZERO);
		
		nubePuntosCalculados.anadirPunto(pVisProm);
		escalaGoogle = "13";
	}
	
	private void calcularIntersecDirectaDistancias() {
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
		
		ObsIntersDirecta obsDI = new ObsIntersDirecta();
		ObsIntersDirecta obsDV = new ObsIntersDirecta();
		ObsIntersDirecta obsID = new ObsIntersDirecta();
		ObsIntersDirecta obsIV = new ObsIntersDirecta();
		
		int numeroObservaciones = estadilloObsIntersDirecta.tamanioNubePuntos();
		for (int i = 0; i < numeroObservaciones; i++) {
			ObsIntersDirecta observacion = estadilloObsIntersDirecta.getPunto(i);
			
			if ("DI".equals(observacion.getCodVisual())) {
				obsDI.setIdEstacion(observacion.getIdEstacion());
				obsDI.setIdVisado(observacion.getIdVisado());
				obsDI.setlH(observacion.getlH());
				obsDI.setlV(observacion.getlV());
				obsDI.setDistancia(observacion.getDistancia());
				obsDI.setCodVisual(observacion.getCodVisual());
				
			} else if ("DV".equals(observacion.getCodVisual())) {
				obsDV.setIdEstacion(observacion.getIdEstacion());
				obsDV.setIdVisado(observacion.getIdVisado());
				obsDV.setlH(observacion.getlH());
				obsDV.setlV(observacion.getlV());
				obsDV.setDistancia(observacion.getDistancia());
				obsDV.setCodVisual(observacion.getCodVisual());
				
			} else if ("ID".equals(observacion.getCodVisual())) {
				obsID.setIdEstacion(observacion.getIdEstacion());
				obsID.setIdVisado(observacion.getIdVisado());
				obsID.setlH(observacion.getlH());
				obsID.setlV(observacion.getlV());
				obsID.setDistancia(observacion.getDistancia());
				obsID.setCodVisual(observacion.getCodVisual());
				
			} else if ("IV".equals(observacion.getCodVisual())) {
				obsIV.setIdEstacion(observacion.getIdEstacion());
				obsIV.setIdVisado(observacion.getIdVisado());
				obsIV.setlH(observacion.getlH());
				obsIV.setlV(observacion.getlV());
				obsIV.setDistancia(observacion.getDistancia());
				obsIV.setCodVisual(observacion.getCodVisual());
			}
		}
		
        // Calcular los lados del triangulo
		BigDecimal base_EstDEstI = pEstD.distancia(pEstI);
		BigDecimal ladoDV = new BigDecimal(obsDV.getDistancia().doubleValue() * Math.sin(Utilidades.convertToRadian(obsDV.getlV()).doubleValue()));
		BigDecimal ladoIV = new BigDecimal(obsIV.getDistancia().doubleValue() * Math.sin(Utilidades.convertToRadian(obsIV.getlV()).doubleValue()));

        // Por el teorema del coseno se calculan los angulos del triangulo
		BigDecimal anguloD = Utilidades.convertToGradian(new BigDecimal(
				Math.acos((Math.pow(ladoDV.doubleValue(), 2) + Math.pow(base_EstDEstI.doubleValue(), 2) - Math.pow(ladoIV.doubleValue(), 2)) 
						/ (2 * ladoDV.doubleValue() * base_EstDEstI.doubleValue()))));
		
		BigDecimal anguloI = Utilidades.convertToGradian(new BigDecimal(
				Math.acos((Math.pow(ladoIV.doubleValue(), 2) + Math.pow(base_EstDEstI.doubleValue(), 2) - Math.pow(ladoDV.doubleValue(), 2)) 
						/ (2 * ladoIV.doubleValue() * base_EstDEstI.doubleValue()))));
        
		BigDecimal alpha = Constantes.BIGDEC_200.subtract(anguloD).subtract(anguloI);

		BigDecimal comprobacion = alpha.add(anguloD).add(anguloI);
		logger.debug("Comprobación sumatorio 200g --> " + comprobacion);

		BigDecimal acimutDV = acimutDI.subtract(anguloD);
		BigDecimal acimutIV = acimutID.add(anguloI);

        // Calcular los incrementos de coordenadas
		BigDecimal axD = new BigDecimal(ladoDV.doubleValue() * Math.sin(Utilidades.convertToRadian(acimutDV).doubleValue()));
		BigDecimal ayD = new BigDecimal(ladoDV.doubleValue() * Math.cos(Utilidades.convertToRadian(acimutDV).doubleValue()));

		BigDecimal axI = new BigDecimal(ladoIV.doubleValue() * Math.sin(Utilidades.convertToRadian(acimutIV).doubleValue()));
		BigDecimal ayI = new BigDecimal(ladoIV.doubleValue() * Math.cos(Utilidades.convertToRadian(acimutIV).doubleValue()));

		
		// Calcular coordenadas
		pCalDesdeD = new Punto3D();
		pCalDesdeI = new Punto3D();
		
		pCalDesdeD.setCoordX(pEstD.getCoordX().add(axD));
		pCalDesdeD.setCoordY(pEstD.getCoordY().add(ayD));
		
		pCalDesdeI.setCoordX(pEstI.getCoordX().add(axI));
		pCalDesdeI.setCoordY(pEstI.getCoordY().add(ayI));
		
		// Promedio del punto calculado
		Punto3D pVisProm = new Punto3D();
		pVisProm.setId(obsDV.getIdVisado());
		pVisProm.setCoordX(pCalDesdeD.getCoordX().add(pCalDesdeI.getCoordX()).divide(Constantes.BIGDEC_2).setScale(3, RoundingMode.HALF_DOWN));
		pVisProm.setCoordY(pCalDesdeD.getCoordY().add(pCalDesdeI.getCoordY()).divide(Constantes.BIGDEC_2).setScale(3, RoundingMode.HALF_DOWN));
		pVisProm.setCoordZ(BigDecimal.ZERO);
		
		nubePuntosCalculados.anadirPunto(pVisProm);
		escalaGoogle = "18";
	}
	
	private void calcularIntersecInversaPothenot() {
		
		// Se localizan las estaciones D e I mediante el codigo del punto
		for (int i = 0; i < nubePuntos.tamanioNubePuntos(); i++) {
	        if ("A".equals(nubePuntos.getPunto(i).getCodigo())) {
	            pEstA = nubePuntos.getPunto(i);
	        } else if ("B".equals(nubePuntos.getPunto(i).getCodigo())) {
	        	 pEstB = nubePuntos.getPunto(i);
	        } else if ("C".equals(nubePuntos.getPunto(i).getCodigo())) {
	        	 pEstC = nubePuntos.getPunto(i);
	        }
		}
		
		acimutAB = pEstA.acimut(pEstB);
        acimutBC = pEstB.acimut(pEstC);
//        acimutAC = pEstA.acimut(pEstC);
		
		ObsIntersInversa obsPA = new ObsIntersInversa();
		ObsIntersInversa obsPB = new ObsIntersInversa();
		ObsIntersInversa obsPC = new ObsIntersInversa();
		
		int numeroObservaciones = estadilloObsIntersInversa.tamanioNubePuntos();
		for (int i = 0; i < numeroObservaciones; i++) {
			ObsIntersInversa observacion = estadilloObsIntersInversa.getPunto(i);
			
			if ("PA".equals(observacion.getCodVisual())) {
				obsPA.setIdEstacion(observacion.getIdEstacion());
				obsPA.setIdVisado(observacion.getIdVisado());
				obsPA.setlH(observacion.getlH());
				obsPA.setlV(observacion.getlV());
//				obsPA.setDistancia(observacion.getDistancia());
				obsPA.setCodVisual(observacion.getCodVisual());
				
			} else if ("PB".equals(observacion.getCodVisual())) {
				obsPB.setIdEstacion(observacion.getIdEstacion());
				obsPB.setIdVisado(observacion.getIdVisado());
				obsPB.setlH(observacion.getlH());
				obsPB.setlV(observacion.getlV());
//				obsPB.setDistancia(observacion.getDistancia());
				obsPB.setCodVisual(observacion.getCodVisual());
				
			} else if ("PC".equals(observacion.getCodVisual())) {
				obsPC.setIdEstacion(observacion.getIdEstacion());
				obsPC.setIdVisado(observacion.getIdVisado());
				obsPC.setlH(observacion.getlH());
				obsPC.setlV(observacion.getlV());
//				obsPC.setDistancia(observacion.getDistancia());
				obsPC.setCodVisual(observacion.getCodVisual());
				
			}
		}
		
		if (obsPB.getlH().compareTo(obsPA.getlH()) < 0) {
            alpha = (Constantes.BIGDEC_400.subtract(obsPA.getlH())).add(obsPB.getlH());
        }
        else {
            alpha = obsPB.getlH().subtract(obsPA.getlH());
        }
        if (obsPC.getlH().compareTo(obsPB.getlH()) < 0) {
            beta = (Constantes.BIGDEC_400.subtract(obsPC.getlH())).add(obsPB.getlH());
        }
        else {
            beta = obsPC.getlH().subtract(obsPB.getlH());
        }
        delta = Constantes.BIGDEC_400.subtract(alpha).subtract(beta);
		
		BigDecimal angA = new BigDecimal(Constantes.int_0);
		BigDecimal angB = new BigDecimal(Constantes.int_0);
		BigDecimal angC = new BigDecimal(Constantes.int_0);
        
		BigDecimal distAB = pEstA.distancia(pEstB);
		BigDecimal distBC = pEstB.distancia(pEstC);

		BigDecimal angAux = new BigDecimal(
				Math.atan((distAB.doubleValue() * Math.sin(Utilidades.convertToRadian(beta).doubleValue()))
						/ (distBC.doubleValue() * Math.sin(Utilidades.convertToRadian(alpha).doubleValue()))));
        angAux = Utilidades.convertToGradian(angAux);

        angB = Utilidades.acimutReciproco(acimutAB).subtract(acimutBC);

        BigDecimal aMasC = Constantes.BIGDEC_200.subtract(((angB.add(alpha).add(beta)).divide(Constantes.BIGDEC_2)));
        BigDecimal aMenosC = new BigDecimal(
        		Math.atan((Math.tan(Utilidades.convertToRadian(aMasC).doubleValue())) * 
        				(1 / (Math.tan(Utilidades.convertToRadian(Constantes.BIGDEC_50.add(angAux)).doubleValue())))));
        aMenosC = Utilidades.convertToGradian(aMenosC);

        angA = aMenosC.add(aMasC);
        angC = aMasC.subtract(aMenosC);

     // Se comprueba que la suma de todos los ángulos sumen 400g
        BigDecimal comprobacion = angA.add(angB).add(angC).add(alpha).add(beta);
		logger.debug("Comprobación sumatorio 400g --> " + comprobacion);

        BigDecimal acimutAP = acimutAB.add(angA);
        BigDecimal acimutCB = Utilidades.acimutReciproco(acimutBC);
        BigDecimal acimutCP = acimutCB.subtract(angC);

        BigDecimal distAP = new BigDecimal(
        		(distAB.doubleValue() * Math.sin(Utilidades.convertToRadian(Constantes.BIGDEC_200.subtract(angA).subtract(alpha)).doubleValue()))
        		/ (Math.sin(Utilidades.convertToRadian(alpha).doubleValue())));
        BigDecimal distCP = new BigDecimal(
        		(distBC.doubleValue() * Math.sin(Utilidades.convertToRadian(Constantes.BIGDEC_200.subtract(angC).subtract(beta)).doubleValue()))
        		/ (Math.sin(Utilidades.convertToRadian(beta).doubleValue())));

        // Calcular los incrementos de coordenadas
        BigDecimal axA = new BigDecimal(distAP.doubleValue() * Math.sin(Utilidades.convertToRadian(acimutAP).doubleValue()));
        BigDecimal ayA = new BigDecimal(distAP.doubleValue() * Math.cos(Utilidades.convertToRadian(acimutAP).doubleValue()));

        BigDecimal axC = new BigDecimal(distCP.doubleValue() * Math.sin(Utilidades.convertToRadian(acimutCP).doubleValue()));
        BigDecimal ayC = new BigDecimal(distCP.doubleValue() * Math.cos(Utilidades.convertToRadian(acimutCP).doubleValue()));

        // Calcular coordenadas del punto estacionado
        Punto3D pCalDesdeA = new Punto3D();
        Punto3D pCalDesdeC = new Punto3D();
        
        pCalDesdeA.setCoordX(pEstA.getCoordX().add(axA));
        pCalDesdeA.setCoordY(pEstA.getCoordY().add(ayA));

        pCalDesdeC.setCoordX(pEstC.getCoordX().add(axC));
        pCalDesdeC.setCoordY(pEstC.getCoordY().add(ayC));
        
        // Promedio del punto calculado
		Punto3D pVisProm = new Punto3D();
//		pVisProm.setId(obsDV.getIdVisado());
		pVisProm.setCoordX(pCalDesdeA.getCoordX().add(pCalDesdeC.getCoordX()).divide(Constantes.BIGDEC_2).setScale(3, RoundingMode.HALF_DOWN));
		pVisProm.setCoordY(pCalDesdeA.getCoordY().add(pCalDesdeC.getCoordY()).divide(Constantes.BIGDEC_2).setScale(3, RoundingMode.HALF_DOWN));
		pVisProm.setCoordZ(BigDecimal.ZERO);
        
		nubePuntosCalculados.anadirPunto(pVisProm);
		escalaGoogle = "18";
	}
	
	private void calcularIntersecInversaTienstra() {
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
		
		ObsIntersDirecta obsDI = new ObsIntersDirecta();
		ObsIntersDirecta obsDV = new ObsIntersDirecta();
		ObsIntersDirecta obsID = new ObsIntersDirecta();
		ObsIntersDirecta obsIV = new ObsIntersDirecta();
		
		int numeroObservaciones = estadilloObsIntersDirecta.tamanioNubePuntos();
		for (int i = 0; i < numeroObservaciones; i++) {
			ObsIntersDirecta observacion = estadilloObsIntersDirecta.getPunto(i);
			
			if ("DI".equals(observacion.getCodVisual())) {
				obsDI.setIdEstacion(observacion.getIdEstacion());
				obsDI.setIdVisado(observacion.getIdVisado());
				obsDI.setlH(observacion.getlH());
				obsDI.setlV(observacion.getlV());
				obsDI.setDistancia(observacion.getDistancia());
				obsDI.setCodVisual(observacion.getCodVisual());
				
			} else if ("DV".equals(observacion.getCodVisual())) {
				obsDV.setIdEstacion(observacion.getIdEstacion());
				obsDV.setIdVisado(observacion.getIdVisado());
				obsDV.setlH(observacion.getlH());
				obsDV.setlV(observacion.getlV());
				obsDV.setDistancia(observacion.getDistancia());
				obsDV.setCodVisual(observacion.getCodVisual());
				
			} else if ("ID".equals(observacion.getCodVisual())) {
				obsID.setIdEstacion(observacion.getIdEstacion());
				obsID.setIdVisado(observacion.getIdVisado());
				obsID.setlH(observacion.getlH());
				obsID.setlV(observacion.getlV());
				obsID.setDistancia(observacion.getDistancia());
				obsID.setCodVisual(observacion.getCodVisual());
				
			} else if ("IV".equals(observacion.getCodVisual())) {
				obsIV.setIdEstacion(observacion.getIdEstacion());
				obsIV.setIdVisado(observacion.getIdVisado());
				obsIV.setlH(observacion.getlH());
				obsIV.setlV(observacion.getlV());
				obsIV.setDistancia(observacion.getDistancia());
				obsIV.setCodVisual(observacion.getCodVisual());
			}
		}
		
        // Calcular los lados del triangulo
		BigDecimal base_EstDEstI = pEstD.distancia(pEstI);
		BigDecimal ladoDV = new BigDecimal(obsDV.getDistancia().doubleValue() * Math.sin(Utilidades.convertToRadian(obsDV.getlV()).doubleValue()));
		BigDecimal ladoIV = new BigDecimal(obsIV.getDistancia().doubleValue() * Math.sin(Utilidades.convertToRadian(obsIV.getlV()).doubleValue()));

        // Por el teorema del coseno se calculan los angulos del triangulo
		BigDecimal anguloD = Utilidades.convertToGradian(new BigDecimal(
				Math.acos((Math.pow(ladoDV.doubleValue(), 2) + Math.pow(base_EstDEstI.doubleValue(), 2) - Math.pow(ladoIV.doubleValue(), 2)) 
						/ (2 * ladoDV.doubleValue() * base_EstDEstI.doubleValue()))));
		
		BigDecimal anguloI = Utilidades.convertToGradian(new BigDecimal(
				Math.acos((Math.pow(ladoIV.doubleValue(), 2) + Math.pow(base_EstDEstI.doubleValue(), 2) - Math.pow(ladoDV.doubleValue(), 2)) 
						/ (2 * ladoIV.doubleValue() * base_EstDEstI.doubleValue()))));
        
		BigDecimal alpha = Constantes.BIGDEC_200.subtract(anguloD).subtract(anguloI);

		BigDecimal comprobacion = alpha.add(anguloD).add(anguloI);
		logger.debug("Comprobación sumatorio 200g --> " + comprobacion);

		BigDecimal acimutDV = acimutDI.subtract(anguloD);
		BigDecimal acimutIV = acimutID.add(anguloI);

        // Calcular los incrementos de coordenadas
		BigDecimal axD = new BigDecimal(ladoDV.doubleValue() * Math.sin(Utilidades.convertToRadian(acimutDV).doubleValue()));
		BigDecimal ayD = new BigDecimal(ladoDV.doubleValue() * Math.cos(Utilidades.convertToRadian(acimutDV).doubleValue()));

		BigDecimal axI = new BigDecimal(ladoIV.doubleValue() * Math.sin(Utilidades.convertToRadian(acimutIV).doubleValue()));
		BigDecimal ayI = new BigDecimal(ladoIV.doubleValue() * Math.cos(Utilidades.convertToRadian(acimutIV).doubleValue()));

		
		// Calcular coordenadas
		pCalDesdeD = new Punto3D();
		pCalDesdeI = new Punto3D();
		
		pCalDesdeD.setCoordX(pEstD.getCoordX().add(axD));
		pCalDesdeD.setCoordY(pEstD.getCoordY().add(ayD));
		
		pCalDesdeI.setCoordX(pEstI.getCoordX().add(axI));
		pCalDesdeI.setCoordY(pEstI.getCoordY().add(ayI));
		
		// Promedio del punto calculado
		Punto3D pVisProm = new Punto3D();
		pVisProm.setId(obsDV.getIdVisado());
		pVisProm.setCoordX(pCalDesdeD.getCoordX().add(pCalDesdeI.getCoordX()).divide(Constantes.BIGDEC_2).setScale(3, RoundingMode.HALF_DOWN));
		pVisProm.setCoordY(pCalDesdeD.getCoordY().add(pCalDesdeI.getCoordY()).divide(Constantes.BIGDEC_2).setScale(3, RoundingMode.HALF_DOWN));
		pVisProm.setCoordZ(BigDecimal.ZERO);
		
		nubePuntosCalculados.anadirPunto(pVisProm);
		escalaGoogle = "18";
	}
	
	private void obtenerPoligonoParaGoogleMaps(List<LatLng> listPuntosLatLng) {
		// Se obtiene el poligono a mostrar en Google Maps
		Polygon polygon = new Polygon();
		for (LatLng latLng : listPuntosLatLng) {
			polygon.getPaths().add(latLng);
		}
		
		// Se customiza el mapa
		polygon.setStrokeColor("#FF9900");
		polygon.setFillColor("#FFECC1");
		polygon.setStrokeOpacity(1);
		polygon.setFillOpacity(0.6);
		polygon.setStrokeWeight(2);
		
		polygonModel.addOverlay(polygon);
		obtenerCoordCentroidePoligono(listPuntosLatLng);
		
		renderedGoogleMaps = true;
	}
	
	/**
	 * Metodo que obtiene el centroide de la figura que representa el listado de puntos que se pasan por parámetro
	 * para centrar el mapa cuando se renderice por pantalla
	 * @param listPuntosLatLng --> Lista de puntos en coordenadas geográficas
	 */
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
	public BigDecimal getAcimutAB() {
		return acimutAB;
	}
	public void setAcimutAB(BigDecimal acimutAB) {
		this.acimutAB = acimutAB;
	}
	public BigDecimal getAcimutBC() {
		return acimutBC;
	}
	public void setAcimutBC(BigDecimal acimutBC) {
		this.acimutBC = acimutBC;
	}
	public BigDecimal getAcimutAC() {
		return acimutAC;
	}
	public void setAcimutAC(BigDecimal acimutAC) {
		this.acimutAC = acimutAC;
	}
	public Punto3D getpEstA() {
		return pEstA;
	}
	public void setpEstA(Punto3D pEstA) {
		this.pEstA = pEstA;
	}
	public Punto3D getpEstB() {
		return pEstB;
	}
	public void setpEstB(Punto3D pEstB) {
		this.pEstB = pEstB;
	}
	public Punto3D getpEstC() {
		return pEstC;
	}
	public void setpEstC(Punto3D pEstC) {
		this.pEstC = pEstC;
	}
	public BigDecimal getAlpha() {
		return alpha;
	}
	public void setAlpha(BigDecimal alpha) {
		this.alpha = alpha;
	}
	public BigDecimal getBeta() {
		return beta;
	}
	public void setBeta(BigDecimal beta) {
		this.beta = beta;
	}
	public BigDecimal getDelta() {
		return delta;
	}
	public void setDelta(BigDecimal delta) {
		this.delta = delta;
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
	public boolean isEsIntersecInversaPothenot() {
		return esIntersecInversaPothenot;
	}
	public void setEsIntersecInversaPothenot(boolean esIntersecInversaPothenot) {
		this.esIntersecInversaPothenot = esIntersecInversaPothenot;
	}
	public boolean isEsIntersecInversaTienstra() {
		return esIntersecInversaTienstra;
	}
	public void setEsIntersecInversaTienstra(boolean esIntersecInversaTienstra) {
		this.esIntersecInversaTienstra = esIntersecInversaTienstra;
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
	public String getEscalaGoogle() {
		return escalaGoogle;
	}
	public void setEscalaGoogle(String escalaGoogle) {
		this.escalaGoogle = escalaGoogle;
	}

}
