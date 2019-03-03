package es.gaitan.topografia.utilities;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.primefaces.model.map.LatLng;

import es.gaitan.topografia.constants.Constantes;
/**
 * Clase de utilidades genéricas para toda la aplicación
 * 
 * @author rafagaitan
 *
 */
public final class Utilidades {
	
	private static final Logger logger = Logger.getLogger(Utilidades.class);

	private Utilidades() {
		super();
	}
	
	public static Boolean isNullOrEmpty(Object obj) {
		Boolean salida = false;
		
		if (null == obj || "".equals(obj)) {
		    salida = true;
		}
		
		return salida;
	}

    public static BigDecimal convertToRadian(BigDecimal angulo) {
        return angulo.multiply(new BigDecimal(Math.PI / Constantes.int_200));
    }

	public static BigDecimal convertToGradian(BigDecimal angulo) {
		return angulo.multiply(new BigDecimal(Constantes.int_200 / Math.PI));
	}

    public static BigDecimal acimutReciproco(BigDecimal acimutEntrada) { 
    	BigDecimal acimutSalida = acimutEntrada;
    	
		if (acimutSalida.compareTo(Constantes.BIGDEC_200) > Constantes.int_0) {
			acimutSalida = acimutSalida.subtract(Constantes.BIGDEC_200);
		} else {
			acimutSalida = acimutSalida.add(Constantes.BIGDEC_200) ;
		}
		
		return acimutSalida;
    }
    
	/**
	 * Metodo que permite transformar las coordenadas UTM (Este, Norte) en coordenadas geográficas (Latitud, Longitud)
	 * @param coord_Este
	 * @param coord_Norte
	 * @return Punto con las coordenadas transformadas 
	 */
    public static LatLng transformarUTMaGeodesicas(BigDecimal coord_Este, BigDecimal coord_Norte) throws Exception {
		LatLng coordGeograficas = null;
	
	    double latitud;
	    double longitud;
        int Zone = 30;
        char Letter = "S".toUpperCase(Locale.ENGLISH).charAt(0);
        double Easting = Double.parseDouble(coord_Este.toString());
        double Northing = Double.parseDouble(coord_Norte.toString());
	    
        double Hem;
        if (Letter > 'M') {
        	Hem='N';
        } else {
        	Hem='S';
        }
        
        double north;
        if (Hem == 'S') {
        	north = Northing - 10000000;
        } else {
        	north = Northing;
        }
        
        // Se ha obtenido del elipsoide WGS84
        double segundaExcentricidadCuadrado = 0.006739433721;
        double radioCurvatura = 6399591.4187;
        
        latitud = (north/6366197.724/0.9996+(1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)-segundaExcentricidadCuadrado*Math.sin(north/6366197.724/0.9996)*Math.cos(north/6366197.724/0.9996)*(Math.atan(Math.cos(Math.atan(( Math.exp((Easting - 500000) / (0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting - 500000) / (0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*( 1 -  segundaExcentricidadCuadrado*Math.pow((Easting - 500000) / (0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*radioCurvatura*(north/6366197.724/0.9996-segundaExcentricidadCuadrado*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(segundaExcentricidadCuadrado*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996 )/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(segundaExcentricidadCuadrado*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*radioCurvatura*(north/6366197.724/0.9996 - segundaExcentricidadCuadrado*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(segundaExcentricidadCuadrado*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996 )*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(segundaExcentricidadCuadrado*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996)*3/2)*(Math.atan(Math.cos(Math.atan((Math.exp((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*radioCurvatura*(north/6366197.724/0.9996-segundaExcentricidadCuadrado*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(segundaExcentricidadCuadrado*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(segundaExcentricidadCuadrado*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*radioCurvatura*(north/6366197.724/0.9996-segundaExcentricidadCuadrado*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(segundaExcentricidadCuadrado*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(segundaExcentricidadCuadrado*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996))*180/Math.PI;
        latitud = Math.round(latitud*10000000);
        latitud = latitud/10000000;
        
        longitud = Math.atan((Math.exp((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*radioCurvatura*( north/6366197.724/0.9996-segundaExcentricidadCuadrado*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(segundaExcentricidadCuadrado*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2* north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(segundaExcentricidadCuadrado*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3)) / (0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-segundaExcentricidadCuadrado*Math.pow((Easting-500000)/(0.9996*radioCurvatura/Math.sqrt((1+segundaExcentricidadCuadrado*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))*180/Math.PI+Zone*6-183;
        longitud = Math.round(longitud*10000000);
        longitud = longitud/10000000;
        
        coordGeograficas = new LatLng(latitud, longitud);
        
        logger.info("latitud --> " + latitud);
		logger.info("longitud --> " + longitud);
 
		return coordGeograficas;
	}
    
//	public static void transformarUTMaGeodesicas(BigDecimal coord_X, BigDecimal coord_Y) {
//	try {
//		File file = new File("E:\\GAITAN\\TFG\\Geodesia_Problema_Directo_Inverso.xlsx");
//		FileInputStream fileInput = new FileInputStream(file);
//		XSSFWorkbook libro = new XSSFWorkbook(fileInput);
//		XSSFSheet hoja = libro.getSheetAt(0);
//		
//		// Se comprueba los valores anteriores de las coordenadas geodésicas
//		XSSFRow filaLongIniOld = hoja.getRow(18);
//		XSSFCell celdaLongIniOld = filaLongIniOld.getCell(7);
//		logger.info("Longitud OLD --> " + celdaLongIniOld.getNumericCellValue());
//		
//		XSSFRow filaLatiIniOld = hoja.getRow(19);
//		XSSFCell celdaLatiIniOld = filaLatiIniOld.getCell(7);
//		logger.info("Latitud OLD --> " + celdaLatiIniOld.getNumericCellValue());
//		
//		XSSFRow filaXUtm = hoja.getRow(13);
//		XSSFCell celdaXUtm = filaXUtm.getCell(2);
//		logger.info("X_UTM OLD --> " + celdaXUtm.getNumericCellValue());
//		
//		// Coordenada X a calcular
//		celdaXUtm.setCellValue(coord_X.doubleValue());
//		logger.info("X_UTM --> " + celdaXUtm.getNumericCellValue());
//		
//		XSSFRow filaYUtm = hoja.getRow(14);
//		XSSFCell celdaYUtm = filaYUtm.getCell(2);
//		logger.info("Y_UTM OLD --> " + celdaYUtm.getNumericCellValue());
//		
//		// Coordenada Y a calcular
//		celdaYUtm.setCellValue(coord_Y.doubleValue());
//		logger.info("Y_UTM --> " + celdaYUtm.getNumericCellValue());
//		
//		// Se evaluan todas las formulas del libro para actualizar resultados
//		XSSFFormulaEvaluator.evaluateAllFormulaCells(libro);
//		
//		XSSFRow filaLong = hoja.getRow(18);
//		XSSFCell celdaLong = filaLong.getCell(7);
//		logger.info("Longitud --> " + celdaLong.getNumericCellValue());
//		
//		XSSFRow filaLati = hoja.getRow(19);
//		XSSFCell celdaLati = filaLati.getCell(7);
//		logger.info("Latitud --> " + celdaLati.getNumericCellValue());
//		
//		fileInput.close();
//		libro.close();
//		
//	} catch (FileNotFoundException fnfExc) {
//		logger.error("Error tipo FileNotFoundException --> Fichero no encontrado", fnfExc);
//	} catch (IOException ioEcx) {
//		logger.error("Error tipo IOException --> El fichero no se ha cargado correctamente", ioEcx);
//	}  catch (Exception exc) {
//    	logger.error("Error tipo Exception", exc);
//    }
//}

}
