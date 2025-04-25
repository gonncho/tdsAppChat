package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaRegistro{

	private JFrame frame;
	private JTextField textNombre;
	private JTextField textApellidos;
	private JTextField textTelefono;
	private JTextField textContraseña;
	private JTextField textFecha;
	private JTextField textField;
    private JLabel lblImagenPreview;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaRegistro window = new VentanaRegistro();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VentanaRegistro() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(150, 150, 500, 350);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{20, 0, 0, 63, 0, 0, 20, 0};
		gridBagLayout.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 27, 44, 0, -5, 0, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel labelNombre = new JLabel("Nombre:");
		GridBagConstraints gbc_labelNombre = new GridBagConstraints();
		gbc_labelNombre.anchor = GridBagConstraints.EAST;
		gbc_labelNombre.insets = new Insets(0, 0, 5, 5);
		gbc_labelNombre.gridx = 1;
		gbc_labelNombre.gridy = 1;
		frame.getContentPane().add(labelNombre, gbc_labelNombre);
		
		textNombre = new JTextField();
		GridBagConstraints gbc_textNombre = new GridBagConstraints();
		gbc_textNombre.gridwidth = 4;
		gbc_textNombre.fill = GridBagConstraints.BOTH;
		gbc_textNombre.insets = new Insets(0, 0, 5, 5);
		gbc_textNombre.gridx = 2;
		gbc_textNombre.gridy = 1;
		frame.getContentPane().add(textNombre, gbc_textNombre);
		textNombre.setColumns(10);
		
		JLabel labelApellidos = new JLabel("Apellidos:");
		GridBagConstraints gbc_labelApellidos = new GridBagConstraints();
		gbc_labelApellidos.anchor = GridBagConstraints.EAST;
		gbc_labelApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_labelApellidos.gridx = 1;
		gbc_labelApellidos.gridy = 2;
		frame.getContentPane().add(labelApellidos, gbc_labelApellidos);
		
		textApellidos = new JTextField();
		GridBagConstraints gbc_textApellidos = new GridBagConstraints();
		gbc_textApellidos.fill = GridBagConstraints.HORIZONTAL;
		gbc_textApellidos.gridwidth = 4;
		gbc_textApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_textApellidos.gridx = 2;
		gbc_textApellidos.gridy = 2;
		frame.getContentPane().add(textApellidos, gbc_textApellidos);
		textApellidos.setColumns(10);
		
		JLabel labelTelefono = new JLabel("Telefono:");
		GridBagConstraints gbc_labelTelefono = new GridBagConstraints();
		gbc_labelTelefono.anchor = GridBagConstraints.EAST;
		gbc_labelTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_labelTelefono.gridx = 1;
		gbc_labelTelefono.gridy = 3;
		frame.getContentPane().add(labelTelefono, gbc_labelTelefono);
		
		textTelefono = new JTextField();
		GridBagConstraints gbc_textTelefono = new GridBagConstraints();
		gbc_textTelefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_textTelefono.insets = new Insets(0, 0, 5, 5);
		gbc_textTelefono.gridx = 2;
		gbc_textTelefono.gridy = 3;
		frame.getContentPane().add(textTelefono, gbc_textTelefono);
		textTelefono.setColumns(10);
		
		JLabel labelContraseña = new JLabel("Contraseña:");
		GridBagConstraints gbc_labelContraseña = new GridBagConstraints();
		gbc_labelContraseña.anchor = GridBagConstraints.EAST;
		gbc_labelContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_labelContraseña.gridx = 1;
		gbc_labelContraseña.gridy = 4;
		frame.getContentPane().add(labelContraseña, gbc_labelContraseña);
		
		textContraseña = new JTextField();
		GridBagConstraints gbc_textContraseña = new GridBagConstraints();
		gbc_textContraseña.fill = GridBagConstraints.HORIZONTAL;
		gbc_textContraseña.insets = new Insets(0, 0, 5, 5);
		gbc_textContraseña.gridx = 2;
		gbc_textContraseña.gridy = 4;
		frame.getContentPane().add(textContraseña, gbc_textContraseña);
		textContraseña.setColumns(10);
		
		JLabel labelRcontraseña = new JLabel("Repetir Contraseña:");
		GridBagConstraints gbc_labelRcontraseña = new GridBagConstraints();
		gbc_labelRcontraseña.gridwidth = 2;
		gbc_labelRcontraseña.anchor = GridBagConstraints.EAST;
		gbc_labelRcontraseña.insets = new Insets(0, 0, 5, 5);
		gbc_labelRcontraseña.gridx = 3;
		gbc_labelRcontraseña.gridy = 4;
		frame.getContentPane().add(labelRcontraseña, gbc_labelRcontraseña);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 5;
		gbc_textField.gridy = 4;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel labelFecha = new JLabel("fecha:");
		GridBagConstraints gbc_labelFecha = new GridBagConstraints();
		gbc_labelFecha.anchor = GridBagConstraints.EAST;
		gbc_labelFecha.insets = new Insets(0, 0, 5, 5);
		gbc_labelFecha.gridx = 1;
		gbc_labelFecha.gridy = 5;
		frame.getContentPane().add(labelFecha, gbc_labelFecha);
		
		textFecha = new JTextField();
		GridBagConstraints gbc_textFecha = new GridBagConstraints();
		gbc_textFecha.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFecha.insets = new Insets(0, 0, 5, 5);
		gbc_textFecha.gridx = 2;
		gbc_textFecha.gridy = 5;
		frame.getContentPane().add(textFecha, gbc_textFecha);
		textFecha.setColumns(10);
		
		JLabel labelSaludo = new JLabel("saludo:");
		GridBagConstraints gbc_labelSaludo = new GridBagConstraints();
		gbc_labelSaludo.anchor = GridBagConstraints.EAST;
		gbc_labelSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_labelSaludo.gridx = 1;
		gbc_labelSaludo.gridy = 6;
		frame.getContentPane().add(labelSaludo, gbc_labelSaludo);
		
		JLabel labelImagen = new JLabel("Imagen:");
		GridBagConstraints gbc_labelImagen = new GridBagConstraints();
		gbc_labelImagen.anchor = GridBagConstraints.WEST;
		gbc_labelImagen.insets = new Insets(0, 0, 5, 5);
		gbc_labelImagen.gridx = 3;
		gbc_labelImagen.gridy = 6;
		frame.getContentPane().add(labelImagen, gbc_labelImagen);
		
		JButton buttonAceptar = new JButton("Aceptar");
		buttonAceptar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				VentanaLogin vl = new VentanaLogin();
				vl.mostrar();
				frame.dispose();
				
				//PENDIENTE DE IMPLEMENTAR GUARDAR DATOS INTRODUCIDOS EN BD (Persistencia)
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 6;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		JTextArea textAreaSaludo = new JTextArea();
		textAreaSaludo.setLineWrap(true);
		textAreaSaludo.setWrapStyleWord(true);
		scrollPane.setViewportView(textAreaSaludo);
		
		lblImagenPreview = new JLabel();
		lblImagenPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// si quieres forzar tamaño:
		lblImagenPreview.setPreferredSize(new Dimension(150,150));
		
		GridBagConstraints gbc_labelFotoImagen = new GridBagConstraints();
		gbc_labelFotoImagen.gridheight = 3;
		gbc_labelFotoImagen.insets = new Insets(0, 0, 5, 5);
		gbc_labelFotoImagen.gridx = 5;
		gbc_labelFotoImagen.gridy = 6;
		gbc_labelFotoImagen.fill    = GridBagConstraints.BOTH;   
		gbc_labelFotoImagen.weightx = 0.5;                      
		gbc_labelFotoImagen.weighty = 1.0;                       
		
		frame.getContentPane().add(lblImagenPreview, gbc_labelFotoImagen);
		GridBagConstraints gbc_buttonAceptar = new GridBagConstraints();
		gbc_buttonAceptar.insets = new Insets(0, 0, 5, 5);
		gbc_buttonAceptar.gridx = 2;
		gbc_buttonAceptar.gridy = 10;
		frame.getContentPane().add(buttonAceptar, gbc_buttonAceptar);
		
		JButton buttonCancelar = new JButton("Cancelar");
		buttonCancelar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				VentanaLogin vl = new VentanaLogin();
				vl.mostrar();
				frame.dispose();
			}
		});
		GridBagConstraints gbc_buttonCancelar = new GridBagConstraints();
		gbc_buttonCancelar.gridwidth = 2;
		gbc_buttonCancelar.insets = new Insets(0, 0, 5, 5);
		gbc_buttonCancelar.gridx = 3;
		gbc_buttonCancelar.gridy = 10;
		frame.getContentPane().add(buttonCancelar, gbc_buttonCancelar);
		
		JButton botonImagen = new JButton("Subir Imagen");
		botonImagen.addActionListener(e -> {
		    JFileChooser chooser = new JFileChooser();
		    chooser.setFileFilter(new FileNameExtensionFilter(
		        "Imágenes JPG, PNG, GIF", "jpg","jpeg","png","gif"));
		    int opcion = chooser.showOpenDialog(frame);
		    if (opcion == JFileChooser.APPROVE_OPTION) {
		        File fichero = chooser.getSelectedFile();
		        try {
		            BufferedImage img = ImageIO.read(fichero);
		            // escalamos para que quepa en el JLabel           
		            Dimension d = lblImagenPreview.getPreferredSize();
		            Image imgEscalada = img.getScaledInstance(
		            		d.width,
		            		d.height,
		            		Image.SCALE_SMOOTH
		            );
		            
		            lblImagenPreview.setIcon(new ImageIcon(imgEscalada));
		        } catch (IOException ex) {
		            JOptionPane.showMessageDialog(frame,
		                "No se pudo cargar la imagen",
		                "Error",
		                JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		GridBagConstraints gbc_botonImagen = new GridBagConstraints();
		gbc_botonImagen.anchor = GridBagConstraints.EAST;
		gbc_botonImagen.insets = new Insets(0, 0, 5, 5);
		gbc_botonImagen.gridx = 5;
		gbc_botonImagen.gridy = 10;
		frame.getContentPane().add(botonImagen, gbc_botonImagen);
	}
	
	void mostrar() {
		frame.setVisible(true);
	}

	

}
