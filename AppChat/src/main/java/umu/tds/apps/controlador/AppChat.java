package umu.tds.apps.controlador;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import umu.tds.apps.estrategia.EstrategiaDescuento;
import umu.tds.apps.estrategia.descuento.DescuentoFecha;
import umu.tds.apps.estrategia.descuento.DescuentoMensaje;
import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.RepositorioUsuarios;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.adaptador.AdaptadorContactoIndividualTDS;
import umu.tds.apps.persistencia.adaptador.AdaptadorGrupoTDS;
import umu.tds.apps.persistencia.adaptador.AdaptadorUsuarioTDS;
import umu.tds.apps.persistencia.dao.IAdaptadorChatDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorMensajeDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorUsuarioDAO;
import umu.tds.apps.persistencia.factoria.FactoriaDAO;

public class AppChat {

	private Usuario usuarioActual;
	private RepositorioUsuarios repositorioUsuarios;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorChatDAO adaptadorChat;
	private IAdaptadorMensajeDAO adaptadorMensaje;

	private static AppChat unicaInstancia = null;

	public static final double PRECIO_PREMIUM = 25.0;

	private AppChat() {
		inicializarAdaptador();
		inicializarRepositorio();
	}

	public void inicializarRepositorio() {
		repositorioUsuarios = RepositorioUsuarios.getUnicaInstancia();
	}

	public void inicializarAdaptador() {
		try {
			FactoriaDAO factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuario = factoria.getUsuarioDAO();
			adaptadorMensaje = factoria.getMensajeDAO();
			adaptadorChat = factoria.getChatDAO();
		} catch (Exception e) {
			System.err.println("Error al inicializar los adaptadores: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static AppChat getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AppChat();
		}
		return unicaInstancia;
	}

	public static List<Mensaje> getMensajesRecientesPorUsuario() {
		Usuario actual = Optional.ofNullable(getUsuarioActual())
				.orElseThrow(() -> new IllegalStateException("No hay un usuario autenticado."));

		return actual.getListaChats().stream().flatMap(c -> c.getMensajes().stream())
				.sorted(Comparator.comparing(Mensaje::getFecha).thenComparing(Mensaje::getHora).reversed())
				.collect(Collectors.toList());
	}

	public boolean iniciarSesion(String telefono, char[] contraseña) {
		Optional<Usuario> encontrado = Optional.ofNullable(repositorioUsuarios.getUsuario(telefono))
				.filter(u -> u.isClaveValida(new String(contraseña)));

		usuarioActual = encontrado.orElse(null);
		return encontrado.isPresent();
	}

	public boolean registrarUsuario(String nombre, char[] clave, String telefono, String email, Optional<String> saludo,
			Icon imagenPerfil, LocalDate nacimiento, LocalDate registro) {
		if (repositorioUsuarios.getUsuario(telefono) != null) {
			return false;
		}

		ImageIcon icono = null;
		if (imagenPerfil instanceof ImageIcon) {
			ImageIcon img = (ImageIcon) imagenPerfil;
			icono = new ImageIcon(img.getDescription());
		}

		Usuario nuevo = new Usuario(nombre, new String(clave), telefono, email, saludo, icono, nacimiento, registro);

		try {
			adaptadorUsuario.registrarUsuario(nuevo);
			repositorioUsuarios.addUsuario(nuevo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void cambiarImagenPerfil(ImageIcon imagenPerfil) {
		if (usuarioActual != null) {
			usuarioActual.setImagenPerfil(imagenPerfil);
			adaptadorUsuario.modificarUsuario(usuarioActual);
		} else {
			System.err.println("No hay usuario autenticado.");
		}
	}

	public void cambiarSaludo(String saludo) {
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado.");
		}
		usuarioActual.setSaludo(Optional.of(saludo));
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	/**
	 * Calcula el precio de suscripción premium aplicando descuentos por antigüedad y actividad
	 */
	public double calcularPrecioPremium() {
		Usuario u = Optional.ofNullable(usuarioActual)
				.orElseThrow(() -> new IllegalStateException("No hay un usuario autenticado."));

		EstrategiaDescuento estrategia = new EstrategiaDescuento();
		List<umu.tds.apps.estrategia.descuento.Descuento> descuentos = Arrays.asList(
				new DescuentoFecha(u.getFechaRegistro()), new DescuentoMensaje((int) u.getNumeroMensajesUltimoMes()));

		double precio = PRECIO_PREMIUM;
		for (umu.tds.apps.estrategia.descuento.Descuento d : descuentos) {
			estrategia.setEstrategiaDescuento(d);
			precio = estrategia.calcularPrecioFinal(precio);
		}

		return precio;
	}

	public boolean activarPremium() {
		Usuario u = Optional.ofNullable(usuarioActual)
				.orElseThrow(() -> new IllegalStateException("No hay un usuario autenticado."));

		if (!u.isPremium()) {
			u.activarPremium();
			adaptadorUsuario.modificarUsuario(u);
		}

		return true;
	}

	public static Usuario getUsuarioActual() {
		return (unicaInstancia != null) ? unicaInstancia.usuarioActual : null;
	}

	public List<Mensaje> obtenerMensajesEnviados() {
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado.");
		}
		return usuarioActual.getListaMensajesEnviados();
	}

	public List<Mensaje> obtenerMensajesRecibidos() {
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado.");
		}
		return usuarioActual.getListaMensajesRecibidos();
	}

	public List<Mensaje> obtenerMensajesConContacto(Usuario contacto) {
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado.");
		}

		return usuarioActual.obtenerMensajesCon(contacto);
	}

	public boolean añadirContacto(String nombre, String telefono) {
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado");
		}

		if (usuarioActual.getTelefono().equals(telefono)) {
			System.err.println("Error: No se puede añadir a uno mismo como contacto");
			return false;
		}

		boolean yaExiste = usuarioActual.getListaContactos().stream().filter(c -> c instanceof ContactoIndividual)
				.map(c -> ((ContactoIndividual) c).getTelefono()).anyMatch(telefono::equals);
		if (yaExiste) {
			System.err.println("Error: Ya existe un contacto con el número " + telefono);
			return false;
		}

		Usuario usuarioContacto = repositorioUsuarios.getUsuario(telefono);
		if (usuarioContacto == null) {
			return false;
		}

		ContactoIndividual nuevo = new ContactoIndividual(nombre, telefono, usuarioContacto);
		try {
			usuarioActual.añadirContacto(nuevo);
			AdaptadorContactoIndividualTDS.getUnicaInstancia().registrarContactoIndividual(nuevo);
			adaptadorUsuario.modificarUsuario(usuarioActual);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean crearGrupo(String nombreGrupo, List<ContactoIndividual> contactos, Optional<ImageIcon> imagenGrupo) {
		if (nombreGrupo == null || nombreGrupo.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del grupo no puede estar vacío.");
		}

		boolean nombreDuplicado = getUsuarioActual().getGrupos().stream()
				.anyMatch(grupo -> grupo.getNombre().equalsIgnoreCase(nombreGrupo));
		if (nombreDuplicado) {
			throw new IllegalArgumentException("Ya existe un grupo con este nombre.");
		}

		Grupo nuevoGrupo = new Grupo(nombreGrupo, contactos, getUsuarioActual(), imagenGrupo.orElse(null));
		getUsuarioActual().añadirContacto(nuevoGrupo);

		// Persistir el grupo y el usuario
		AdaptadorGrupoTDS.getUnicaInstancia().registrarGrupo(nuevoGrupo);
		AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(getUsuarioActual());

		return true;
	}

	/**
	 * Filtra mensajes aplicando simultáneamente criterios de texto, teléfono y contacto
	 */
	public List<Mensaje> filtrarMensajes(String texto, String telefono, String contacto) {
		return usuarioActual.filtrarMensajes(texto, telefono, contacto);
	}

	public void crearNuevoChat(Usuario uActual, Usuario otroUsuario) {
		Optional.ofNullable(uActual.obtenerChatCon(otroUsuario)).orElseGet(() -> {
			Chat chat = uActual.crearChatCon(otroUsuario);
			adaptadorChat.registrarChat(chat);
			adaptadorUsuario.modificarUsuario(uActual);
			adaptadorUsuario.modificarUsuario(otroUsuario);
			return chat;
		});
	}

	/**
	 * Envía un mensaje entre usuarios, creando el chat si no existe
	 */
	public boolean enviarMensaje(Usuario uActual, Usuario receptor, String texto) {
		if (texto == null) {
			System.err.println("Error: texto null");
			return false;
		}

		Chat chat = Optional.ofNullable(uActual.obtenerChatCon(receptor)).orElseGet(() -> {
			Chat nuevo = new Chat(uActual, receptor);
			adaptadorChat.registrarChat(nuevo);
			uActual.añadirChat(nuevo);
			receptor.añadirChat(nuevo);
			adaptadorUsuario.modificarUsuario(uActual);
			adaptadorUsuario.modificarUsuario(receptor);
			return nuevo;
		});

		Mensaje mensaje = new Mensaje(uActual, texto, receptor, chat);
		adaptadorMensaje.registrarMensaje(mensaje);

		uActual.añadirMensajeEnviado(mensaje);
		receptor.añadirMensajeRecibido(mensaje);

		adaptadorUsuario.modificarUsuario(uActual);
		adaptadorUsuario.modificarUsuario(receptor);

		chat.addMensaje(mensaje);
		adaptadorChat.modificarChat(chat);

		return true;
	}

	public boolean enviarEmoji(Usuario uActual, Usuario uDestino, int emoji) {
		String texto = "EMOJI:" + emoji;
		return enviarMensaje(uActual, uDestino, texto);

	}

	public Usuario obtenerUsuarioPorTelefono(String telefono) {
		return Optional.ofNullable(telefono).map(String::trim).map(repositorioUsuarios::getUsuario).orElse(null);
	}

	/**
	 * Busca un usuario entre los contactos del usuario actual por su nombre
	 * (no es una búsqueda global en todos los usuarios)
	 */
	public Usuario obtenerUsuarioPorNombre(String nombre) {
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado");
		}

		for (Contacto c : usuarioActual.getListaContactos()) {
			if (c instanceof ContactoIndividual) {
				ContactoIndividual ci = (ContactoIndividual) c;
				if (ci.getNombre().equals(nombre)) {
					return ci.getUsuario();
				}
			}
		}

		return null;
	}

	public ContactoIndividual obtenerContactoPorTelefono(String telefono) {
		if (usuarioActual == null) {
			throw new IllegalStateException("No hay un usuario autenticado");
		}

		for (Contacto c : usuarioActual.getListaContactos()) {
			if (c instanceof ContactoIndividual) {
				ContactoIndividual ci = (ContactoIndividual) c;
				if (ci.getTelefono().equals(telefono)) {
					return ci;
				}
			}
		}
 
		return null;
	}

	public boolean enviarMensajeAGrupo(Usuario emisor, Grupo grupo, String texto) {
		if (texto == null || texto.trim().isEmpty() || emisor == null || grupo == null
				|| grupo.getListaContactos().isEmpty()) {
			System.err.println("Datos inválidos para enviar mensaje al grupo.");
			return false;
		}

		boolean enviado = true;
		Mensaje referencia = null;

		for (ContactoIndividual miembro : grupo.getListaContactos()) {
			boolean ok = enviarMensaje(emisor, miembro.getUsuario(), texto);
			if (!ok) {
				enviado = false;
			} else if (referencia == null) {
				Chat chat = emisor.obtenerChatCon(miembro.getUsuario());
				referencia = chat.getMensajes().get(chat.getMensajes().size() - 1);
			}
		}

		if (referencia != null) {
			grupo.addMensajeEnviado(referencia);
			AdaptadorGrupoTDS.getUnicaInstancia().modificarGrupo(grupo);
		}

		return enviado;
	}

	public boolean enviarEmojiAGrupo(Usuario emisor, Grupo grupo, int emoji) {
		String texto = "EMOJI:" + emoji;

		boolean resultado = enviarMensajeAGrupo(emisor, grupo, texto);

		if (resultado) {
			boolean mensajeYaExistente = false;
			for (Mensaje m : grupo.getMensajesEnviados()) {
				if (m.getTexto().equals(texto) && m.getEmisor().equals(emisor)
						&& m.getFecha().equals(LocalDate.now())) {
					mensajeYaExistente = true;
					break;
				}
			}

			if (!mensajeYaExistente) {
				List<ContactoIndividual> miembros = grupo.getListaContactos();
				if (!miembros.isEmpty()) {
					Usuario primerMiembro = miembros.get(0).getUsuario();
					Chat chat = emisor.obtenerChatCon(primerMiembro);

					if (chat != null) {
						List<Mensaje> mensajes = chat.getMensajes();
						for (int i = mensajes.size() - 1; i >= 0; i--) {
							Mensaje m = mensajes.get(i);
							if (m.getTexto().equals(texto) && m.getEmisor().equals(emisor)) {
								grupo.addMensajeEnviado(m);
								AdaptadorGrupoTDS.getUnicaInstancia().modificarGrupo(grupo);
								break;
							}
						}
					}
				}
			}
		}

		return resultado;
	}

	public boolean generarPDFContactos(String rutaArchivo) {
		if (!usuarioActual.isPremium()) {
			System.err.println("Esta función solo está disponible para usuarios Premium");
			return false;
		}

		Document documento = new Document();

		try {
			PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
			documento.open();

			Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);

			documento.add(new Paragraph("Contactos y Grupos de " + usuarioActual.getUsuario(), fontTitle));
			documento.add(new Paragraph(" ")); 

			documento.add(new Paragraph("Información del Usuario:", fontSubtitle));
			documento.add(new Paragraph("Nombre: " + usuarioActual.getUsuario(), fontNormal));
			documento.add(new Paragraph("Teléfono: " + usuarioActual.getTelefono(), fontNormal));
			documento.add(new Paragraph("Email: " + usuarioActual.getEmail(), fontNormal));
			documento.add(new Paragraph(" ")); 

			documento.add(new Paragraph("Contactos Individuales:", fontSubtitle));
			documento.add(new Paragraph(" ")); 

			PdfPTable tablaContactos = new PdfPTable(2); 
			tablaContactos.setWidthPercentage(100);
			tablaContactos.setWidths(new float[] { 2, 1 });

			PdfPCell celdaNombre = new PdfPCell(new Phrase("Nombre", fontSubtitle));
			PdfPCell celdaTelefono = new PdfPCell(new Phrase("Teléfono", fontSubtitle));
			tablaContactos.addCell(celdaNombre);
			tablaContactos.addCell(celdaTelefono);

			for (Contacto contacto : usuarioActual.getListaContactos()) {
				if (contacto instanceof ContactoIndividual) {
					ContactoIndividual ci = (ContactoIndividual) contacto;
					tablaContactos.addCell(new Phrase(ci.getNombre(), fontNormal));
					tablaContactos.addCell(new Phrase(ci.getTelefono(), fontNormal));
				}
			}

			documento.add(tablaContactos);
			documento.add(new Paragraph(" "));

			documento.add(new Paragraph("Grupos:", fontSubtitle));
			documento.add(new Paragraph(" ")); 

			for (Contacto contacto : usuarioActual.getListaContactos()) {
				if (contacto instanceof Grupo) {
					Grupo grupo = (Grupo) contacto;
					documento.add(new Paragraph("Grupo: " + grupo.getNombre(), fontSubtitle));

					PdfPTable tablaMiembros = new PdfPTable(2);
					tablaMiembros.setWidthPercentage(100);
					tablaMiembros.setWidths(new float[] { 2, 1 }); 

					PdfPCell celdaMiembro = new PdfPCell(new Phrase("Miembro", fontSubtitle));
					PdfPCell celdaTelefonoMiembro = new PdfPCell(new Phrase("Teléfono", fontSubtitle));
					tablaMiembros.addCell(celdaMiembro);
					tablaMiembros.addCell(celdaTelefonoMiembro);

					for (ContactoIndividual miembro : grupo.getListaContactos()) {
						tablaMiembros.addCell(new Phrase(miembro.getNombre(), fontNormal));
						tablaMiembros.addCell(new Phrase(miembro.getTelefono(), fontNormal));
					}

					documento.add(tablaMiembros);
					documento.add(new Paragraph(" ")); 

					List<Mensaje> mensajesGrupo = grupo.getMensajesEnviados();
					if (mensajesGrupo != null && !mensajesGrupo.isEmpty()) {
						documento.add(new Paragraph("Mensajes enviados al grupo:", fontSubtitle));
						documento.add(new Paragraph(" ")); 

						PdfPTable tablaMensajes = new PdfPTable(3); 
						tablaMensajes.setWidthPercentage(100);
						tablaMensajes.setWidths(new float[] { 1.5f, 3, 1.5f }); 

						tablaMensajes.addCell(new Phrase("Emisor", fontSubtitle));
						tablaMensajes.addCell(new Phrase("Mensaje", fontSubtitle));
						tablaMensajes.addCell(new Phrase("Fecha y Hora", fontSubtitle));

						for (Mensaje mensaje : mensajesGrupo) {
							String nombreEmisor = mensaje.getEmisor().equals(usuarioActual) ? "Yo"
									: mensaje.getEmisor().getUsuario();

							String textoMensaje = mensaje.getTexto();
							if (textoMensaje.startsWith("EMOJI:")) {
								textoMensaje = "[Emoji]";
							}

							String fechaHora = mensaje.getFecha().toString() + " " + String.format("%02d:%02d",
									mensaje.getHora().getHour(), mensaje.getHora().getMinute());

							tablaMensajes.addCell(new Phrase(nombreEmisor, fontNormal));
							tablaMensajes.addCell(new Phrase(textoMensaje, fontNormal));
							tablaMensajes.addCell(new Phrase(fechaHora, fontNormal));
						}

						documento.add(tablaMensajes);
					} else {
						documento.add(new Paragraph("No hay mensajes enviados a este grupo.", fontNormal));
					}

					documento.add(new Paragraph(" "));
				}
			}

			documento.close();
			return true;

		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			if (documento.isOpen()) {
				documento.close();
			}
			return false;
		}
	}

	/**
	 * Genera un PDF con el historial de conversación entre el usuario actual y otro usuario
	 */
	public boolean generarPDFMensajesUsuario(Usuario otroUsuario, String rutaArchivo) {
		if (!usuarioActual.isPremium()) {
			System.err.println("Esta función solo está disponible para usuarios Premium");
			return false;
		}

		if (otroUsuario == null) {
			System.err.println("El usuario de destino no puede ser nulo");
			return false;
		}

		Document documento = new Document();

		try {
			PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
			documento.open();

			Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);

			documento.add(new Paragraph("Mensajes con " + otroUsuario.getUsuario(), fontTitle));
			documento.add(new Paragraph(" ")); 

			documento.add(new Paragraph(
					"Usuario actual: " + usuarioActual.getUsuario() + " (" + usuarioActual.getTelefono() + ")",
					fontNormal));
			documento.add(new Paragraph(
					"Usuario conversación: " + otroUsuario.getUsuario() + " (" + otroUsuario.getTelefono() + ")",
					fontNormal));
			documento.add(new Paragraph(" "));

			List<Mensaje> mensajes = usuarioActual.obtenerMensajesCon(otroUsuario);

			documento.add(new Paragraph("Mensajes intercambiados:", fontSubtitle));
			documento.add(new Paragraph(" "));

			if (mensajes.isEmpty()) {
				documento.add(new Paragraph("No hay mensajes intercambiados con este usuario.", fontNormal));
			} else {
				PdfPTable tablaMensajes = new PdfPTable(4); 
				tablaMensajes.setWidthPercentage(100);
				tablaMensajes.setWidths(new float[] { 1.5f, 1.5f, 4, 2 }); 

				tablaMensajes.addCell(new Phrase("Emisor", fontSubtitle));
				tablaMensajes.addCell(new Phrase("Receptor", fontSubtitle));
				tablaMensajes.addCell(new Phrase("Mensaje", fontSubtitle));
				tablaMensajes.addCell(new Phrase("Fecha y Hora", fontSubtitle));

				for (Mensaje mensaje : mensajes) {
					String nombreEmisor = mensaje.getEmisor().equals(usuarioActual) ? "Yo" : otroUsuario.getUsuario();
					String nombreReceptor = mensaje.getReceptor().equals(usuarioActual) ? "Yo"
							: otroUsuario.getUsuario();
					String textoMensaje = mensaje.getTexto();
					if (textoMensaje.startsWith("EMOJI:")) {
						textoMensaje = "[Emoji]"; 
					}
					String fechaHora = mensaje.getFecha().toString() + " "
							+ String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute());

					tablaMensajes.addCell(new Phrase(nombreEmisor, fontNormal));
					tablaMensajes.addCell(new Phrase(nombreReceptor, fontNormal));
					tablaMensajes.addCell(new Phrase(textoMensaje, fontNormal));
					tablaMensajes.addCell(new Phrase(fechaHora, fontNormal));
				}

				documento.add(tablaMensajes);
			}

			documento.close();
			return true;

		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			if (documento.isOpen()) {
				documento.close();
			}
			return false;
		}
	}

	/**
	 * Verifica si existe un grupo con el nombre especificado
	 * @param nombreGrupo El nombre a verificar
	 * @return true si ya existe un grupo con ese nombre
	 */
	public boolean existeGrupoConNombre(String nombreGrupo) {
		return getUsuarioActual().getGrupos().stream()
				.anyMatch(grupo -> grupo.getNombreGrupo().equalsIgnoreCase(nombreGrupo));
	}

	/**
	 * Obtiene un grupo existente por su nombre
	 * @param nombreGrupo El nombre del grupo a buscar
	 * @return El grupo encontrado o null si no existe
	 */
	public Grupo obtenerGrupoPorNombre(String nombreGrupo) {
		return getUsuarioActual().getListaContactos().stream()
				.filter(contacto -> contacto instanceof Grupo)
				.map(contacto -> (Grupo) contacto)
				.filter(grupo -> grupo.getNombreGrupo().equalsIgnoreCase(nombreGrupo))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Convierte una lista de nombres de contactos en ContactoIndividual
	 * @param nombresContactos Lista de nombres de contactos
	 * @return Lista de objetos ContactoIndividual correspondientes
	 */
	public List<ContactoIndividual> obtenerContactosPorNombres(List<String> nombresContactos) {
		List<ContactoIndividual> resultado = new ArrayList<>();
		
		for (String nombre : nombresContactos) {
			ContactoIndividual contacto = (ContactoIndividual) usuarioActual.getListaContactos().stream()
					.filter(c -> c instanceof ContactoIndividual && c.getNombre().equals(nombre))
					.findFirst()
					.orElse(null);
			
			if (contacto != null) {
				resultado.add(contacto);
			}
		}
		
		return resultado;
	}

	/**
	 * Añade contactos a un grupo existente
	 * @param grupo El grupo al que añadir los contactos
	 * @param contactos Lista de contactos a añadir
	 * @return true si se añadió al menos un contacto nuevo
	 */
	public boolean agregarContactosAGrupo(Grupo grupo, List<ContactoIndividual> contactos) {
		boolean cambiosRealizados = false;
		
		for (ContactoIndividual contacto : contactos) {
			if (!grupo.getListaContactos().contains(contacto)) {
				grupo.agregarContacto(contacto);
				cambiosRealizados = true;
			}
		}
		
		// Si se realizaron cambios, persistir
		if (cambiosRealizados) {
			// Código de persistencia aquí (adaptador)
		}
		
		return cambiosRealizados;
	}
}