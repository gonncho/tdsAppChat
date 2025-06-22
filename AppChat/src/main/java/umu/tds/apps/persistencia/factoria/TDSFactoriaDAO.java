package umu.tds.apps.persistencia.factoria;

import umu.tds.apps.persistencia.adaptador.AdaptadorChatTDS;
import umu.tds.apps.persistencia.adaptador.AdaptadorContactoIndividualTDS;
import umu.tds.apps.persistencia.adaptador.AdaptadorGrupoTDS;
import umu.tds.apps.persistencia.adaptador.AdaptadorMensajeTDS;
import umu.tds.apps.persistencia.adaptador.AdaptadorUsuarioTDS;
import umu.tds.apps.persistencia.dao.IAdaptadorChatDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorContactoIndividualDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorGrupoDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorMensajeDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorUsuarioDAO;

public class TDSFactoriaDAO extends FactoriaDAO {

	public TDSFactoriaDAO() {
	}

	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorMensajeDAO getMensajeDAO() {
		return AdaptadorMensajeTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorContactoIndividualDAO getContactoIndividualDAO() {
		return AdaptadorContactoIndividualTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorChatDAO getChatDAO() {
		return AdaptadorChatTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorGrupoDAO getGrupoDAO() {
		return AdaptadorGrupoTDS.getUnicaInstancia();
	}

}
