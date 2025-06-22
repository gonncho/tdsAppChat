
package umu.tds.apps.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;

public class Grupo extends Contacto {

	private String nombre;
	private List<ContactoIndividual> listaContactos;
	private Usuario creador;
	private ImageIcon imagenGrupo;
	private List<Mensaje> mensajesEnviados;

	public Grupo(String nombre, List<ContactoIndividual> listaContactos, Usuario creador, ImageIcon imagenGrupo) {
		super(nombre); 
		this.nombre = nombre;
		this.listaContactos = new ArrayList<>(listaContactos); 
		this.creador = creador;
		this.imagenGrupo = imagenGrupo;
		mensajesEnviados = new ArrayList<>();
	}

	public Grupo(String nombreGrupo) {
		super(nombreGrupo);
		this.nombre = nombreGrupo;
		this.listaContactos = new ArrayList<>();
	}

	public List<ContactoIndividual> getListaContactos() {
		return Collections.unmodifiableList(listaContactos);
	}

	public void setListaContactos(List<ContactoIndividual> listaContactos) {
		this.listaContactos = new ArrayList<>(listaContactos);
	}

	public String getNombreGrupo() {
		return nombre;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombre = nombreGrupo;
	}

	public Usuario getCreador() {
		return creador;
	}

	public void setCreador(Usuario creador) {
		this.creador = creador;
	}

	public ImageIcon getImagenGrupo() {
		return imagenGrupo;
	}

	public List<Mensaje> getMensajesEnviados() {
		return Collections.unmodifiableList(mensajesEnviados);
	}

	public void addMensajeEnviado(Mensaje mensaje) {
		mensajesEnviados.add(mensaje);
	}

	public void setImagenGrupo(ImageIcon imagenGrupo) {
		this.imagenGrupo = imagenGrupo;
	}

	public void agregarContacto(ContactoIndividual contacto) {
		if (listaContactos.stream().noneMatch(contacto::equals)) {
			listaContactos.add(contacto);
		}
	}

	public void eliminarContacto(ContactoIndividual contacto) {
		listaContactos.removeIf(contacto::equals);
	}
}
