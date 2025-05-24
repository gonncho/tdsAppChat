package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class Contacto {
    private final String id;
    private final String nombre;   // puede ser null si no es contacto
    private final String telefono;
    private final List<Mensaje> mensajes = new ArrayList<>();

    protected Contacto(String nombre, String telefono) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getId()         { return id; }
    public String getNombre()     { return nombre; }
    public String getTelefono()   { return telefono; }

    /** Lista inmutable de mensajes */
    public List<Mensaje> getMensajes() {
        return Collections.unmodifiableList(mensajes);
    }

    /** Añade un mensaje al chat de este contacto */
    public void addMensaje(Mensaje m) {
        if (m != null) mensajes.add(m);
    }
}
