package umu.tds.apps.estrategia;

import java.util.ArrayList;
import java.util.List;

import umu.tds.apps.estrategia.busqueda.BusquedaMensaje;
import umu.tds.apps.modelo.Mensaje;

/**
 * Gestor que permite encadenar varias estrategias de búsqueda sobre una lista
 * de mensajes.
 */
public class EstrategiaBusquedaMensaje {

    private List<BusquedaMensaje> estrategias = new ArrayList<>();

    /**
     * Registra una nueva estrategia de filtrado.
     *
     * @param estrategia Estrategia de búsqueda a añadir
     */
    public void addEstrategiaBusqueda(BusquedaMensaje estrategia) {
        estrategias.add(estrategia);
    }

    /**
     * Aplica todas las estrategias registradas de manera secuencial.
     *
     * @param mensajes conjunto inicial a examinar
     * @return lista resultante tras aplicar los filtros
     * @throws IllegalArgumentException si {@code mensajes} es {@code null}
     */
    public List<Mensaje> ejecutarBusqueda(List<Mensaje> mensajes) {
        if (mensajes == null) {
            throw new IllegalArgumentException("La lista de mensajes no puede ser nula");
        }
        final List<Mensaje>[] filtrados = new List[]{new ArrayList<>(mensajes)};
        estrategias.forEach(e -> filtrados[0] = e.buscar(filtrados[0]));
        return filtrados[0];
    }

}
