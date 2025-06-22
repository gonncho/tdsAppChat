package umu.tds.apps.vista.ventanas;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.utils.Placeholder; 

public class VentanaLogin {

    private JFrame frmAppchat;
    private JTextField textField;
    private JPasswordField passwordField;
    private static AppChat controlador;
    private Placeholder placeholder = new Placeholder(); 

    public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				VentanaLogin window = new VentanaLogin();
				window.frmAppchat.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaLogin() {
		controlador = AppChat.getUnicaInstancia();
		if (controlador == null) {
			System.err.println("Error: No se pudo inicializar el controlador AppChat.");
			JOptionPane.showMessageDialog(null, "Error crítico al iniciar la aplicación. Por favor, contacte al soporte.");
			System.exit(1);
		}
		initialize();
	}

	public void setVisible(boolean visible) {
		frmAppchat.setVisible(visible);
	}

	private void initialize() {
		frmAppchat = new JFrame();
		frmAppchat.setFocusable(true);
		frmAppchat.setTitle("AppChat");
		frmAppchat.setBounds(100, 100, 565, 376);
		frmAppchat.setLocationRelativeTo(null);
		frmAppchat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel fondoRojo = new JPanel(new BorderLayout());
		fondoRojo.setBackground(new Color(255, 102, 102));
		frmAppchat.setContentPane(fondoRojo);

		// Título en letras en vez de imagen
		JLabel titulo = new JLabel("AppChat", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 50));
		titulo.setForeground(Color.WHITE);
		titulo.setPreferredSize(new Dimension(100, 100));
		fondoRojo.add(titulo, BorderLayout.NORTH);

		JPanel panelCentro = new JPanel();
		panelCentro.setOpaque(false);
		GridBagLayout gbl_panelCentro = new GridBagLayout();
		gbl_panelCentro.columnWidths = new int[]{20, 0, 300, 20, 0}; 
		gbl_panelCentro.rowHeights = new int[]{20, 0, 0, 20, 0};
		gbl_panelCentro.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelCentro.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelCentro.setLayout(gbl_panelCentro);


		textField = new JTextField();
		textField.setText("Teléfono"); 
		textField.setFont(new Font("Arial", Font.ITALIC, 14)); 
		placeholder.crearPlaceholderText(textField, "Teléfono"); 
		textField.setBackground(SystemColor.control);
		textField.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField.setColumns(30); 
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;

		textField.setMinimumSize(new Dimension(300, textField.getPreferredSize().height));
		textField.setPreferredSize(new Dimension(300, textField.getPreferredSize().height));
		panelCentro.add(textField, gbc_textField);

		passwordField = new JPasswordField();
		passwordField.setText("Contraseña"); // Set initial text
		passwordField.setFont(new Font("Arial", Font.ITALIC, 14)); // Set font style
		passwordField.setEchoChar((char) 0); // Initially show the text
		placeholder.crearPlaceholderPassword(passwordField, "Contraseña"); // Apply placeholder
		passwordField.setBackground(SystemColor.control);
		passwordField.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		passwordField.setColumns(30); // Increase columns
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 2;
		passwordField.setMinimumSize(new Dimension(300, passwordField.getPreferredSize().height));
		passwordField.setPreferredSize(new Dimension(300, passwordField.getPreferredSize().height));
		panelCentro.add(passwordField, gbc_passwordField);

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		fondoRojo.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridBagLayout());
		panel.add(panelCentro);

		JPanel panelSur = new JPanel();
		panelSur.setOpaque(false);
		fondoRojo.add(panelSur, BorderLayout.SOUTH);

		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRegistrar.setBackground(new Color(220, 80, 80));
		btnRegistrar.setForeground(Color.WHITE);
		btnRegistrar.setFont(new Font("Arial", Font.BOLD, 13));
		btnRegistrar.setBorder(new LineBorder(new Color(180, 60, 60), 2, true));
		btnRegistrar.setPreferredSize(new Dimension(120, 30));
		panelSur.add(btnRegistrar);
		btnRegistrar.addActionListener(e -> {
			VentanaRegistro ventanaRegistro = new VentanaRegistro();
			ventanaRegistro.setVisible(true);
			frmAppchat.setVisible(false);
		});

		JButton btnLogin = new JButton("Login");
		btnLogin.setFocusPainted(false);
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.setBackground(new Color(0, 128, 128));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
		btnLogin.setBorder(new LineBorder(new Color(0, 100, 100), 2, true));
		btnLogin.setPreferredSize(new Dimension(120, 30));
		panelSur.add(btnLogin);
		btnLogin.addActionListener(e -> {
			String usuario = textField.getText();
			char[] contrasenia = passwordField.getPassword();
			if (controlador.iniciarSesion(usuario, contrasenia)) {
				VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
				ventanaPrincipal.setVisible(true);
				frmAppchat.setVisible(false);
			} else {
				// Mantener el cambio de color de fondo para indicación visual
				textField.setBackground(new Color(255, 200, 200));
				passwordField.setBackground(new Color(255, 200, 200));
				
				// Añadir la alerta personalizada
				mostrarMensaje("Teléfono o contraseña incorrectos. Por favor, inténtelo de nuevo.", 
							  "Error de acceso", 
							  new Color(153, 0, 0),      // Color rojo oscuro para el texto
							  new Color(255, 230, 230)); // Color rosa claro para el fondo
			}
		});

		frmAppchat.getRootPane().setDefaultButton(btnLogin);
	}

	// Primero, añadir este método a la clase VentanaLogin
	private void mostrarMensaje(String texto, String titulo, Color colorTexto, Color fondo) {
	    JPanel panel = new JPanel();
	    panel.setBackground(fondo);
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	    JLabel mensaje = new JLabel(texto);
	    mensaje.setFont(new Font("Arial", Font.BOLD, 14));
	    mensaje.setForeground(colorTexto);
	    mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

	    panel.add(Box.createVerticalStrut(10));
	    panel.add(mensaje);

	    JOptionPane.showMessageDialog(frmAppchat, panel, titulo, JOptionPane.PLAIN_MESSAGE);
	}
}