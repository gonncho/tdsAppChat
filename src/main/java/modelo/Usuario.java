package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Usuario {

    // — Campos inmutables —
    private final String id;
    private final String nombre;
    private final String email;
    private final String telefono;
    private final String contrasenia;
    private final LocalDate fechaNacimiento;  // opcional
    private final LocalDate fechaRegistro;    // alta en el sistema
    private final String  saludo;             // opcional
    private final String  imagenPerfil;       // opcional (ruta o URL)
    private final boolean isPremium;

    // — Campos mutables (listas) —
    private final List<Contacto> contactos = new ArrayList<>();
    private final List<Mensaje> mensajes   = new ArrayList<>();

    private Usuario(Builder b) {
        this.id              = UUID.randomUUID().toString();
        this.nombre          = b.nombre;
        this.email           = b.email;
        this.telefono        = b.telefono;
        this.contrasenia     = b.contrasenia;
        this.fechaNacimiento = b.fechaNacimiento;
        this.fechaRegistro   = b.fechaRegistro != null ? b.fechaRegistro : LocalDate.now();
        this.saludo          = b.saludo;
        this.imagenPerfil    = b.imagenPerfil;
        this.isPremium       = b.isPremium;
    }

    // — Accesores —
    public String getId()               { return id; }
    public String getNombre()           { return nombre; }
    public String getEmail()            { return email; }
    public String getTelefono()         { return telefono; }
    public String getContrasenia()      { return contrasenia; }
    public LocalDate getFechaNacimiento(){ return fechaNacimiento; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public String getSaludo()           { return saludo; }
    public String getImagenPerfil()     { return imagenPerfil; }
    public boolean isPremium()          { return isPremium; }

    /** Lista inmutable de contactos */
    public List<Contacto> getContactos() {
        return Collections.unmodifiableList(contactos);
    }
    /** Lista inmutable de mensajes */
    public List<Mensaje> getMensajes() {
        return Collections.unmodifiableList(mensajes);
    }

    // — Métodos de negocio para añadir relaciones —
    public void addContacto(Contacto c) {
        if (c != null && !contactos.contains(c)) {
            contactos.add(c);
        }
    }

    public void addMensaje(Mensaje m) {
        if (m != null) {
            mensajes.add(m);
        }
    }

    // — Builder interno —
    public static class Builder {
        // obligatorios
        private final String nombre;
        private final String email;
        private final String telefono;
        private final String contrasenia;

        // opcionales con valores por defecto
        private LocalDate fechaNacimiento = null;
        private LocalDate fechaRegistro  = null;
        private String   saludo          = "";
        private String   imagenPerfil    = null;
        private boolean  isPremium       = false;

        public Builder(String nombre, String email, String telefono, String contrasenia) {
            this.nombre      = nombre;
            this.email       = email;
            this.telefono    = telefono;
            this.contrasenia = contrasenia;
        }

        public Builder fechaNacimiento(LocalDate fecha) {
            this.fechaNacimiento = fecha;
            return this;
        }

        public Builder fechaRegistro(LocalDate fecha) {
            this.fechaRegistro = fecha;
            return this;
        }

        public Builder saludo(String saludo) {
            this.saludo = saludo;
            return this;
        }

        public Builder imagenPerfil(String rutaOUrl) {
            this.imagenPerfil = rutaOUrl;
            return this;
        }

        public Builder premium(boolean premium) {
            this.isPremium = premium;
            return this;
        }

        public Usuario build() {
            // aquí podrías validar formatos/email/password, lanzar IllegalArgumentException, etc.
            return new Usuario(this);
        }
    }
}
