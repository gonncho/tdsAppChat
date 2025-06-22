
package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umu.tds.apps.persistencia.dao.IAdaptadorUsuarioDAO;
import umu.tds.apps.persistencia.excepcion.DAOException;
import umu.tds.apps.persistencia.factoria.FactoriaDAO;

public class RepositorioUsuarios {
	private Map<String, Usuario> usuarios;
	private static RepositorioUsuarios unicaInstancia = new RepositorioUsuarios();

	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;

	private RepositorioUsuarios() {
		usuarios = new HashMap<String, Usuario>();
		try {
			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuario = dao.getUsuarioDAO();

			this.catalogoUsuarios();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}

	public static RepositorioUsuarios getUnicaInstancia() {
		return unicaInstancia;
	}

	public List<Usuario> getUsuarios() {
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		for (Usuario u : usuarios.values())
			lista.add(u);
		return lista;
	}

	public Usuario getUsuario(int codigo) {
		return usuarios.values().stream().filter(c -> c.getCodigo() == codigo).findFirst().orElse(null);
	}

	public Usuario getUsuario(String telefono) {
		return usuarios.get(telefono);
	}

	public void addUsuario(Usuario usu) {
		usuarios.put(usu.getTelefono(), usu);
	}

	public void deleteUsuario(Usuario usu) {
		usuarios.remove(usu.getTelefono());
	}

	// Carga todos los usuarios almacenados en la base de datos al crear la app
	
	private void catalogoUsuarios() throws DAOException {
		List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
		for (Usuario usu : usuariosBD)
			usuarios.put(usu.getTelefono(), usu);
	}

}
