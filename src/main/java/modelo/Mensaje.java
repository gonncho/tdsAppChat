package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Mensaje {

    public enum Tipo { ENVIADO, RECIBIDO }

    private final String id;
    private final String contenido;
    private final LocalDateTime fechaEnvio;
    private final Tipo tipo;
    private final List<String> emoticonos;

    private Mensaje(Builder b) {
        this.id         = UUID.randomUUID().toString();
        this.contenido  = b.contenido;
        this.fechaEnvio = b.fechaEnvio;
        this.tipo       = b.tipo;
        this.emoticonos = Collections.unmodifiableList(new ArrayList<>(b.emoticonos));
    }

    public String getId()                { return id; }
    public String getContenido()         { return contenido; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public Tipo getTipo()                { return tipo; }
    public List<String> getEmoticonos()  { return emoticonos; }

    public static class Builder {
        private final String contenido;
        private final LocalDateTime fechaEnvio;
        private final Tipo tipo;
        private List<String> emoticonos = new ArrayList<>();

        public Builder(String contenido, LocalDateTime fechaEnvio, Tipo tipo) {
            this.contenido  = contenido;
            this.fechaEnvio = fechaEnvio;
            this.tipo       = tipo;
        }

        public Builder emoticonos(List<String> lista) {
            if (lista != null) emoticonos = lista;
            return this;
        }

        public Mensaje build() {
            return new Mensaje(this);
        }
    }
}
