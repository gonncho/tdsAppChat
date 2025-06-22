package umu.tds.apps.persistencia.adaptador;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.PoolDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorMensajeDAO;

public class AdaptadorMensajeTDS implements IAdaptadorMensajeDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorMensajeTDS unicaInstancia = null;

	private AdaptadorMensajeTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public static synchronized AdaptadorMensajeTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorMensajeTDS();
		}
		return unicaInstancia;
	}

	@Override
	public void registrarMensaje(Mensaje mensaje) {
		if (mensaje.getCodigo() > 0 && servPersistencia.recuperarEntidad(mensaje.getCodigo()) != null)
			return; 

		Entidad eMensaje = new Entidad();
		eMensaje.setNombre("mensaje");
		eMensaje.setPropiedades(Arrays.asList(new Propiedad("texto", mensaje.getTexto()),
				new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getCodigo())),
				new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getCodigo())),
				new Propiedad("fecha", mensaje.getFecha().toString()),
				new Propiedad("hora", mensaje.getHora().toString()),
				new Propiedad("chat", String.valueOf(mensaje.getChat().getCodigo()))));

		eMensaje = servPersistencia.registrarEntidad(eMensaje);
		mensaje.setCodigo(eMensaje.getId());
	}

	@Override
	public Mensaje recuperarMensaje(int codigo) {
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}

		Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);
		if (eMensaje == null) {
			System.err.println("No se encontró el mensaje con ID: " + codigo);
			return null;
		}

		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
		int emisorId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor"));
		int receptorId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor"));
		int chatId = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "chat"));

		String fechaStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, "fecha");
		String horaStr = servPersistencia.recuperarPropiedadEntidad(eMensaje, "hora");

		Mensaje mensaje = new Mensaje();
		mensaje.setCodigo(codigo);

		PoolDAO.getUnicaInstancia().addObjeto(codigo, mensaje);

		Usuario emisor = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(emisorId);
		Usuario receptor = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(receptorId);
		Chat chat = AdaptadorChatTDS.getUnicaInstancia().obtenerChat(chatId);

		mensaje.setEmisor(emisor);
		mensaje.setReceptor(receptor);
		mensaje.setChat(chat);
		mensaje.setTexto(texto);
		
		java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);
		java.time.LocalDateTime hora = java.time.LocalDateTime.parse(horaStr);
		mensaje.setFecha(fecha);
		mensaje.setHora(hora);

		return mensaje;
	}

	@Override
	public List<Mensaje> recuperarTodosMensajes() {
		List<Entidad> eMensajes = servPersistencia.recuperarEntidades("mensaje");
		List<Mensaje> mensajes = new LinkedList<>();
		for (Entidad eMensaje : eMensajes) {
			Mensaje m = recuperarMensaje(eMensaje.getId());
			if (m != null) {
				mensajes.add(m);
			}
		}
		return mensajes;
	}

}
