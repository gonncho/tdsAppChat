package test;

import vista.VentanaLogin;
import vista.VentanaRegistro;
import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestVistas {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1) Probar VentanaLogin
            VentanaLogin login = new VentanaLogin();
            login.addLoginListener(e -> 
                JOptionPane.showMessageDialog(null, 
                    "Login Aceptar pulsado: " + login.getTelefono() + " / " + login.getPassword()
                )
            );
            login.addRegisterListener(e -> 
                JOptionPane.showMessageDialog(null, "Registrar pulsado")
            );
            login.addCancelListener(e -> 
                JOptionPane.showMessageDialog(null, "Cancelar desde Login")
            );
            login.mostrar();

            // 2) Probar VentanaRegistro
            VentanaRegistro registro = new VentanaRegistro();
            registro.addAcceptListener(e -> 
                JOptionPane.showMessageDialog(null, 
                    "Registro Aceptar: " +
                    registro.getNombre()+" "+registro.getApellidos()
                )
            );
            registro.addCancelListener(e -> 
                JOptionPane.showMessageDialog(null, "Cancelar desde Registro")
            );
            registro.addUploadListener(e -> 
                JOptionPane.showMessageDialog(null, "Subir Imagen pulsado")
            );
            registro.mostrar();

            // 3) Probar VentanaPrincipal
            VentanaPrincipal principal = new VentanaPrincipal();
            principal.addContactosListener(e -> 
                JOptionPane.showMessageDialog(null, "Contactos pulsado")
            );
            principal.addPremiumListener(e -> 
                JOptionPane.showMessageDialog(null, "Premium pulsado")
            );
            principal.addBuscarListener(e -> 
                JOptionPane.showMessageDialog(null, "Buscar pulsado")
            );
            principal.addSendListener(e -> {
                String msg = JOptionPane.showInputDialog("Escribe un mensaje de prueba:");
                if (msg != null && !msg.isEmpty()) {
                    principal.addSentBubble(msg);
                }
            });
            principal.setEmojiSentHandler((contacto, idx) -> 
                JOptionPane.showMessageDialog(null, 
                    "Emoji enviado al contacto " + contacto + ": id=" + idx
                )
            );
            principal.mostrar();
        });
    }
}
