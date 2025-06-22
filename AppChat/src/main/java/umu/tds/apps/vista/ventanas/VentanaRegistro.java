package umu.tds.apps.vista.ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Component;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JScrollPane;
import com.toedter.calendar.JDateChooser;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.utils.Placeholder;
import umu.tds.apps.utils.IconUtil;

import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Toolkit;
import java.awt.Cursor;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import umu.tds.apps.modelo.Usuario;


public class VentanaRegistro extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField email;
	private JTextField txtNombre;
	private JTextField textTelefono;
	private JPasswordField textContrasenia;
	private JPasswordField textContraseniaNueva;
	private JTextArea textArea;
	private JButton btnRegistrar;
	private JButton btnCancelar;
	private JLabel lblNewLabel_8;
	private JScrollPane scrollPane;
	private JDateChooser dateChooser;
	private Placeholder placeholder = new Placeholder();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaRegistro frame = new VentanaRegistro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaRegistro() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaRegistro.class.getResource("/umu/tds/apps/resources/icono.png")));
		setTitle("AppChat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 561, 421);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 102, 102));
		contentPane.setBorder(null);
		contentPane.setFocusable(true); 

		setContentPane(contentPane);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 20, 0, 0, 0, 0, 20, 0 };
		gbl_contentPane.rowHeights = new int[] { 20, 0, 0, 0, 0, 0, 20, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		txtNombre = new JTextField();
		txtNombre.setText("*Nombre");
		txtNombre.setFont(new Font("Arial", Font.ITALIC, 14));
		placeholder.crearPlaceholderText(txtNombre, "*Nombre");
		txtNombre.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_txtNombre = new GridBagConstraints();
		gbc_txtNombre.gridwidth = 3;
		gbc_txtNombre.insets = new Insets(0, 0, 5, 5);
		gbc_txtNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNombre.gridx = 2;
		gbc_txtNombre.gridy = 1;
		contentPane.add(txtNombre, gbc_txtNombre);
		txtNombre.setColumns(10);

		email = new JTextField();
		email.setText("Email");
		email.setFont(new Font("Arial", Font.ITALIC, 14));
		placeholder.crearPlaceholderText(email, "Email");
		email.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 3;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 2;
		contentPane.add(email, gbc_textField_1);
		email.setColumns(10);

		textTelefono = new JTextField();
		textTelefono.setText("*Telefono");
		textTelefono.setFont(new Font("Arial", Font.ITALIC, 14));
		placeholder.crearPlaceholderText(textTelefono, "*Telefono");
		textTelefono.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 2;
		gbc_textField_2.gridy = 3;
		contentPane.add(textTelefono, gbc_textField_2);
		textTelefono.setColumns(10);
		
		dateChooser = new JDateChooser();
		JTextField editorFecha = ((JTextField) dateChooser.getDateEditor().getUiComponent());
		editorFecha.setText("Fecha de nacimiento");
		editorFecha.setFont(new Font("Arial", Font.ITALIC, 14));
		Placeholder placeholder = new Placeholder();
		placeholder.crearPlaceholderText(editorFecha, "Fecha de nacimiento");
		dateChooser.getCalendarButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		dateChooser.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_dateChooser = new GridBagConstraints();
		gbc_dateChooser.gridwidth = 2;
		gbc_dateChooser.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser.fill = GridBagConstraints.BOTH;
		gbc_dateChooser.gridx = 3;
		gbc_dateChooser.gridy = 3;
		contentPane.add(dateChooser, gbc_dateChooser);

		textContrasenia = new JPasswordField();
		textContrasenia.setText("*Contraseña");
		textContrasenia.setFont(new Font("Arial", Font.ITALIC, 14));
		textContrasenia.setEchoChar((char) 0); 
		placeholder.crearPlaceholderPassword(textContrasenia, "*Contraseña");
		textContrasenia.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textContrasenia.setColumns(10);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 2; 
		gbc_passwordField.gridy = 4;
		contentPane.add(textContrasenia, gbc_passwordField);
		
		textContraseniaNueva = new JPasswordField();
		textContraseniaNueva.setText("*Repetir contraseña");
		textContraseniaNueva.setFont(new Font("Arial", Font.ITALIC, 14));
		textContraseniaNueva.setEchoChar((char) 0); 
		placeholder.crearPlaceholderPassword(textContraseniaNueva, "*Repetir contraseña");
		textContraseniaNueva.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textContraseniaNueva.setColumns(10);
		GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
		gbc_passwordField_1.gridwidth = 2;
		gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField_1.gridx = 3;
		gbc_passwordField_1.gridy = 4;
		contentPane.add(textContraseniaNueva, gbc_passwordField_1);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 3; // intercambiado: antes era 2
		gbc_scrollPane.gridy = 5;
		contentPane.add(scrollPane, gbc_scrollPane);

		textArea = new JTextArea();
		textArea.setText("Introduce un breve saludo");
		textArea.setFont(new Font("Arial", Font.ITALIC, 14));
		placeholder.crearPlaceholderTextArea(textArea, "Introduce un breve saludo");
		textArea.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setViewportView(textArea);
		textArea.setColumns(4);
		
		ImageIcon defaultIcon = new ImageIcon(VentanaRegistro.class.getResource("/umu/tds/apps/resources/usuario.png"));
		defaultIcon.setDescription("/umu/tds/apps/resources/usuario.png");

		lblNewLabel_8 = new JLabel("");
		lblNewLabel_8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblNewLabel_8.setIcon(defaultIcon);
		GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
		gbc_lblNewLabel_8.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_8.gridx = 2; // intercambiado: antes era 3
		gbc_lblNewLabel_8.gridy = 5;
		contentPane.add(lblNewLabel_8, gbc_lblNewLabel_8);

		lblNewLabel_8.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        seleccionarFotoPerfil();
		    }
		});
		
				btnCancelar = new JButton("Cancelar"); 
				GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
				gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
				gbc_btnNewButton_1.gridx = 3;
				gbc_btnNewButton_1.gridy = 7;
				contentPane.add(btnCancelar, gbc_btnNewButton_1);
				btnCancelar.setFocusPainted(false);
				btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				btnCancelar.setBackground(new Color(245, 245, 245));
				btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							VentanaLogin login = new VentanaLogin();
							login.setVisible(true);
							dispose();
						} catch (Exception ex) {
							mostrarMensaje("Error al abrir la ventana de inicio de sesión.", "Error",
									new Color(153, 0, 0), new Color(255, 230, 230));
						}
					}
				});
				btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
				
				btnRegistrar = new JButton("Aceptar");
				GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
				gbc_btnNewButton.anchor = GridBagConstraints.EAST;
				gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
				gbc_btnNewButton.gridx = 4;
				gbc_btnNewButton.gridy = 7;
				contentPane.add(btnRegistrar, gbc_btnNewButton);
				btnRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				btnRegistrar.setFocusPainted(false);
				btnRegistrar.setBackground(new Color(0, 128, 128));
				btnRegistrar.setFont(new Font("Arial", Font.BOLD, 13));
		
				
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = txtNombre.getText();
				String telefono = textTelefono.getText();
				String contraseña = String.valueOf(textContrasenia.getPassword());
				String contraseñaRepetida = String.valueOf(textContraseniaNueva.getPassword());
				String emailText = email.getText();

				// Validación inicial de campos obligatorios
				if (nombre.isEmpty() || nombre.equals("*Nombre") ||
					telefono.isEmpty() || telefono.equals("*Telefono") ||
					contraseña.isEmpty() || contraseña.equals("*Contraseña") ||
					contraseñaRepetida.isEmpty() || contraseñaRepetida.equals("*Repetir contraseña")) {
					
					mostrarMensaje("Por favor, complete todos los campos obligatorios.", "Campos Vacíos",
							new Color(153, 0, 0), new Color(255, 230, 230)); 
					return;
				}

				if (!contraseña.equals(contraseñaRepetida)) {
					mostrarMensaje("Las contraseñas no coinciden. Por favor, inténtelo de nuevo.", "Error de Contraseña",
							new Color(153, 0, 0), new Color(255, 230, 230));
					return;
				}

				// [NUEVO] Verificar si el teléfono ya existe ANTES de intentar registrar
				AppChat controlador = AppChat.getUnicaInstancia();
				Usuario usuarioExistente = controlador.obtenerUsuarioPorTelefono(telefono);
				if (usuarioExistente != null) {
					mostrarMensaje("El número de teléfono ya está registrado. Por favor, use otro número.", 
							"Teléfono duplicado", new Color(153, 0, 0), new Color(255, 230, 230));
					// Destacar visualmente el campo de teléfono
					textTelefono.setBackground(new Color(255, 200, 200));
					return;
				}

				// El resto del código del registro permanece igual...
				boolean resultado = false;
				try {
					String rutaImagen = ((ImageIcon) lblNewLabel_8.getIcon()).getDescription();
					
					// Comprobación de fecha para evitar enviar el placeholder
					LocalDate fechaNacimiento = null;
					if (dateChooser.getDate() != null) {
						// Comprobamos si el texto es el placeholder
						JTextField editorFecha = ((JTextField) dateChooser.getDateEditor().getUiComponent());
						if (!editorFecha.getText().equals("Fecha de nacimiento")) {
							// Solo convertimos la fecha si no es el placeholder
							fechaNacimiento = dateChooser.getDate().toInstant()
									   .atZone(java.time.ZoneId.systemDefault())
									   .toLocalDate();
						}
					}
					
					// Llamada al controlador con la fecha verificada
					resultado = controlador.registrarUsuario(
						nombre, contraseña.toCharArray(), telefono, emailText,
						Optional.ofNullable(textArea.getText()), new ImageIcon(rutaImagen),
						fechaNacimiento,  // Ahora puede ser null si era el placeholder
						LocalDate.now()
					);
				} catch (Exception ex) {
					mostrarMensaje("Error al registrar el usuario. Por favor, inténtelo de nuevo.", 
							"Error de Registro", new Color(153, 0, 0), new Color(255, 230, 230)); 
					return;
				}

				if (resultado) { 
					mostrarMensaje("¡Usuario registrado correctamente!", "Registro Exitoso", 
							new Color(0, 128, 0), new Color(255, 245, 245));
					// Volver a la pantalla de login
					VentanaLogin login = new VentanaLogin();
					login.setVisible(true);
					dispose();
				} else {
					// Este error ahora sería por otras razones, no por teléfono duplicado
					mostrarMensaje("Error al registrar el usuario. Verifique que los datos sean correctos.", 
							"Error de Registro", new Color(153, 0, 0), new Color(255, 230, 230));
				}
			}
		});
		
		

	}
	
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

	    JOptionPane.showMessageDialog(contentPane, panel, titulo, JOptionPane.PLAIN_MESSAGE);
	}
	
	private void seleccionarFotoPerfil() {
	    String[] opciones = { "Introducir enlace", "Seleccionar archivo" };
	    int seleccion = JOptionPane.showOptionDialog(contentPane, 
	            "Seleccione cómo desea cargar la imagen:",
	            "Cargar Imagen", 
	            JOptionPane.DEFAULT_OPTION, 
	            JOptionPane.INFORMATION_MESSAGE, 
	            null, 
	            opciones, 
	            opciones[0]);

	    try {
	        BufferedImage imagen = null;
	        String descripcion = "";

	        if (seleccion == 0) {
	            // Desde URL
	            String urlImagen = JOptionPane.showInputDialog(contentPane, 
	                    "Introduzca el enlace de la imagen:",
	                    "Cargar Imagen desde URL", 
	                    JOptionPane.PLAIN_MESSAGE);

	            if (urlImagen != null && !urlImagen.isEmpty()) {
	                imagen = ImageIO.read(new URL(urlImagen));
	                descripcion = urlImagen;
	            }
	        } else if (seleccion == 1) {
	            // Desde archivo local
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));
	            int resultado = fileChooser.showOpenDialog(contentPane);
	            if (resultado == JFileChooser.APPROVE_OPTION) {
	                imagen = ImageIO.read(fileChooser.getSelectedFile());
	                descripcion = fileChooser.getSelectedFile().getAbsolutePath();
	            }
	        }

	        if (imagen != null) {
	            ImageIcon icono = new ImageIcon(imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
	            icono = IconUtil.createCircularIcon(imagen, 100);
	            if (descripcion.startsWith("file:")) descripcion = descripcion.substring(5);
	            icono.setDescription(descripcion);
	            lblNewLabel_8.setIcon(icono);
	        }

	    } catch (IOException ex) {
	        mostrarMensaje("No se pudo cargar la imagen.", "Error de carga", new Color(153, 0, 0), new Color(255, 230, 230));
	    }
	}

	
	

}
