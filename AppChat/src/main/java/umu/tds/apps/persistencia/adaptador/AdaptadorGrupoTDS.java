
package umu.tds.apps.persistencia.adaptador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import beans.Entidad;
import umu.tds.apps.modelo.Mensaje;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.PoolDAO;
import umu.tds.apps.persistencia.dao.IAdaptadorGrupoDAO;

public class AdaptadorGrupoTDS implements IAdaptadorGrupoDAO {

	private static ServicioPersistencia servPersistencia;
	private static AdaptadorGrupoTDS unicaInstancia = null;

	private AdaptadorGrupoTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public static synchronized AdaptadorGrupoTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorGrupoTDS();
		}
		return unicaInstancia;
	}

	private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
		List<Mensaje> mensajes = new LinkedList<>();
		if (codigos == null || codigos.trim().isEmpty())
			return mensajes;

		StringTokenizer st = new StringTokenizer(codigos, " ");
		while (st.hasMoreTokens()) {
			try {
				int codMensaje = Integer.parseInt(st.nextToken());
				Mensaje m = AdaptadorMensajeTDS.getUnicaInstancia().recuperarMensaje(codMensaje);
				if (m != null) {
					mensajes.add(m);
				}
			} catch (NumberFormatException e) {
				System.err.println("Error al convertir código de mensaje: " + e.getMessage());
			}
		}
		return mensajes;
	}

	private String obtenerCodigosMensajes(List<Mensaje> list) {
		return list.stream().map(m -> Integer.toString(m.getCodigo()))
				.collect(java.util.stream.Collectors.joining(" "));
	}

	@Override
	public void registrarGrupo(Grupo grupo) {
		if (grupo.getNombreGrupo() == null || grupo.getCreador() == null) {
			throw new IllegalArgumentException("El nombre del grupo y el creador no pueden ser nulos.");
		}

		Entidad eGrupo = new Entidad();
		eGrupo.setNombre("grupo");

		String rutaImagen = (grupo.getImagenGrupo() != null) ? grupo.getImagenGrupo().getDescription() : "";
		List<Propiedad> propiedades = new ArrayList<>(
				Arrays.asList(new Propiedad("nombreGrupo", grupo.getNombreGrupo()),
						new Propiedad("contactos", getCodigosContactos(grupo.getListaContactos())),
						new Propiedad("creador", String.valueOf(grupo.getCreador().getCodigo())),
						new Propiedad("mensajesEnviados", obtenerCodigosMensajes(grupo.getMensajesEnviados())),
						new Propiedad("imagenGrupo", rutaImagen)));

		eGrupo.setPropiedades(propiedades);

		eGrupo = servPersistencia.registrarEntidad(eGrupo);
		grupo.setCodigo(eGrupo.getId());
	}

	@Override
	public Grupo recuperarGrupo(int codigo) {
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (Grupo) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}

		Entidad eGrupo = servPersistencia.recuperarEntidad(codigo);
		if (eGrupo == null) {
			System.err.println("No se encontró el grupo con código: " + codigo);
			return null;
		}

		String nombreGrupo = servPersistencia.recuperarPropiedadEntidad(eGrupo, "nombreGrupo");
		String contactosCodigos = servPersistencia.recuperarPropiedadEntidad(eGrupo, "contactos");
		int creadorCodigo = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eGrupo, "creador"));

		String rutaImagen = servPersistencia.recuperarPropiedadEntidad(eGrupo, "imagenGrupo");

		String mensajesEnviadosCodigos = servPersistencia.recuperarPropiedadEntidad(eGrupo, "mensajesEnviados");

		Usuario creador = AdaptadorUsuarioTDS.getUnicaInstancia().recuperarUsuario(creadorCodigo);
		if (creador == null) {
			throw new IllegalStateException("El creador del grupo no existe: " + creadorCodigo);
		}

		List<ContactoIndividual> contactos = getContactosDesdeCodigos(contactosCodigos);

		ImageIcon imagenGrupo = null;
		if (rutaImagen != null && !rutaImagen.trim().isEmpty()) {
			try {
				imagenGrupo = new ImageIcon(rutaImagen);
				if (imagenGrupo.getIconWidth() <= 0) {
					System.err.println("No se pudo cargar la imagen del grupo desde: " + rutaImagen);
					imagenGrupo = null;
				}
			} catch (Exception e) {
				System.err.println("Error al cargar la imagen del grupo: " + e.getMessage());
				imagenGrupo = null;
			}
		}

		Grupo grupo = new Grupo(nombreGrupo, contactos, creador, imagenGrupo);
		grupo.setCodigo(codigo);

		PoolDAO.getUnicaInstancia().addObjeto(codigo, grupo);

		if (mensajesEnviadosCodigos != null && !mensajesEnviadosCodigos.isEmpty()) {
			List<Mensaje> mensajesEnviados = obtenerMensajesDesdeCodigos(mensajesEnviadosCodigos);
			for (Mensaje mensaje : mensajesEnviados) {
				grupo.addMensajeEnviado(mensaje);
			}
		}

		return grupo;
	}

	@Override
	public void modificarGrupo(Grupo grupo) {
		// 1) Recuperar la entidad del grupo
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		if (eGrupo == null) {
			System.err.println("No se encontró el grupo con código: " + grupo.getCodigo());
			return;
		}

		// 2) Verificar si ya existe la propiedad mensajesEnviados
		boolean tienePropiedadMensajes = false;
		for (Propiedad prop : eGrupo.getPropiedades()) {
			if (prop.getNombre().equals("mensajesEnviados")) {
				tienePropiedadMensajes = true;
				break;
			}
		}

		if (!tienePropiedadMensajes) {
			List<Propiedad> propiedadesNuevas = new ArrayList<>(eGrupo.getPropiedades());

			propiedadesNuevas
					.add(new Propiedad("mensajesEnviados", obtenerCodigosMensajes(grupo.getMensajesEnviados())));

			eGrupo.setPropiedades(propiedadesNuevas);

			servPersistencia.modificarEntidad(eGrupo);
		} else {
			for (Propiedad prop : eGrupo.getPropiedades()) {
				switch (prop.getNombre()) {
				case "nombreGrupo":
					prop.setValor(grupo.getNombreGrupo());
					break;
				case "contactos":
					prop.setValor(getCodigosContactos(grupo.getListaContactos()));
					break;
				case "creador":
					prop.setValor(String.valueOf(grupo.getCreador().getCodigo()));
					break;
				case "mensajesEnviados":
					prop.setValor(obtenerCodigosMensajes(grupo.getMensajesEnviados()));
					break;
				case "imagenGrupo":
					String desc = (grupo.getImagenGrupo() != null) ? grupo.getImagenGrupo().getDescription() : "";
					prop.setValor(desc);
					break;
				default:
					break;
				}
				servPersistencia.modificarPropiedad(prop);
			}
		}

	}

	@Override
	public List<Grupo> recuperarTodosGrupos() {
		List<Entidad> eGrupos = servPersistencia.recuperarEntidades("grupo");
		List<Grupo> grupos = new LinkedList<>();
		for (Entidad eGrupo : eGrupos) {
			Grupo grupo = recuperarGrupo(eGrupo.getId());
			if (grupo != null) {
				grupos.add(grupo);
			}
		}
		return grupos;
	}

	private String getCodigosContactos(List<ContactoIndividual> listaContactos) {
		return listaContactos.stream().map(c -> Integer.toString(c.getCodigo()))
				.collect(java.util.stream.Collectors.joining(" "));
	}

	private List<ContactoIndividual> getContactosDesdeCodigos(String codigos) {
		if (codigos == null || codigos.trim().isEmpty())
			return new LinkedList<>();

		return java.util.Arrays.stream(codigos.split(" ")).map(Integer::parseInt)
				.map(id -> AdaptadorContactoIndividualTDS.getUnicaInstancia().recuperarContactoIndividual(id))
				.filter(ci -> ci != null && ci.getUsuario() != null)
				.collect(java.util.stream.Collectors.toCollection(LinkedList::new));
	}

}
