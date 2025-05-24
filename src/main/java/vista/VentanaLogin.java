package vista;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaLogin {

    private final JFrame frame;
    private final JTextField txtTelefono;
    private final JPasswordField txtPassword;
    private final JButton btnAceptar;
    private final JButton btnRegistrar;
    private final JButton btnCancelar;

    public VentanaLogin() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        // Cabecera
        JLabel lblTitulo = new JLabel("AppChat", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.PLAIN, 30f));
        frame.add(lblTitulo, BorderLayout.NORTH);

        // Panel central
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder(
            new EtchedBorder(EtchedBorder.LOWERED, Color.white, Color.lightGray),
            "Login"
        ));
        frame.add(panel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill   = GridBagConstraints.HORIZONTAL;

        // Teléfono
        c.gridx = 0; c.gridy = 0; c.weightx = 0; panel.add(new JLabel("Teléfono:"), c);
        c.gridx = 1; c.gridy = 0; c.weightx = 1; txtTelefono = new JTextField(20); panel.add(txtTelefono, c);

        // Contraseña
        c.gridx = 0; c.gridy = 1; c.weightx = 0; panel.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; c.gridy = 1; c.weightx = 1; txtPassword = new JPasswordField(20); panel.add(txtPassword, c);

        // Botones
        JPanel botones = new JPanel();
        btnRegistrar = new JButton("Registrar");
        btnAceptar   = new JButton("Aceptar");
        btnCancelar  = new JButton("Cancelar");
        botones.add(btnRegistrar);
        botones.add(btnAceptar);
        botones.add(btnCancelar);
        frame.add(botones, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    // — Getters para modelo/controlador —
    public String getTelefono()   { return txtTelefono.getText().trim(); }
    public String getPassword()   { return new String(txtPassword.getPassword()); }

    // — Inyección de ActionListeners —
    public void addLoginListener(ActionListener l)    { btnAceptar.addActionListener(l); }
    public void addRegisterListener(ActionListener l) { btnRegistrar.addActionListener(l); }
    public void addCancelListener(ActionListener l)   { btnCancelar.addActionListener(l); }

    // — Mostrar / ocultar —
    public void mostrar() { frame.setVisible(true); }
    public void ocultar()  { frame.setVisible(false); }
}
