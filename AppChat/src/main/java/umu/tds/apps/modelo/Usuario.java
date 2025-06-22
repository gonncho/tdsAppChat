
package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import umu.tds.apps.estrategia.EstrategiaBusquedaMensaje;
import umu.tds.apps.estrategia.busqueda.BusquedaPorContacto;
import umu.tds.apps.estrategia.busqueda.BusquedaPorTelefono;
import umu.tds.apps.estrategia.busqueda.BusquedaPorTexto;
import umu.tds.apps.persistencia.adaptador.AdaptadorUsuarioTDS;

public class Usuario {

	private String usuario;
	private String contraseña;
	private String telefono;
	private String email;
	private LocalDate fechaNacimiento;
	private ImageIcon fotoPerfil;
	private Optional<String> saludo;
	private List<Contacto> listaContactos;
	private List<Mensaje> listaMensajesEnviados;
	private List<Mensaje> listaMensajesRecibidos;
	private List<Chat> listaChats;
	private boolean premium;
	private int codigo;
	private LocalDate fechaRegistro;

	public Usuario(String usuario, String contraseña, String telefono, String email, Optional<String> saludo,
			ImageIcon imagenPerfil, LocalDate fechaNacimiento, LocalDate fechaRegistro) {
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.telefono = telefono;
		this.email = email;
		this.saludo = saludo;
		this.fotoPerfil = imagenPerfil;
		this.fechaNacimiento = fechaNacimiento;
		inicializar();
	}

	private void inicializar() {
		this.fechaRegistro = LocalDate.now();
		this.listaContactos = new LinkedList<>();
		this.listaMensajesEnviados = new LinkedList<>();
		this.listaMensajesRecibidos = new LinkedList<>();
		this.listaChats = new LinkedList<>();
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contaseña) {
		this.contraseña = contaseña;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Optional<String> getSaludo() {
		return saludo;
	}

	public void setSaludo(Optional<String> saludo) {
		this.saludo = saludo;
	}

	public ImageIcon getFotoPerfil() {
		return fotoPerfil;
	}

	public void setImagenPerfil(ImageIcon imagenPerfil) {
		this.fotoPerfil = imagenPerfil;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	public List<Chat> getListaChats() {
		return listaChats;
	}

	public void setListaChats(List<Chat> listaChats) {
		this.listaChats = listaChats;
	}

	public boolean isClaveValida(String contraseña) {
		return this.contraseña.equals(contraseña);
	}

	public List<Grupo> getGrupos() {
		return listaContactos.stream().filter(c -> c instanceof Grupo).map(c -> (Grupo) c).collect(Collectors.toList());
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public List<Contacto> getListaContactos() {
		return new LinkedList<Contacto>(listaContactos); // comprobar
	}

	public void setListaContactos(List<Contacto> listaContactos) {
		this.listaContactos = listaContactos;
	}

	public List<Mensaje> getListaMensajesEnviados() {
		return Collections.unmodifiableList(listaMensajesEnviados);
	}

	public void setListaMensajesEnviados(List<Mensaje> listaMensajesEnviados) {
		this.listaMensajesEnviados = listaMensajesEnviados;
	}

	public List<Mensaje> getListaMensajesRecibidos() {
		return Collections.unmodifiableList(listaMensajesRecibidos);
	}

	public void setListaMensajesRecibidos(List<Mensaje> listaMensajesRecibidos) {
		this.listaMensajesRecibidos = listaMensajesRecibidos;
	}
	
	public long getNumeroMensajesUltimoMes() {
		LocalDate ahora = LocalDate.now();

		LocalDate primerDiaDelMes = ahora.withDayOfMonth(1);
		LocalDate ultimoDiaDelMes = primerDiaDelMes.plusMonths(1).minusDays(1);

		return listaMensajesEnviados.stream().filter(mensaje -> {
			LocalDate fechaEnvio = mensaje.getFecha();
			return !fechaEnvio.isBefore(primerDiaDelMes) && !fechaEnvio.isAfter(ultimoDiaDelMes);
		}).count();
	}


	public boolean añadirContacto(Contacto contacto) {
		boolean existe = listaContactos.stream().anyMatch(contacto::equals);
		if (!existe) {
			listaContactos.add(contacto);
			AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(this);
			return true;
		}
		return false;
	}

	public void añadirChat(Chat chat) {
		if (!listaChats.contains(chat)) {
			listaChats.add(chat);
		}
	}

	
	public Chat obtenerChatCon(Usuario otroUsuario) {
		return listaChats.stream().filter(ch -> ch.contieneUsuario(otroUsuario)).findFirst().orElse(null);
	}
	
	
	
	// Devuelve una lista de mensajes entre este usuario y otro usuario, ordenados por fecha y hora.
	public List<Mensaje> obtenerMensajesCon(Usuario otroUsuario) {
		List<Mensaje> mensajes = new LinkedList<>();

		listaMensajesEnviados.stream().filter(m -> m.getReceptor().equals(otroUsuario)).forEach(mensajes::add);

		listaMensajesRecibidos.stream().filter(m -> m.getEmisor().equals(otroUsuario)).forEach(mensajes::add);

		mensajes.sort((m1, m2) -> {
			int cmp = m1.getFecha().compareTo(m2.getFecha());
			if (cmp == 0) {
				return m1.getHora().compareTo(m2.getHora());
			}
			return cmp;
		});

		return mensajes;
	}
	
	// Crea un nuevo chat con otro usuario. Si ya existe un chat con ese usuario, lo devuelve.
	public Chat crearChatCon(Usuario otroUsuario) {
		Chat chatExistente = obtenerChatCon(otroUsuario);
		if (chatExistente != null) {
			return chatExistente; 
		}

		Chat nuevoChat = new Chat(this, otroUsuario);

		this.añadirChat(nuevoChat);
		otroUsuario.añadirChat(nuevoChat);

		return nuevoChat;
	}
	
	// Devuelve el contacto individual asociado a otro usuario, si existe.
	public Contacto obtenerContactoCon(Usuario otroUsuario) {
		return listaContactos.stream().filter(c -> {
			if (c instanceof ContactoIndividual) {
				return ((ContactoIndividual) c).getUsuario().equals(otroUsuario);
			}
			return false;
		}).findFirst().orElse(null);
	}

	public void activarPremium() {
		this.premium = true;
	}

	public void desactivarPremium() {
		this.premium = false;
	}

	public Chat getChatMensajes(Usuario otroUsuario) {
		return listaChats.stream().filter(chat -> chat.getOtroUsuario().equals(otroUsuario)).findFirst().orElse(null);
	}

	public void añadirMensajeEnviado(Mensaje mensaje) {
		this.listaMensajesEnviados.add(mensaje);
	}

	public void añadirMensajeRecibido(Mensaje mensaje) {
		this.listaMensajesRecibidos.add(mensaje);
	}


	public List<Mensaje> filtrarMensajes(String texto, String telefono, String contacto) {
		EstrategiaBusquedaMensaje estrategiaBusqueda = new EstrategiaBusquedaMensaje();

		if (texto != null && !texto.isEmpty()) {
			estrategiaBusqueda.addEstrategiaBusqueda(new BusquedaPorTexto(texto));
		}
		if (telefono != null && !telefono.isEmpty()) {
			estrategiaBusqueda.addEstrategiaBusqueda(new BusquedaPorTelefono(telefono));
		}
		if (contacto != null && !contacto.isEmpty()) {
			estrategiaBusqueda.addEstrategiaBusqueda(new BusquedaPorContacto(this, contacto));
		}

		List<Mensaje> mensajes = new ArrayList<>();
		mensajes.addAll(this.getListaMensajesEnviados());
		mensajes.addAll(this.getListaMensajesRecibidos());

		return estrategiaBusqueda.ejecutarBusqueda(mensajes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(telefono);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(telefono, other.telefono);
	}

	

}
