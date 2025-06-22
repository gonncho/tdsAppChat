
package umu.tds.apps.modelo;

public class Contacto {

	private int codigo;
	private String nombre;
	
	public Contacto(String nombre) {
		this.nombre = nombre;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre != null ? nombre.trim() : null;
	}
}
