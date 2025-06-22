
package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Chat {

	private int codigo;
	private Usuario usuario;
	private Usuario otroUsuario;
	private List<Mensaje> mensajes;
	
	// Crea un chat entre dos usuarios e inicializa la lista de mensajes
	
	public Chat(Usuario usuario, Usuario otroUsuarioChat) {
		this.usuario = usuario;
		this.otroUsuario = otroUsuarioChat;
		this.mensajes = new ArrayList<>();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getOtroUsuario() {
		return otroUsuario;
	}

	public void setOtroUsuario(Usuario otroUsuarioChat) {
		this.otroUsuario = otroUsuarioChat;
	}
	
	// Verifica si el chat pertenece al usuario pasado como parámetro
	
	public boolean contieneUsuario(Usuario otroUsuario) {
		return usuario.equals(otroUsuario) || this.otroUsuario.equals(otroUsuario);
	}
	
	// Devuelve una copia de la lista de mensajes
	
	public List<Mensaje> getMensajes() {
		return new ArrayList<>(mensajes);
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
	
	// Inserta un mensaje manteniendo el orden cronológico

	public void addMensaje(Mensaje mensaje) {
		Comparator<Mensaje> comp = Comparator.comparing(Mensaje::getFecha).thenComparing(Mensaje::getHora);
		int pos = java.util.Collections.binarySearch(mensajes, mensaje, comp);
		if (pos < 0) {
			pos = -pos - 1;
		}
		mensajes.add(pos, mensaje);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Chat chat = (Chat) obj;
		return (usuario.equals(chat.usuario) && otroUsuario.equals(chat.otroUsuario))
				|| (usuario.equals(chat.otroUsuario) && otroUsuario.equals(chat.usuario));
	}

	@Override
	public int hashCode() {
		return Objects.hash(usuario, otroUsuario);
	}

}
