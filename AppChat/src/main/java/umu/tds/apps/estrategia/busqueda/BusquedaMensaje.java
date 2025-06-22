package umu.tds.apps.estrategia.busqueda;

import java.util.List;

import umu.tds.apps.modelo.Mensaje;

import java.util.Collections;

public interface BusquedaMensaje {

    List<Mensaje> buscar(List<Mensaje> mensajes);
    default List<Mensaje> buscarSeguros(List<Mensaje> mensajes) {
        return mensajes == null ? Collections.emptyList() : buscar(mensajes);
    }
}