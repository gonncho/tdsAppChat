package persistencia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Usuario;

public class RepositorioUsuarios {

    public static final RepositorioUsuarios INSTANCE = new RepositorioUsuarios();

    // Por defecto usamos la implementación en memoria para
    // poder probar la aplicación sin la capa de persistencia TDS.
    private final DAOUsuario dao = new DAOUsuarioMemoria();
    private final Map<String,Usuario> usuarios = new HashMap<>();

    private RepositorioUsuarios() { }

    /** Debe llamarse al arrancar la app */
    public void cargarUsuarios() {
        List<Usuario> all = dao.findAll();
        all.forEach(u -> usuarios.put(u.getTelefono(), u));
    }

    public void add(Usuario u) {
        dao.save(u);
        usuarios.put(u.getTelefono(), u);
    }

    public Usuario getByTelefono(String telefono) {
        return usuarios.get(telefono);
    }

    public boolean login(String telefono, String pwd) {
        Usuario u = getByTelefono(telefono);
        return u != null && u.getContrasenia().equals(pwd);
    }
}
