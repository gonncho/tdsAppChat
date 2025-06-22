package umu.tds.apps.persistencia.dao;

import java.util.List;

import umu.tds.apps.modelo.Usuario;

/**
 * Interfaz para acceder a la capa de persistencia de usuarios.
 */
public interface IAdaptadorUsuarioDAO {

	public void registrarUsuario(Usuario usuario);

	public void modificarUsuario(Usuario usuario);

	public Usuario recuperarUsuario(int id);

	public List<Usuario> recuperarTodosUsuarios();
}
