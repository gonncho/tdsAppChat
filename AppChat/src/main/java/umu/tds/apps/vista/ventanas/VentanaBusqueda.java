package umu.tds.apps.vista.ventanas;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.utils.Placeholder;
import umu.tds.apps.vista.renders.BusquedaCellRenderer;

import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.Toolkit;
import javax.swing.UIManager;

public class VentanaBusqueda extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtTexto;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPanel panelMensajes = new JPanel();
	private JList<Mensaje> listaResultados;
	private VentanaPrincipal ventanaPrincipal;
	private Placeholder placeholder = new Placeholder();

	public VentanaBusqueda(VentanaPrincipal ventanaPrincipal) {
		this.ventanaPrincipal = ventanaPrincipal;
		initialize();
	}

	public void initialize() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaBusqueda.class.getResource("/umu/tds/apps/resources/lupaVentanaBuscar.png")));
		setResizable(false);
		setTitle("Búsqueda");
		setBounds(100, 100, 495, 465);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(205, 235, 234));
		contentPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(150, 150, 150)));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(255, 225, 225));
		contentPanel.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BorderLayout(0, 0));
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(new Color(255, 225, 225));
		lblNewLabel.setIcon(
				new ImageIcon(VentanaBusqueda.class.getResource("/umu/tds/apps/resources/lupaVentanaBuscar.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panelNorte.add(lblNewLabel);

		JPanel panelCentro = new JPanel();
		panelCentro.setBackground(new Color(255, 225, 225));
		contentPanel.add(panelCentro, BorderLayout.CENTER);
		panelCentro.setLayout(new BorderLayout(0, 0));
		JPanel panelBuscar = new JPanel();
		panelBuscar.setBackground(new Color(255, 225, 225));
		panelBuscar.setFont(new Font("Arial", Font.PLAIN, 11));
		panelBuscar.setBorder(new TitledBorder(new LineBorder(new Color(47, 79, 79), 2, true), "Buscar",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCentro.add(panelBuscar, BorderLayout.NORTH);

		GridBagLayout gbl_panelBuscar = new GridBagLayout();
		gbl_panelBuscar.columnWidths = new int[] { 10, 0, 0, 0, 0, 0, 10, 0 };
		gbl_panelBuscar.rowHeights = new int[] { 5, 0, 0, 5, 0 };
		gbl_panelBuscar.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelBuscar.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelBuscar.setLayout(gbl_panelBuscar);

		txtTexto = new JTextField("Texto");
		txtTexto.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtTexto.setBackground(new Color(255, 255, 255));
		txtTexto.setFont(new Font("Arial", Font.ITALIC, 14));
		placeholder.crearPlaceholderText(txtTexto, "Texto");
		GridBagConstraints gbc_txtTexto = new GridBagConstraints();
		gbc_txtTexto.gridwidth = 5;
		gbc_txtTexto.insets = new Insets(0, 0, 5, 5);
		gbc_txtTexto.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTexto.gridx = 1;
		gbc_txtTexto.gridy = 1;
		panelBuscar.add(txtTexto, gbc_txtTexto);

		textField_1 = new JTextField("Teléfono");
		textField_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField_1.setBackground(new Color(255, 255, 255));
		textField_1.setFont(new Font("Arial", Font.ITALIC, 14));
		placeholder.crearPlaceholderText(textField_1, "Teléfono");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		panelBuscar.add(textField_1, gbc_textField_1);

		textField_2 = new JTextField("Contacto");
		textField_2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField_2.setBackground(new Color(255, 255, 255));
		textField_2.setFont(new Font("Arial", Font.ITALIC, 14));
		placeholder.crearPlaceholderText(textField_2, "Contacto");
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 3;
		gbc_textField_2.gridy = 2;
		panelBuscar.add(textField_2, gbc_textField_2);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setBorder(UIManager.getBorder("Button.border"));
		btnBuscar.setBackground(new Color(255, 128, 128));
		btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBuscar.setFocusPainted(false);
		btnBuscar.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 11));
		btnBuscar.setForeground(new Color(255, 255, 255));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 5;
		gbc_btnNewButton.gridy = 2;
		panelBuscar.add(btnBuscar, gbc_btnNewButton);
		btnBuscar.addActionListener(e -> {
			buscarMensajes();
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				btnBuscar.requestFocusInWindow();
			}
		});

		JPanel panel = new JPanel();
		panelCentro.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(205, 235, 234));
		scrollPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane);

		panelMensajes = new JPanel();
		panelMensajes.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelMensajes.setFont(new Font("Arial", Font.PLAIN, 11));
		scrollPane.setViewportView(panelMensajes);

		listaResultados = new JList<>();
		listaResultados.setCellRenderer(new BusquedaCellRenderer());

		JScrollPane scroll = new JScrollPane(listaResultados);
		scroll.setBorder(BorderFactory.createTitledBorder("Resultados de la Búsqueda"));
		panelCentro.add(scroll, BorderLayout.CENTER);

		getRootPane().setDefaultButton(btnBuscar);

		listaResultados.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = listaResultados.locationToIndex(e.getPoint());
					if (index >= 0) {
						Mensaje mensajeSeleccionado = listaResultados.getModel().getElementAt(index);

						Usuario emisor = mensajeSeleccionado.getEmisor();
						Usuario receptor = mensajeSeleccionado.getReceptor();
						Usuario otroUsuario = emisor.equals(AppChat.getUsuarioActual()) ? receptor : emisor;

						ventanaPrincipal.cargarMensajeEnChat(otroUsuario, mensajeSeleccionado);

						CardLayout cardLayout = (CardLayout) ventanaPrincipal.getPanelCentro().getLayout();
						cardLayout.show(ventanaPrincipal.getPanelCentro(), "Chat");

						VentanaBusqueda.this.dispose();
					}
				}
			}
		});

	}

	private void buscarMensajes() {
		String texto = txtTexto.getText();
		String telefono = textField_1.getText();
		String contacto = textField_2.getText();

		if (texto.equals("Texto"))
			texto = "";
		if (telefono.equals("Teléfono"))
			telefono = "";
		if (contacto.equals("Contacto"))
			contacto = "";

		AppChat controlador = AppChat.getUnicaInstancia();
		List<Mensaje> resultados = controlador.filtrarMensajes(texto, telefono, contacto);

		listaResultados.setListData(resultados.toArray(new Mensaje[0]));
		refrescar();
	}

	private void refrescar() {
		panelMensajes.revalidate();
		panelMensajes.repaint();
	}

}
