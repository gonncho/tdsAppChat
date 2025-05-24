package modelo;

import java.util.List;

public class FactoriaContacto {

    public static ContactoIndividual crearIndividual(String nombre, String telefono) {
        return new ContactoIndividual(nombre, telefono);
    }

    public static Grupo crearGrupo(String nombre, String imagen, List<ContactoIndividual> miembros) {
        Grupo.Builder b = new Grupo.Builder(nombre);
        if (imagen != null && !imagen.isEmpty()) {
            b.imagen(imagen);
        }
        if (miembros != null) {
            miembros.forEach(b::addMiembro);
        }
        return b.build();
    }
}
