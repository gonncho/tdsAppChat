package umu.tds.apps.persistencia.adaptador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.PoolDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorChatDAO;

public class AdaptadorChatTDS implements IAdaptadorChatDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorChatTDS unicaInstancia = null;

	private AdaptadorChatTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public static synchronized AdaptadorChatTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorChatTDS();
		}
		return unicaInstancia;
	}

	@Override
	public void registrarChat(Chat chat) {
		if (chat.getCodigo() > 0 && servPersistencia.recuperarEntidad(chat.getCodigo()) != null)
			return; 

		Entidad eChat = new Entidad();
		eChat.setNombre("chat");
		eChat.setPropiedades(Arrays.asList(new Propiedad("usuario", String.valueOf(chat.getUsuario().getCodigo())),
				new Propiedad("otroUsuarioChat", String.valueOf(chat.getOtroUsuario().getCodigo())),
				new Propiedad("mensajes", getCodigosMensajes(chat.getMensajes()))));

		eChat = servPersistencia.registrarEntidad(eChat);
		chat.setCodigo(eChat.getId());
	}

	@Override
	public void modificarChat(Chat chat) {
		Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
		if (eChat == null) {
			System.err.println("No se encontró el chat con ID: " + chat.getCodigo());
			return;
		}
		for (Propiedad prop : eChat.getPropiedades()) {
			switch (prop.getNombre()) {
			case "usuario":
				prop.setValor(String.valueOf(chat.getUsuario().getCodigo()));
				break;
			case "otroUsuarioChat":
				prop.setValor(String.valueOf(chat.getOtroUsuario().getCodigo()));
				break;
			case "mensajes":
				prop.setValor(getCodigosMensajes(chat.getMensajes()));
				break;
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}

	@Override
	public Chat obtenerChat(int codigo) {
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (Chat) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}

		Entidad eChat = servPersistencia.recuperarEntidad(codigo);
		if (eChat == null) {
			System.err.println("No se encontró el chat con ID: " + codigo);
			return null;
		}

		Chat chat = new Chat(null, null);
		chat.setCodigo(codigo);

		PoolDAO.getUnicaInstancia().addObjeto(codigo, chat);

		int usuarioId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eChat, "usuario"));
		int otroUsuarioId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eChat, "otroUsuarioChat"));
		String codigosMensajes = servPersistencia.recuperarPropiedadEntidad(eChat, "mensajes");

		Usuario u = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(usuarioId);
		Usuario u2 = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(otroUsuarioId);

		chat.setUsuario(u);
		chat.setOtroUsuario(u2);

		List<Mensaje> mensajes = getMensajesDesdeCodigos(codigosMensajes);
		chat.setMensajes(mensajes);

		return chat;
	}

	@Override
	public List<Chat> recuperarTodosChats() {
		List<Entidad> eChats = servPersistencia.recuperarEntidades("chat");
		List<Chat> chats = new LinkedList<>();
		for (Entidad eChat : eChats) {
			Chat c = obtenerChat(eChat.getId());
			if (c != null) {
				chats.add(c);
			}
		}
		return chats;
	}
	
	private String getCodigosMensajes(List<Mensaje> listaMensajes) {
		return listaMensajes.stream().map(m -> Integer.toString(m.getCodigo()))
				.collect(java.util.stream.Collectors.joining(" "));
	}

	private List<Mensaje> getMensajesDesdeCodigos(String codigos) {
		if (codigos == null || codigos.trim().isEmpty())
			return new LinkedList<>();

		AdaptadorMensajeTDS adaptadorMensaje = AdaptadorMensajeTDS.getUnicaInstancia();
		return java.util.Arrays.stream(codigos.split(" ")).map(Integer::parseInt)
				.map(adaptadorMensaje::recuperarMensaje).filter(java.util.Objects::nonNull)
				.collect(java.util.stream.Collectors.toCollection(LinkedList::new));
	}
}