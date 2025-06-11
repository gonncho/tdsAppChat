package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Pequeña ventana para añadir un contacto individual.
 */
public class VentanaNuevoContacto {
    private final JFrame frame;
    private final JTextField txtNombre;
    private final JTextField txtTelefono;
    private final JButton btnAceptar;
    private final JButton btnCancelar;

    public VentanaNuevoContacto() {
        frame = new JFrame("Nuevo contacto");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx=0; c.gridy=0; panel.add(new JLabel("Nombre:"), c);
        c.gridx=1; txtNombre = new JTextField(20); panel.add(txtNombre, c);
        c.gridx=0; c.gridy=1; panel.add(new JLabel("Teléfono:"), c);
        c.gridx=1; txtTelefono = new JTextField(15); panel.add(txtTelefono, c);

        frame.add(panel, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        botones.add(btnAceptar);
        botones.add(btnCancelar);
        frame.add(botones, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public String getNombre() { return txtNombre.getText().trim(); }
    public String getTelefono() { return txtTelefono.getText().trim(); }

    public void addAcceptListener(ActionListener l) { btnAceptar.addActionListener(l); }
    public void addCancelListener(ActionListener l) { btnCancelar.addActionListener(l); }

    public void mostrar() { frame.setVisible(true); }
    public void ocultar() { frame.setVisible(false); }
}