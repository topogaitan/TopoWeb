package es.gaitan.topografia.interfaces;

public interface Estadillo {
	
	public abstract boolean anadirObservacion(String linea) throws Exception;
	
	public abstract void limpiarEstadillo();
		
}
