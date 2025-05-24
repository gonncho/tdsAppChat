package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grupo extends Contacto {
    private final String imagen;  // ruta o URL (opcional)
    private final List<ContactoIndividual> miembros;

    private Grupo(Builder b) {
        super(b.nombre, null);  // nombre de grupo; teléfono no aplica
        this.imagen   = b.imagen;
        this.miembros = Collections.unmodifiableList(new ArrayList<>(b.miembros));
    }

    public String getImagen() {
        return imagen;
    }

    public List<ContactoIndividual> getMiembros() {
        return miembros;
    }

    public static class Builder {
        private final String nombre;
        private String imagen = null;
        private List<ContactoIndividual> miembros = new ArrayList<>();

        public Builder(String nombre) {
            this.nombre = nombre;
        }

        public Builder imagen(String rutaOUrl) {
            this.imagen = rutaOUrl;
            return this;
        }

        public Builder addMiembro(ContactoIndividual ci) {
            if (ci != null) miembros.add(ci);
            return this;
        }

        public Grupo build() {
            return new Grupo(this);
        }
    }

    @Override
    public String toString() {
        return "Grupo: " + super.getNombre();
    }
}
