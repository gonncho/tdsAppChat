package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaRegistro {

    private final JFrame frame;
    private final JTextField txtNombre;
    private final JTextField txtApellidos;
    private final JTextField txtTelefono;
    private final JTextField txtPassword;
    private final JTextField txtRepetirPassword;
    private final JTextField txtFecha;
    private final JTextArea  txtSaludo;
    private final JLabel     lblImagenPreview;
    private final JButton    btnSubirImagen;
    private final JButton    btnAceptar;
    private final JButton    btnCancelar;

    public VentanaRegistro() {
        frame = new JFrame("Registro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel, BorderLayout.CENTER);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill   = GridBagConstraints.HORIZONTAL;

        // Nombre
        c.gridx=0; c.gridy=0; c.weightx=0; panel.add(new JLabel("Nombre:"), c);
        c.gridx=1; c.gridy=0; c.weightx=1; txtNombre = new JTextField(20); panel.add(txtNombre, c);

        // Apellidos
        c.gridx=0; c.gridy=1; c.weightx=0; panel.add(new JLabel("Apellidos:"), c);
        c.gridx=1; c.gridy=1; c.weightx=1; txtApellidos = new JTextField(20); panel.add(txtApellidos, c);

        // Teléfono
        c.gridx=0; c.gridy=2; c.weightx=0; panel.add(new JLabel("Teléfono:"), c);
        c.gridx=1; c.gridy=2; c.weightx=1; txtTelefono = new JTextField(15); panel.add(txtTelefono, c);

        // Contraseña y repetir
        c.gridx=0; c.gridy=3; c.weightx=0; panel.add(new JLabel("Contraseña:"), c);
        c.gridx=1; c.gridy=3; c.weightx=1; txtPassword = new JPasswordField(15); panel.add(txtPassword, c);
        c.gridx=2; c.gridy=3; panel.add(new JLabel("Repetir:"), c);
        c.gridx=3; c.gridy=3; txtRepetirPassword = new JPasswordField(15); panel.add(txtRepetirPassword, c);

        // Fecha nacimiento
        c.gridx=0; c.gridy=4; c.weightx=0; panel.add(new JLabel("Fecha Nac.:"), c);
        c.gridx=1; c.gridy=4; c.weightx=1; txtFecha = new JTextField(10); panel.add(txtFecha, c);

        // Saludo
        c.gridx=0; c.gridy=5; c.weightx=0; panel.add(new JLabel("Saludo:"), c);
        c.gridx=1; c.gridy=5; c.gridheight=2; c.weightx=1;
        txtSaludo = new JTextArea(4,20);
        panel.add(new JScrollPane(txtSaludo), c);
        c.gridheight=1;

        // Imagen
        c.gridx=2; c.gridy=5; panel.add(new JLabel("Imagen:"), c);
        c.gridx=3; c.gridy=5;
        lblImagenPreview = new JLabel();
        lblImagenPreview.setPreferredSize(new Dimension(150,150));
        panel.add(lblImagenPreview, c);

        // Botones
        JPanel botones = new JPanel();
        btnSubirImagen = new JButton("Subir Imagen");
        btnAceptar      = new JButton("Aceptar");
        btnCancelar     = new JButton("Cancelar");
        botones.add(btnSubirImagen);
        botones.add(btnAceptar);
        botones.add(btnCancelar);
        frame.add(botones, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    // — Getters —
    public String getNombre()            { return txtNombre.getText().trim(); }
    public String getApellidos()         { return txtApellidos.getText().trim(); }
    public String getTelefono()          { return txtTelefono.getText().trim(); }
    public String getPassword()          { return txtPassword.getText(); }
    public String getRepetirPassword()   { return txtRepetirPassword.getText(); }
    public String getFecha()             { return txtFecha.getText().trim(); }
    public String getSaludo()            { return txtSaludo.getText().trim(); }
    public JLabel getImagenPreviewLabel(){ return lblImagenPreview; }

    // — Inyección de listeners —
    public void addUploadListener(ActionListener l) { btnSubirImagen.addActionListener(l); }
    public void addAcceptListener(ActionListener l) { btnAceptar.addActionListener(l); }
    public void addCancelListener(ActionListener l) { btnCancelar.addActionListener(l); }

    // — Mostrar / ocultar —
    public void mostrar() { frame.setVisible(true); }
    public void ocultar()  { frame.setVisible(false); }
}
