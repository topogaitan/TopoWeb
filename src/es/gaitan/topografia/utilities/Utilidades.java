package es.gaitan.topografia.utilities;

import java.math.BigDecimal;

import es.gaitan.topografia.constants.Constantes;

public final class Utilidades {

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

}
