package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import modelo.*;
import persistencia.RepositorioUsuarios;

/**
 * Controlador principal de la aplicación.
 * Simplifica el manejo de usuarios, contactos y mensajes.
 * Por ahora no implementa persistencia con TDS, solo usa un
 * repositorio en memoria.
 */
public class AppChat {

    public static final AppChat INSTANCE = new AppChat();

    private final RepositorioUsuarios repo = RepositorioUsuarios.INSTANCE;
    private Usuario usuarioActual;

    private AppChat() {}

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @return true si se registra con éxito o false si el teléfono ya existe.
     */
    public boolean registrarUsuario(String nombre,
                                    String email,
                                    String telefono,
                                    String contrasenia,
                                    LocalDate fechaNacimiento,
                                    String imagenPerfil,
                                    String saludo) {
        if (repo.getByTelefono(telefono) != null) {
            return false; // ya existe
        }
        Usuario u = new Usuario.Builder(nombre, email, telefono, contrasenia)
                            .fechaNacimiento(fechaNacimiento)
                            .saludo(saludo)
                            .imagenPerfil(imagenPerfil)
                            .build();
        repo.add(u);
        return true;
    }

    /**
     * Comprueba las credenciales y establece el usuario actual.
     */
    public boolean login(String telefono, String contrasenia) {
        boolean ok = repo.login(telefono, contrasenia);
        usuarioActual = ok ? repo.getByTelefono(telefono) : null;
        return ok;
    }

    public void logout() {
        usuarioActual = null;
    }

    /**
     * Añade un contacto a la agenda del usuario actual.
     * Devuelve el contacto creado o null si el teléfono no pertenece
     * a ningún usuario existente.
     */
    public ContactoIndividual agregarContacto(String nombre, String telefono) {
        if (usuarioActual == null) return null;
        Usuario u = repo.getByTelefono(telefono);
        if (u == null) return null;
        ContactoIndividual ci = FactoriaContacto.crearIndividual(nombre, telefono);
        usuarioActual.addContacto(ci);
        return ci;
    }

    /**
     * Crea un grupo de contactos para el usuario actual.
     */
    public Grupo crearGrupo(String nombre, String imagen, List<ContactoIndividual> miembros) {
        if (usuarioActual == null) return null;
        Grupo g = FactoriaContacto.crearGrupo(nombre, imagen, miembros);
        usuarioActual.addContacto(g);
        return g;
    }

    /**
     * Envía un mensaje al contacto indicado. Si el contacto es un grupo se
     * añade el mensaje al grupo; en una versión completa también se enviaría a
     * cada miembro por separado.
     */
    public Mensaje enviarMensajeContacto(Contacto contacto,
                                         String texto,
                                         int emojiIndex,
                                         Mensaje.Tipo tipo) {
        if (usuarioActual == null || contacto == null) return null;
        List<String> emotis = new ArrayList<>();
        if (emojiIndex >= 0) {
            emotis.add(Integer.toString(emojiIndex));
        }
        Mensaje m = new Mensaje.Builder(texto, LocalDateTime.now(), tipo)
                            .emoticonos(emotis)
                            .build();
        usuarioActual.addMensaje(m);
        contacto.addMensaje(m);
        return m;
    }
}
