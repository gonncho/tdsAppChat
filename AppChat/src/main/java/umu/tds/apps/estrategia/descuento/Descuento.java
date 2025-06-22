package umu.tds.apps.estrategia.descuento;

public interface Descuento {

	public double calcularDescuento(double precio);

	default double aplicar(double precio) {
		return calcularDescuento(precio);
	}
}
