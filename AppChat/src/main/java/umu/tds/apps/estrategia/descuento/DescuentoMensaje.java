// Implementación específica para el descuento por mensajes
package umu.tds.apps.estrategia.descuento;

public class DescuentoMensaje implements Descuento {
	private int cantidadMensajes;

	public DescuentoMensaje(int cantidadMensajes) {
		this.cantidadMensajes = cantidadMensajes;
	}

	@Override
	public double calcularDescuento(double precio) {
		double factor;
		if (cantidadMensajes > 10) {
			factor = 0.65;
		} else if (cantidadMensajes > 7) {
			factor = 0.7;
		} else if (cantidadMensajes > 5) {
			factor = 0.75;
		} else {
			factor = 1.0;
		}
		return precio * factor;
	}
}
