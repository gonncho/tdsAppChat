
package umu.tds.apps.persistencia.dao;

import java.util.List;

import umu.tds.apps.modelo.Grupo;

public interface IAdaptadorGrupoDAO {

	public void registrarGrupo(Grupo grupo);

	public Grupo recuperarGrupo(int codigo);

	public List<Grupo> recuperarTodosGrupos();

	public void modificarGrupo(Grupo grupo);
}
