// Clase para manejar la estrategia de descuento
package umu.tds.apps.estrategia;

import umu.tds.apps.estrategia.descuento.Descuento;

/**
 * Permite configurar y aplicar diferentes estrategias de descuento sobre un precio.
 */
public class EstrategiaDescuento {
    private Descuento estrategiaDescuento;

    
    /**
     * Define la estrategia de cálculo de descuento que se utilizará.
     *
     * @param estrategiaDescuento estrategia a aplicar
     */
    public void setEstrategiaDescuento(Descuento estrategiaDescuento) {
        this.estrategiaDescuento = estrategiaDescuento;
    }

    /**
     * Obtiene el precio con descuento o el mismo valor si no se ha configurado
     * ninguna estrategia.
     *
     * @param precioOriginal importe de partida
     * @return precio resultante tras aplicar la estrategia
     */
    public double calcularPrecioFinal(double precioOriginal) {
        return java.util.Optional.ofNullable(estrategiaDescuento)
                .map(e -> e.calcularDescuento(precioOriginal))
                .orElse(precioOriginal);
    }
}
