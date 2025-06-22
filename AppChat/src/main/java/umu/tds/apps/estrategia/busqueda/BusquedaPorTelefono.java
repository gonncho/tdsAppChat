package umu.tds.apps.estrategia.busqueda;

import java.util.List;

import umu.tds.apps.modelo.Mensaje;

public class BusquedaPorTelefono implements BusquedaMensaje {

	private String telefonoBuscado;

	public BusquedaPorTelefono(String telefonoBuscado) {
		this.telefonoBuscado = telefonoBuscado;
	}

	@Override
	public List<Mensaje> buscar(List<Mensaje> mensajes) {
		if (telefonoBuscado == null || telefonoBuscado.isEmpty()) {
			return mensajes;
		}
		List<Mensaje> resultado = new java.util.ArrayList<>();
		for (Mensaje m : mensajes) {
			if (m.getEmisor().getTelefono().contains(telefonoBuscado)
					|| m.getReceptor().getTelefono().contains(telefonoBuscado)) {
				resultado.add(m);
			}
		}
		return resultado;
	}
}
