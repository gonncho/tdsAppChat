// Implementación específica para el descuento por fecha
package umu.tds.apps.estrategia.descuento;

import java.time.LocalDate;

public class DescuentoFecha implements Descuento {
	private LocalDate fechaRegistro;

	public DescuentoFecha(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Override
	public double calcularDescuento(double precio) {
		LocalDate limite = LocalDate.now().minusDays(7);
		if (fechaRegistro.isAfter(limite)) {
			return precio * 0.75;
		}
		return precio;
	}
}