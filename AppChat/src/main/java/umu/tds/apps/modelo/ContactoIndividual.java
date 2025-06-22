
package umu.tds.apps.modelo;

public class ContactoIndividual extends Contacto {

	private String telefono;
	private Usuario usuario;

	public ContactoIndividual(String nombre, String telefono, Usuario usuario) {
		super(nombre);
		this.telefono = telefono;
		this.usuario = usuario;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono != null ? telefono.trim() : null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
