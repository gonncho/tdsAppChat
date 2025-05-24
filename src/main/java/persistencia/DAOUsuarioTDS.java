package persistencia;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;
import tds.driver.FactoriaDAO_TDS;
import tds.driver.Entidad;
import tds.driver.Propiedad;
import tds.driver.ServPersistencia;

public class DAOUsuarioTDS implements DAOUsuario {

    private final ServPersistencia serv = FactoriaDAO_TDS.getInstancia().getPersistencia();

    @Override
    public void save(Usuario u) {
        Entidad e = serv.recuperarEntidad("Usuario", u.getId());
        if (e == null) {
            e = serv.crearEntidad("Usuario");
            u.setId(e.getId());           // forzar ID en el dominio
        }
        serv.modificarPropiedad(e, "nombre",          u.getNombre());
        serv.modificarPropiedad(e, "email",           u.getEmail());
        serv.modificarPropiedad(e, "telefono",        u.getTelefono());
        serv.modificarPropiedad(e, "contrasenia",     u.getContrasenia());
        serv.modificarPropiedad(e, "fechaNacimiento", u.getFechaNacimiento().toString());
        serv.modificarPropiedad(e, "imagenPerfil",    u.getImagenPerfil());
        serv.modificarPropiedad(e, "isPremium",       Boolean.toString(u.isPremium()));
        // contactos y mensajes se guardan aparte (véase RepositorioUsuarios)
    }

    @Override
    public Usuario findByTelefono(String telefono) {
        // suponer que hay un método en ServPersistencia para buscar por propiedad
        Entidad e = serv.recuperarEntidadPorPropiedad("Usuario", "telefono", telefono);
        return (e == null) ? null : recuperarUsuario(e.getId());
    }

    @Override
    public List<Usuario> findAll() {
        return serv.recuperarEntidades("Usuario").stream()
                   .map(entidad -> recuperarUsuario(entidad.getId()))
                   .collect(Collectors.toList());
    }

    private Usuario recuperarUsuario(String id) {
        // Pool evita recursiones infinitas
        if (PoolUsuarios.INSTANCE.contains(id)) {
            return PoolUsuarios.INSTANCE.get(id);
        }
        Entidad e = serv.recuperarEntidad("Usuario", id);
        if (e == null) return null;

        String nombre  = serv.recuperarPropiedad(e, "nombre");
        String email   = serv.recuperarPropiedad(e, "email");
        String movil   = serv.recuperarPropiedad(e, "telefono");
        String pwd     = serv.recuperarPropiedad(e, "contrasenia");
        String fechaS  = serv.recuperarPropiedad(e, "fechaNacimiento");
        String img     = serv.recuperarPropiedad(e, "imagenPerfil");
        boolean prem   = Boolean.parseBoolean(serv.recuperarPropiedad(e, "isPremium"));

        Usuario u = new Usuario.Builder(nombre, email, movil, pwd)
                        .fechaNacimiento(LocalDate.parse(fechaS))
                        .imagenPerfil(img)
                        .premium(prem)
                        .build();
        u.setId(id);
        PoolUsuarios.INSTANCE.add(u);

        // cargar contactos
        List<String> contactosIds = serv.recuperarPropiedad(e, "contactos")
                                        .isEmpty() ? List.of()
                                                   : List.of(serv.recuperarPropiedad(e, "contactos").split(","));
        for (String cid : contactosIds) {
            Contacto c = RepositorioContactos.INSTANCE.getById(cid);
            u.addContacto(c);
        }

        return u;
    }
}
