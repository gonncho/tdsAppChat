package umu.tds.apps.persistencia.dao;

import java.util.List;

import umu.tds.apps.modelo.Mensaje;

/**
 * Interfaz para la gestión de persistencia de mensajes.
 */
public interface IAdaptadorMensajeDAO {

	public void registrarMensaje(Mensaje mensaje);

	public Mensaje recuperarMensaje(int id);

	public List<Mensaje> recuperarTodosMensajes();
}
