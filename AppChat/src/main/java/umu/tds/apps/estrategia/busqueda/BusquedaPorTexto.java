package umu.tds.apps.estrategia.busqueda;

import java.util.List;

import umu.tds.apps.modelo.Mensaje;

public class BusquedaPorTexto implements BusquedaMensaje {

	private String textoBuscado;

	public BusquedaPorTexto(String textoBuscado) {
		this.textoBuscado = textoBuscado;
	}

	public List<Mensaje> buscar(List<Mensaje> mensajes) {
		if (textoBuscado == null || textoBuscado.isEmpty()) {
			return mensajes;
		}
		List<Mensaje> resultado = new java.util.ArrayList<>();
		for (Mensaje m : mensajes) {
			if (m.getTexto().toLowerCase().contains(textoBuscado.toLowerCase())) {
				resultado.add(m);
			}
		}
		return resultado;
	}

}
