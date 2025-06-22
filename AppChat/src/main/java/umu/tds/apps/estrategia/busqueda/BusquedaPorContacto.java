package umu.tds.apps.estrategia.busqueda;

import java.util.List;

import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;

public class BusquedaPorContacto implements BusquedaMensaje {

	private String contactoBuscado;
	private Usuario usuarioActual;

	public BusquedaPorContacto(Usuario usuarioActual, String contactoBuscado) {
		this.usuarioActual = usuarioActual;
		this.contactoBuscado = contactoBuscado;
	}

	@Override
	public List<Mensaje> buscar(List<Mensaje> mensajes) {
		if (contactoBuscado == null || contactoBuscado.isEmpty()) {
			return mensajes;
		}

		List<Mensaje> resultado = new java.util.ArrayList<>();
		for (Mensaje m : mensajes) {
			Usuario otroUsuario = m.getEmisor().equals(usuarioActual) ? m.getReceptor() : m.getEmisor();
			Contacto contacto = usuarioActual.obtenerContactoCon(otroUsuario);
			if (contacto instanceof ContactoIndividual) {
				ContactoIndividual ci = (ContactoIndividual) contacto;
				String nombreContacto = ci.getNombre();
				boolean matches = nombreContacto.toLowerCase().contains(contactoBuscado.toLowerCase());
				if (matches) {
					resultado.add(m);
				}
			} 
		}
		return resultado;
	}

}