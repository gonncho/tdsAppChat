package persistencia;

import java.util.List;
import modelo.Usuario;

public interface DAOUsuario {
    void save(Usuario u);
    Usuario findByTelefono(String telefono);
    List<Usuario> findAll();
}
