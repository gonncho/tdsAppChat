
package umu.tds.apps.persistencia.adaptador;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.swing.ImageIcon;

import beans.Entidad;
import beans.Propiedad;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.PoolDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorUsuarioDAO;

public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioTDS unicaInstancia = null;

	private AdaptadorUsuarioTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public static synchronized AdaptadorUsuarioTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorUsuarioTDS();
		}
		return unicaInstancia;
	}

	@Override
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = (usuario.getCodigo() > 0) ? servPersistencia.recuperarEntidad(usuario.getCodigo()) : null;
		if (eUsuario != null)
			return; // ya existe

		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(Arrays.asList(new Propiedad("nombre", usuario.getUsuario()),
				new Propiedad("telefono", usuario.getTelefono()), new Propiedad("email", usuario.getEmail()),
				new Propiedad("contraseña", usuario.getContraseña()),
				new Propiedad("saludo", usuario.getSaludo().orElse("")),
				new Propiedad("fechaRegistro", usuario.getFechaRegistro().toString()),
				new Propiedad("contactos", obtenerCodigosContactos(usuario.getListaContactos())),
				new Propiedad("imagenPerfil",
						(usuario.getFotoPerfil() != null) ? usuario.getFotoPerfil().getDescription() : ""),
				new Propiedad("mensajesEnviados", obtenerCodigosMensajes(usuario.getListaMensajesEnviados())),
				new Propiedad("mensajesRecibidos", obtenerCodigosMensajes(usuario.getListaMensajesRecibidos())),
				new Propiedad("chats", obtenerCodigosChats(usuario.getListaChats())),
				new Propiedad("premium", String.valueOf(usuario.isPremium())),
				new Propiedad("fechaNacimiento", usuario.getFechaNacimiento() == null ? "" : usuario.getFechaNacimiento().toString())

		));

		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setCodigo(eUsuario.getId());
	}


	@Override
	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		if (eUsuario == null) {
			System.err.println("No se encontró el usuario con ID: " + usuario.getCodigo());
			return;
		}

		for (Propiedad prop : eUsuario.getPropiedades()) {
			switch (prop.getNombre()) {
			case "nombre":
				prop.setValor(usuario.getUsuario());
				break;
			case "telefono":
				prop.setValor(usuario.getTelefono());
				break;
			case "email":
				prop.setValor(usuario.getEmail());
				break;
			case "contraseña":
				prop.setValor(usuario.getContraseña());
				break;
			case "saludo":
				prop.setValor(usuario.getSaludo().orElse(""));
				break;
			case "imagenPerfil":
				prop.setValor((usuario.getFotoPerfil() != null) ? usuario.getFotoPerfil().getDescription() : "");
				break;
			case "contactos":
				prop.setValor(obtenerCodigosContactos(usuario.getListaContactos()));
				break;
			case "mensajesEnviados":
				prop.setValor(obtenerCodigosMensajes(usuario.getListaMensajesEnviados()));
				break;
			case "mensajesRecibidos":
				prop.setValor(obtenerCodigosMensajes(usuario.getListaMensajesRecibidos()));
				break;
			case "chats":
				prop.setValor(obtenerCodigosChats(usuario.getListaChats()));
				break;
			case "premium":
				prop.setValor(String.valueOf(usuario.isPremium()));
				break;
			case "fechaNacimiento":
				prop.setValor(usuario.getFechaNacimiento() == null ? "" : usuario.getFechaNacimiento().toString());
				break;
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}

	@Override
	public Usuario recuperarUsuario(int codigo) {
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}

		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);
		if (eUsuario == null) {
			System.err.println("No se encontró el usuario con ID: " + codigo);
			return null;
		}

		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		String telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
		String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
		String rutaImagenPerfil = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagenPerfil");
		String premiumStr = servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium");
		String fechaNacimientoStr = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaNacimiento");

		ImageIcon imagenPerfil = null;
		if (rutaImagenPerfil != null && !rutaImagenPerfil.isEmpty()) {
			imagenPerfil = new ImageIcon();
			imagenPerfil.setDescription(rutaImagenPerfil);
		}

		Usuario usuario = new Usuario(nombre, contraseña, telefono, email, Optional.ofNullable(saludo), imagenPerfil,
				null, null 
		);
		usuario.setCodigo(codigo);

		String fechaRegistroStr = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaRegistro");
		if (fechaRegistroStr != null) {
			usuario.setFechaRegistro(LocalDate.parse(fechaRegistroStr));
		}

		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		String contactos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos");
		usuario.setListaContactos(obtenerContactosDesdeCodigos(contactos));

		String mensajesEnviados = servPersistencia.recuperarPropiedadEntidad(eUsuario, "mensajesEnviados");
		usuario.setListaMensajesEnviados(obtenerMensajesDesdeCodigos(mensajesEnviados));

		String mensajesRecibidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "mensajesRecibidos");
		usuario.setListaMensajesRecibidos(obtenerMensajesDesdeCodigos(mensajesRecibidos));

		String chats = servPersistencia.recuperarPropiedadEntidad(eUsuario, "chats");
		usuario.setListaChats(obtenerChatsDesdeCodigos(chats));


		boolean premium = false;
		if (premiumStr != null) {
			try {
				premium = Boolean.parseBoolean(premiumStr);
			} catch (NumberFormatException e) {
				System.err.println("Error de casting a booleano: " + premiumStr);
			}
		}
		usuario.setPremium(premium);

		LocalDate fechaNacimiento = null;
		if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
			try {
				fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
			} catch (Exception e) {
				System.err.println("Error de casting a fecha" + fechaNacimientoStr);
			}
		}
		usuario.setFechaNacimiento(fechaNacimiento);

		return usuario;
	}

	@Override
	public List<Usuario> recuperarTodosUsuarios() {
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		List<Usuario> usuarios = new LinkedList<>();
		for (Entidad eUsuario : eUsuarios) {
			Usuario u = recuperarUsuario(eUsuario.getId());
			if (u != null) {
				usuarios.add(u);
			}
		}
		return usuarios;
	}

	private String obtenerCodigosContactos(List<Contacto> listaContactos) {
		return listaContactos.stream().map(c -> Integer.toString(c.getCodigo()))
				.collect(java.util.stream.Collectors.joining(" "));
	}

	private List<Contacto> obtenerContactosDesdeCodigos(String codigos) {
		if (codigos == null || codigos.trim().isEmpty())
			return new LinkedList<>();

		return java.util.Arrays.stream(codigos.split(" ")).map(Integer::parseInt).map(codContacto -> {
			Entidad eContacto = servPersistencia.recuperarEntidad(codContacto);
			if (eContacto.getNombre().equals("contactoIndividual")) {
				return AdaptadorContactoIndividualTDS.getUnicaInstancia().recuperarContactoIndividual(codContacto);
			} else if (eContacto.getNombre().equals("grupo")) {
				return AdaptadorGrupoTDS.getUnicaInstancia().recuperarGrupo(codContacto);
			}
			System.err.println("La entidad con código " + codContacto + " no es un contacto válido.");
			return null;
		}).filter(java.util.Objects::nonNull).collect(java.util.stream.Collectors.toCollection(LinkedList::new));
	}

	private String obtenerCodigosMensajes(List<Mensaje> mensajes) {
		return mensajes.stream().map(m -> Integer.toString(m.getCodigo()))
				.collect(java.util.stream.Collectors.joining(" "));
	}

	private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
		if (codigos == null || codigos.trim().isEmpty())
			return new LinkedList<>();

		return java.util.Arrays.stream(codigos.split(" ")).map(Integer::parseInt)
				.map(id -> AdaptadorMensajeTDS.getUnicaInstancia().recuperarMensaje(id))
				.filter(java.util.Objects::nonNull).collect(java.util.stream.Collectors.toCollection(LinkedList::new));
	}

	private String obtenerCodigosChats(List<Chat> chats) {
		return chats.stream().map(c -> Integer.toString(c.getCodigo()))
				.collect(java.util.stream.Collectors.joining(" "));
	}

	private List<Chat> obtenerChatsDesdeCodigos(String codigos) {
		if (codigos == null || codigos.trim().isEmpty())
			return new LinkedList<>();

		return java.util.Arrays.stream(codigos.split(" ")).map(Integer::parseInt)
				.map(id -> AdaptadorChatTDS.getUnicaInstancia().obtenerChat(id)).filter(java.util.Objects::nonNull)
				.collect(java.util.stream.Collectors.toCollection(LinkedList::new));
	}
}
