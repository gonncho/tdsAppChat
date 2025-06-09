package persistencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Usuario;

/**
 * Implementación sencilla de DAOUsuario que guarda los datos en memoria.
 * Permite probar la lógica de la aplicación sin depender de la capa TDS.
 */
public class DAOUsuarioMemoria implements DAOUsuario {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public void save(Usuario u) {
        usuarios.put(u.getTelefono(), u);
    }

    @Override
    public Usuario findByTelefono(String telefono) {
        return usuarios.get(telefono);
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios.values());
    }
}
