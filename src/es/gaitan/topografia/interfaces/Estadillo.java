package es.gaitan.topografia.interfaces;

/**
 * Interface dedicada al estadillo de observaciones
 */
public interface Estadillo {
	
	public abstract boolean anadirObservacion(String linea) throws Exception;
	
	public abstract void limpiarEstadillo();
	
	public abstract int tamanioNubePuntos();
		
}
