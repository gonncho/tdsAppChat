package umu.tds.apps.persistencia.excepcion;

@SuppressWarnings("serial")
public class DAOException extends Exception {

	public DAOException(String mensaje) {
		super(mensaje);
	}
}
