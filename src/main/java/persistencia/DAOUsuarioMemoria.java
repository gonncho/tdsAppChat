package persistencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Usuario;

/**
 * Implementación de DAOUsuario que almacena los datos en memoria.
 * Para realizar pruebas sin usar el servicio de persistencia TDS.
 */
public class DAOUsuarioMemoria implements DAOUsuario {
    private final Map<String, Usuario> datos = new HashMap<>();

    @Override
    public void save(Usuario u) {
        datos.put(u.getTelefono(), u);
    }

    @Override
    public Usuario findByTelefono(String telefono) {
        return datos.get(telefono);
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(datos.values());
    }
}