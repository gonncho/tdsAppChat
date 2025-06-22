package umu.tds.apps.persistencia.factoria;

import umu.tds.apps.persistencia.dao.IAdaptadorChatDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorContactoIndividualDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorGrupoDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorMensajeDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorUsuarioDAO;
import umu.tds.apps.persistencia.excepcion.DAOException;

public abstract class FactoriaDAO {
	private static FactoriaDAO unicaInstancia;

	public static final String DAO_TDS = "umu.tds.apps.persistencia.factoria.TDSFactoriaDAO";

	public static FactoriaDAO getInstancia(String tipo) throws DAOException {
		if (unicaInstancia == null) {
			try {
				unicaInstancia = (FactoriaDAO) Class.forName(tipo).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new DAOException(e.getMessage());
			}
		}
		return unicaInstancia;
	}

	public static FactoriaDAO getInstancia() throws DAOException {
		return (unicaInstancia == null) ? getInstancia(FactoriaDAO.DAO_TDS) : unicaInstancia;
	}

	protected FactoriaDAO() {
	}

	public abstract IAdaptadorUsuarioDAO getUsuarioDAO();

	public abstract IAdaptadorMensajeDAO getMensajeDAO();

	public abstract IAdaptadorContactoIndividualDAO getContactoIndividualDAO();

	public abstract IAdaptadorChatDAO getChatDAO();

	public abstract IAdaptadorGrupoDAO getGrupoDAO();

}
