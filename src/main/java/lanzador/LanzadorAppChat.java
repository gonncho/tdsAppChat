package lanzador;

import controlador.AppChat;
import modelo.Contacto;
import modelo.Mensaje;
import modelo.Usuario;
import vista.*;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

public class LanzadorAppChat {

    private final AppChat app = AppChat.INSTANCE;
    private final VentanaLogin login = new VentanaLogin();
    private final VentanaRegistro registro = new VentanaRegistro();
    private final VentanaPrincipal principal = new VentanaPrincipal();
    private final VentanaNuevoContacto ventanaContacto = new VentanaNuevoContacto();
    private final VentanaNuevoGrupo    ventanaGrupo    = new VentanaNuevoGrupo();
    private final VentanaBusqueda     ventanaBusqueda = new VentanaBusqueda();
    private String rutaImagenRegistro = null;

    public LanzadorAppChat() {
        configurarLogin();
        configurarRegistro();
        configurarPrincipal();
    }

    private void configurarLogin() {
        login.addLoginListener(e -> {
            if (app.login(login.getTelefono(), login.getPassword())) {
                login.ocultar();
                actualizarPrincipal();
                principal.mostrar();
            } else {
                JOptionPane.showMessageDialog(null, "Credenciales incorrectas");
            }
        });
        login.addRegisterListener(e -> {
            login.ocultar();
            registro.mostrar();
        });
        login.addCancelListener(e -> System.exit(0));
    }

    private void configurarRegistro() {
        registro.addUploadListener(e -> seleccionarImagen());
        registro.addAcceptListener(e -> realizarRegistro());
        registro.addCancelListener(e -> {
            registro.ocultar();
            login.mostrar();
        });
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            rutaImagenRegistro = f.getAbsolutePath();
            registro.getImagenPreviewLabel().setIcon(new ImageIcon(rutaImagenRegistro));
        }
    }

    private void realizarRegistro() {
        if (!registro.getPassword().equals(registro.getRepetirPassword())) {
            JOptionPane.showMessageDialog(null, "Las contrase\u00f1as no coinciden");
            return;
        }
        LocalDate fecha = null;
        try {
            if (!registro.getFecha().isEmpty()) {
                fecha = LocalDate.parse(registro.getFecha());
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(null, "Fecha inv\u00e1lida (yyyy-MM-dd)");
            return;
        }
        boolean ok = app.registrarUsuario(
                registro.getNombre(),
                registro.getApellidos(),
                registro.getTelefono(),
                registro.getPassword(),
                fecha,
                rutaImagenRegistro,
                registro.getSaludo()
        );
        if (ok) {
            JOptionPane.showMessageDialog(null, "Usuario registrado correctamente");
            registro.ocultar();
            login.mostrar();
        } else {
            JOptionPane.showMessageDialog(null, "Tel\u00e9fono ya existente");
        }
    }

    private void configurarPrincipal() {
        principal.addSendListener(e -> enviarMensaje());
        principal.addContactSelectionListener(e -> mostrarConversacion());
        principal.addContactosListener(e -> mostrarVentanaNuevoContacto());
        principal.addBuscarListener(e -> mostrarVentanaBusqueda());
    }

    private void actualizarPrincipal() {
        Usuario u = app.getUsuarioActual();
        principal.setUsuarioInfo(u.getNombre(), null);
        principal.setContactos(
                u.getContactos().stream()
                        .map(Contacto::toString)
                        .collect(Collectors.toList())
        );
    }

    private Contacto buscarContacto(String representacion) {
        Usuario u = app.getUsuarioActual();
        if (u == null) return null;
        return u.getContactos().stream()
                .filter(c -> c.toString().equals(representacion))
                .findFirst()
                .orElse(null);
    }

    private void mostrarConversacion() {
        Contacto c = buscarContacto(principal.getContactoSeleccionado());
        if (c == null) return;
        principal.limpiarConversacion();
        for (Mensaje m : c.getMensajes()) {
            if (m.getTipo() == Mensaje.Tipo.ENVIADO) {
                principal.addSentBubble(m.getContenido());
            } else {
                principal.addReceivedBubble(c.toString(), m.getContenido());
            }
        }
    }

    private void enviarMensaje() {
        String texto = principal.getTextoMensaje();
        if (texto.isEmpty()) return;
        Contacto c = buscarContacto(principal.getContactoSeleccionado());
        if (c == null) return;
        app.enviarMensajeContacto(c, texto, -1, Mensaje.Tipo.ENVIADO);
        principal.addSentBubble(texto);
    }
    
    private void mostrarVentanaNuevoContacto() {
        ventanaContacto.mostrar();
        ventanaContacto.addAcceptListener(e -> {
            Contacto nuevo = app.agregarContacto(
                    ventanaContacto.getNombre(),
                    ventanaContacto.getTelefono());
            if (nuevo != null) {
                actualizarPrincipal();
                ventanaContacto.ocultar();
            } else {
                JOptionPane.showMessageDialog(null, "Teléfono no encontrado");
            }
        });
        ventanaContacto.addCancelListener(e -> ventanaContacto.ocultar());
    }

    private void mostrarVentanaBusqueda() {
        ventanaBusqueda.addSearchListener(e -> realizarBusqueda());
        ventanaBusqueda.addCloseListener(e -> ventanaBusqueda.ocultar());
        ventanaBusqueda.mostrar();
    }

    private void realizarBusqueda() {
        java.util.List<modelo.ResultadoBusqueda> res = app.buscarMensajes(
                ventanaBusqueda.getTexto().isEmpty() ? null : ventanaBusqueda.getTexto(),
                ventanaBusqueda.getNombre().isEmpty() ? null : ventanaBusqueda.getNombre(),
                ventanaBusqueda.getTelefono().isEmpty() ? null : ventanaBusqueda.getTelefono());
        java.util.List<String> textos = res.stream()
                .map(r -> String.format("%s [%s]: %s",
                        r.contacto().toString(),
                        r.mensaje().getFechaEnvio(),
                        r.mensaje().getContenido()))
                .toList();
        ventanaBusqueda.setResultados(textos);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LanzadorAppChat l = new LanzadorAppChat();
            l.login.mostrar();
        });
    }
}
