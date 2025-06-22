package umu.tds.apps.vista.ventanas;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.utils.ImagenUtil;
import umu.tds.apps.utils.MensajeUtil;
import umu.tds.apps.vista.renders.ChatCellRenderer;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import tds.BubbleText;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;

import javax.swing.DefaultComboBoxModel;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentro;
	private JPanel panelBienvenida;
	private JPanel panelChat;
	private JPanel panelChatContenido;
	private JTextField txtMensaje;
	private Usuario receptorActual;
	private Grupo grupoActual;
	private JScrollPane scrollPanelChatMensajes;
	private CardLayout cardLayout;
	private JList<Object> listaContactos;

	public JPanel getPanelCentro() {
		return panelCentro;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				VentanaPrincipal frame = new VentanaPrincipal();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaPrincipal() {
		setTitle("AppChat");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/icono.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 633);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(220, 80, 80));
		panelNorte.setBorder(new LineBorder(new Color(180, 60, 60), 2, true)); // Borde estilo login
		contentPane.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));

		JComboBox<String> comboBoxContactos = new JComboBox<>();
		comboBoxContactos.setModel(new DefaultComboBoxModel<String>(new String[] { "Contacto", "Teléfono" }));
		comboBoxContactos.setBackground(new Color(255, 225, 225));
		comboBoxContactos.setFont(new Font("Arial", Font.BOLD, 12));
		comboBoxContactos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Dimension tamañoCombo = new Dimension(100, 25);
		comboBoxContactos.setPreferredSize(tamañoCombo);
		comboBoxContactos.setMaximumSize(tamañoCombo);
		panelNorte.add(comboBoxContactos);

		JButton btnBuscarPorContactoGrupo = new JButton("");
		Dimension tamañoBuscar = new Dimension(25, 27);
		btnBuscarPorContactoGrupo.setPreferredSize(tamañoBuscar);
		btnBuscarPorContactoGrupo.setMaximumSize(tamañoBuscar);
		btnBuscarPorContactoGrupo.setMinimumSize(tamañoBuscar);
		btnBuscarPorContactoGrupo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBuscarPorContactoGrupo.setFocusPainted(false);
		btnBuscarPorContactoGrupo.setBackground(new Color(255, 187, 187));
		btnBuscarPorContactoGrupo.setForeground(Color.WHITE);
		btnBuscarPorContactoGrupo
				.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/enviar.png")));
		btnBuscarPorContactoGrupo.addActionListener(e -> {
			String seleccion = (String) comboBoxContactos.getSelectedItem();

			if ("Contacto".equals(seleccion)) {
				String nombreContactoOGrupo = JOptionPane.showInputDialog("Introduce el nombre del contacto o grupo:");
				if (nombreContactoOGrupo != null && !nombreContactoOGrupo.trim().isEmpty()) {
					// 1) Localizar el contacto en la listaContactos del usuario actual
					Usuario usuarioActual = AppChat.getUsuarioActual();
					Contacto contacto = usuarioActual.getListaContactos().stream()
							.filter(c -> c.getNombre().equalsIgnoreCase(nombreContactoOGrupo)).findFirst().orElse(null);

					if (contacto == null) {
						MensajeUtil.mostrarMensaje("Contacto o grupo no encontrado.", "Error", new Color(153, 0, 0),
								new Color(255, 230, 230), VentanaPrincipal.this);

						return;
					}

					if (contacto instanceof Grupo) {
						Grupo grupo = (Grupo) contacto;
						cargarChat(grupo);
					} else if (contacto instanceof ContactoIndividual) {
						Usuario usuarioContacto = ((ContactoIndividual) contacto).getUsuario();
						cargarChat(usuarioContacto);
					}
				}
			} else if ("Teléfono".equals(seleccion)) {
				String telefono = JOptionPane.showInputDialog("Introduce el número de teléfono:");
				if (telefono != null && !telefono.trim().isEmpty()) {
					Usuario usuario = AppChat.getUnicaInstancia().obtenerUsuarioPorTelefono(telefono);
					if (usuario != null) {
						cargarChat(usuario);
					} else {
						MensajeUtil.mostrarMensaje("Usuario no encontrado.", "Error", new Color(153, 0, 0),
								new Color(255, 230, 230), VentanaPrincipal.this);

					}
				}
			}
		});

		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea);
		panelNorte.add(btnBuscarPorContactoGrupo);

		JButton btnBuscar = new JButton("");
		btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Dimension lupaTamaño = new Dimension(25, 27);
		btnBuscar.setPreferredSize(lupaTamaño);
		btnBuscar.setMaximumSize(lupaTamaño);
		btnBuscar.setMinimumSize(lupaTamaño);
		btnBuscar.setFocusPainted(false);
		btnBuscar.setBackground(new Color(255, 187, 187));
		btnBuscar.setForeground(Color.WHITE);
		btnBuscar.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/lupa.png")));
		btnBuscar.addActionListener(e -> {
			VentanaBusqueda ventanaBusqueda = new VentanaBusqueda(this);
			ventanaBusqueda.setVisible(true);
		});

		Component rigidArea_1_1 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_1_1);
		panelNorte.add(btnBuscar);

		Component espacioLupaContactos = Box.createRigidArea(new Dimension(15, 0));
		panelNorte.add(espacioLupaContactos);

		JButton btnContactos = new JButton(" Contactos ");
		btnContactos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnContactos.setFocusPainted(false);
		btnContactos.setBackground(new Color(255, 187, 187));
		btnContactos.setForeground(Color.WHITE);
		btnContactos.setFont(new Font("Arial", Font.BOLD, 13));
		btnContactos.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/personas.png")));
		btnContactos.addActionListener(e -> {
			JFrame frameContactos = new JFrame("Crear Grupo o Contacto");
			frameContactos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frameContactos.setSize(600, 400);
			frameContactos.setLocationRelativeTo(null);

			List<String> contactosIniciales = new ArrayList<>();
			contactosIniciales.add("Contacto 1"); // EJEMPLO

			VentanaContactos ventanaContactos = new VentanaContactos();
			frameContactos.getContentPane().add(ventanaContactos);

			frameContactos.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					actualizarListaChats(listaContactos);
				}
			});

			frameContactos.setVisible(true);
		});
		panelNorte.add(btnContactos);

		Component espacioContactosPremium = Box.createRigidArea(new Dimension(15, 0));
		panelNorte.add(espacioContactosPremium);

		JButton btnPremium = new JButton(" Premium ");
		btnPremium.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnPremium.setFocusPainted(false);
		btnPremium.setBackground(new Color(255, 187, 187));
		btnPremium.setForeground(Color.WHITE);
		btnPremium.setFont(new Font("Arial", Font.BOLD, 13));
		btnPremium.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/dolar.png")));

		panelNorte.add(btnPremium);

		Component espacioPremiumpDF = Box.createRigidArea(new Dimension(15, 0));
		panelNorte.add(espacioPremiumpDF);

		Usuario usuarioActual = AppChat.getUsuarioActual();

		Component horizontalGlue = Box.createHorizontalGlue();
		panelNorte.add(horizontalGlue);

		JLabel lblNewLabel = new JLabel(usuarioActual.getUsuario());
		lblNewLabel.setForeground(new Color(248, 248, 255));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel.setIcon(null);
		panelNorte.add(lblNewLabel);

		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_1);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		String path = null;
		if (usuarioActual.getFotoPerfil() != null) {
			path = usuarioActual.getFotoPerfil().getDescription();
		}
		ImagenUtil.cargarImagenPerfil(lblNewLabel_1, path);
		panelNorte.add(lblNewLabel_1);

		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String[] opciones = { "Cambiar Foto de Perfil", "Ver Perfil", "Cancelar" };
				int seleccion = JOptionPane.showOptionDialog(VentanaPrincipal.this, "Selecciona una opción",
						"Opciones de Perfil", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones,
						opciones[2]);

				switch (seleccion) {
				case 0:
					ImagenUtil.seleccionarFotoPerfil(lblNewLabel_1, VentanaPrincipal.this);
					break;
				case 1:
					VentanaPerfil.mostrarPerfil(VentanaPrincipal.this);
					break;
				case 2:
					break;
				}
			}
		});

		JPanel panelMensajes = new JPanel(new BorderLayout());
		panelMensajes.setBackground(new Color(255, 102, 102));
		contentPane.add(panelMensajes, BorderLayout.WEST);

		listaContactos = new JList<>();
		listaContactos.setSelectionBackground(new Color(220, 80, 80));
		listaContactos.setBackground(new Color(255, 185, 185));
		listaContactos.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		listaContactos.setFont(new Font("Arial", Font.PLAIN, 12));
		listaContactos.setFixedCellHeight(120);
		listaContactos.setFixedCellWidth(200);
		listaContactos.setCellRenderer(new ChatCellRenderer());

		JScrollPane scrollPane = new JScrollPane(listaContactos);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		scrollPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelMensajes.add(scrollPane, BorderLayout.WEST);

		panelCentro = new JPanel(new BorderLayout());
		panelCentro.setBackground(Color.WHITE);
		contentPane.add(panelCentro, BorderLayout.CENTER);

		panelBienvenida = new JPanel();
		panelBienvenida.setBackground(new Color(255, 225, 225));
		panelBienvenida.setLayout(new GridBagLayout());

		JLabel lblBienvenida = new JLabel("Seleccione un chat para comenzar a chatear");
		lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
		lblBienvenida.setForeground(Color.BLACK);
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenida.setVerticalAlignment(SwingConstants.CENTER);

		GridBagConstraints gbcLblBienvenida = new GridBagConstraints();
		gbcLblBienvenida.gridx = 0;
		gbcLblBienvenida.gridy = 0; 
		gbcLblBienvenida.weightx = 1.0;
		gbcLblBienvenida.weighty = 1.0; 
		gbcLblBienvenida.anchor = GridBagConstraints.CENTER;
		gbcLblBienvenida.fill = GridBagConstraints.HORIZONTAL;

		panelBienvenida.add(lblBienvenida, gbcLblBienvenida);

		panelCentro.add(panelBienvenida, BorderLayout.CENTER);

		panelChat = new JPanel(new BorderLayout());
		panelChat.setBackground(new Color(255, 225, 225));
		panelChat.setBorder(null);

		scrollPanelChatMensajes = new JScrollPane();
		scrollPanelChatMensajes.setBorder(null);
		scrollPanelChatMensajes.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanelChatMensajes.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanelChatMensajes.setBorder(null);

		Adjustable vertical = scrollPanelChatMensajes.getVerticalScrollBar();
		final boolean[] autoScrollEnabled = { true };
		vertical.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (autoScrollEnabled[0]) {
					vertical.setValue(vertical.getMaximum());
				}
			}
		});

		vertical.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (e.getValueIsAdjusting()) {
					autoScrollEnabled[0] = false;
				} else {
					if (vertical.getValue() + vertical.getVisibleAmount() >= vertical.getMaximum()) {
						autoScrollEnabled[0] = true;
					}
				}
			}
		});

		panelChat.add(scrollPanelChatMensajes, BorderLayout.CENTER);

		panelChatContenido = new JPanel();
		panelChatContenido.setBackground(new Color(255, 225, 225));

		panelChatContenido.setLayout(new BoxLayout(panelChatContenido, BoxLayout.Y_AXIS));
		scrollPanelChatMensajes.setViewportView(panelChatContenido);

		JPanel panelEnvio = new JPanel();
		panelEnvio.setBorder(null);
		panelEnvio.setBackground(new Color(220, 80, 80));
		panelChat.add(panelEnvio, BorderLayout.SOUTH);

		GridBagLayout gbl_panelEnvio = new GridBagLayout();
		gbl_panelEnvio.columnWidths = new int[] { 5, 0, 0, 2, 197, 43, 2, 0 };
		gbl_panelEnvio.rowHeights = new int[] { 5, 21, 0, 0 };
		gbl_panelEnvio.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelEnvio.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelEnvio.setLayout(gbl_panelEnvio);

		JButton btnEmojis = new JButton("");
		btnEmojis.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEmojis.setFocusPainted(false);
		btnEmojis.setBackground(new Color(240, 255, 240));
		btnEmojis
				.setBorder(new CompoundBorder(new TitledBorder(
						new TitledBorder(
								new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
										new Color(160, 160, 160)),
								"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)),
						new EmptyBorder(3, 10, 3, 10)));
		btnEmojis.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/feliz.png")));
		GridBagConstraints gbc_btnEmojis = new GridBagConstraints();
		gbc_btnEmojis.insets = new Insets(0, 0, 5, 5);
		gbc_btnEmojis.gridx = 1;
		gbc_btnEmojis.gridy = 1;
		panelEnvio.add(btnEmojis, gbc_btnEmojis);

		JLayeredPane layeredPane = getLayeredPane();

		JPanel panelEmojis = new EmojiPanel(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numEmoji = Integer.parseInt(e.getActionCommand());

				if (grupoActual != null) {
					// Chat de GRUPO
					boolean enviado = AppChat.getUnicaInstancia().enviarEmojiAGrupo(AppChat.getUsuarioActual(),
							grupoActual, numEmoji);
					if (enviado) {
						MensajeUtil.pintarBubblesEmoji(panelChatContenido, numEmoji, true);
						panelChatContenido.revalidate();
						panelChatContenido.repaint();

						// Actualizar la lista de chats
						actualizarListaChats(listaContactos);
					} else {
						MensajeUtil.mostrarMensaje("No se pudo enviar el emoji al grupo.", "Error", new Color(153, 0, 0),
								new Color(255, 230, 230), VentanaPrincipal.this);

					}
				} else if (receptorActual != null) {
					// Chat INDIVIDUAL (código existente)
					boolean enviado = AppChat.getUnicaInstancia().enviarEmoji(AppChat.getUsuarioActual(),
							receptorActual, numEmoji);
					if (enviado) {
						MensajeUtil.pintarBubblesEmoji(panelChatContenido, numEmoji, true);
						panelChatContenido.revalidate();
						panelChatContenido.repaint();
						actualizarListaChats(listaContactos);
					} else {
						MensajeUtil.mostrarMensaje("No se pudo enviar el emoji.", "Error", new Color(153, 0, 0),
								new Color(255, 230, 230), VentanaPrincipal.this);

					}
				} else {
					MensajeUtil.mostrarMensaje("No hay ningún chat seleccionado para enviar el emoji.", "Advertencia",
							new Color(130, 100, 0), new Color(255, 250, 210), VentanaPrincipal.this);

				}
			}
		});

		layeredPane.add(panelEmojis, JLayeredPane.POPUP_LAYER);

		btnEmojis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!panelEmojis.isVisible()) {
					Point location = btnEmojis.getLocationOnScreen();
					SwingUtilities.convertPointFromScreen(location, layeredPane);
					panelEmojis.setLocation(location.x, location.y - panelEmojis.getHeight());
					panelEmojis.setVisible(true);
				} else {
					panelEmojis.setVisible(false);
				}
			}
		});

		txtMensaje = new JTextField();
		txtMensaje.setToolTipText("Mensaje");
		txtMensaje.setFont(new Font("Arial", Font.PLAIN, 18));
		txtMensaje.setBackground(new Color(245, 245, 245));
		txtMensaje.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_txtMensaje = new GridBagConstraints();
		gbc_txtMensaje.gridwidth = 3;
		gbc_txtMensaje.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMensaje.insets = new Insets(0, 0, 5, 5);
		gbc_txtMensaje.gridx = 2;
		gbc_txtMensaje.gridy = 1;
		panelEnvio.add(txtMensaje, gbc_txtMensaje);
		txtMensaje.setColumns(10);

		JButton btnEnviar = new JButton("Enviar ");
		btnEnviar.setForeground(Color.WHITE);
		btnEnviar.setBackground(new Color(0, 128, 128));
		btnEnviar.setFont(new Font("Arial", Font.BOLD, 12));
		btnEnviar.setBorder(new LineBorder(new Color(0, 100, 100), 2, true));
		btnEnviar.setFocusPainted(false);

		btnEnviar.addActionListener(e -> {
			String textoMensaje = txtMensaje.getText().trim();
			if (!textoMensaje.isEmpty()) {

				if (grupoActual != null) {
					boolean exito = enviarMensajeGrupo(AppChat.getUsuarioActual(), grupoActual, textoMensaje);
					if (exito) {
						String textoConHora = textoMensaje + "  "
								+ String.format("%02d:%02d", LocalTime.now().getHour(), LocalTime.now().getMinute());
						BubbleText burbuja = new BubbleText(panelChatContenido, textoConHora, Color.GREEN, "",
								BubbleText.SENT, 18);
						panelChatContenido.add(burbuja);

						refrescarChat();

						actualizarListaChats(listaContactos);
					} else {
						MensajeUtil.mostrarMensaje("No se pudo enviar el mensaje al grupo.", "Error", new Color(153, 0, 0),
								new Color(255, 230, 230), VentanaPrincipal.this);

					}

					txtMensaje.setText("");
					return;
				}

				Usuario receptor = receptorActual;
				if (receptor != null) {
					AppChat.getUnicaInstancia().enviarMensaje(AppChat.getUsuarioActual(), receptor, textoMensaje);

					String textoConHora = textoMensaje + "  "
							+ String.format("%02d:%02d", LocalTime.now().getHour(), LocalTime.now().getMinute());
					BubbleText burbuja = new BubbleText(panelChatContenido, textoConHora, Color.GREEN, "",
							BubbleText.SENT, 18);
					panelChatContenido.add(burbuja);
					panelChatContenido.revalidate();
					panelChatContenido.repaint();

					actualizarListaChats(listaContactos);
					txtMensaje.setText("");
				} else {
					MensajeUtil.mostrarMensaje("Error al identificar el receptor del mensaje.", "Error", new Color(153, 0, 0),
							new Color(255, 230, 230), VentanaPrincipal.this);

				}
			}
		});

		txtMensaje.addActionListener(e -> btnEnviar.doClick());

		GridBagConstraints gbc_btnEnviar = new GridBagConstraints();
		gbc_btnEnviar.insets = new Insets(0, 0, 5, 5);
		gbc_btnEnviar.gridx = 5;
		gbc_btnEnviar.gridy = 1;
		panelEnvio.add(btnEnviar, gbc_btnEnviar);

		listaContactos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = listaContactos.locationToIndex(e.getPoint());
				if (index >= 0) {
					Object selectedItem = listaContactos.getModel().getElementAt(index);
					if (selectedItem instanceof Chat) {
						Chat chat = (Chat) selectedItem;

						Usuario usuarioActual = AppChat.getUsuarioActual();
						Usuario usuarioOtro = chat.getUsuario().equals(usuarioActual) ? chat.getOtroUsuario()
								: chat.getUsuario();

						ContactoIndividual contactoExistente = AppChat.getUnicaInstancia()
								.obtenerContactoPorTelefono(usuarioOtro.getTelefono());

						if (contactoExistente == null) {
							Rectangle cellBounds = listaContactos.getCellBounds(index, index);
							if (cellBounds != null && cellBounds.contains(e.getPoint())) {
								int relativeX = e.getX() - cellBounds.x;

								int buttonX = cellBounds.width - 40;
								int buttonWidth = 30;

								if (relativeX >= buttonX && relativeX <= buttonX + buttonWidth) {
									String nombreContacto = JOptionPane
											.showInputDialog("Introduce el nombre del contacto:");

									if (nombreContacto != null && !nombreContacto.trim().isEmpty()) {
										boolean agregado = AppChat.getUnicaInstancia().añadirContacto(nombreContacto,
												usuarioOtro.getTelefono());

										if (agregado) {
											actualizarListaChats(listaContactos);
										}
									}
								}
							}
						}
					}
				}
			}
		});

		actualizarListaChats(listaContactos);
		refrescarChat();

		cardLayout = new CardLayout();
		panelCentro.setLayout(cardLayout);

		panelCentro.add(panelBienvenida, "Bienvenida");
		panelCentro.add(panelChat, "Chat");

		cardLayout.show(panelCentro, "Bienvenida");

		listaContactos.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				Object seleccionado = listaContactos.getSelectedValue(); 
				if (seleccionado instanceof Chat) {
					Chat chat = (Chat) seleccionado;
					cargarChat(chat);
					cardLayout.show(panelCentro, "Chat");
				} else if (seleccionado instanceof Grupo) {
					Grupo grupo = (Grupo) seleccionado;
					cargarChat(grupo);
					cardLayout.show(panelCentro, "Chat");
				}
			}
		});

		Component rigidArea_2 = Box.createRigidArea(new Dimension(10, 20)); 
		panelNorte.add(rigidArea_2);

		JButton btnGenerarPDF = new JButton("Generar PDF");
		btnGenerarPDF.setForeground(Color.WHITE);
		btnGenerarPDF.setBackground(new Color(255, 187, 187));
		btnGenerarPDF.setFont(new Font("Arial", Font.BOLD, 13));
		btnGenerarPDF.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnGenerarPDF.setFocusPainted(false);
		btnGenerarPDF.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/umu/tds/apps/resources/pdf.png")));
		btnGenerarPDF.setEnabled(AppChat.getUsuarioActual().isPremium());

		if (AppChat.getUsuarioActual().isPremium()) {
			btnPremium.setText(" Premium ");
			btnPremium.setBackground(new Color(96, 238, 100)); 
		}

		btnPremium.addActionListener(e -> {
			VentanaPremium ventanaPremium = new VentanaPremium();
			ventanaPremium.setModal(true);
			ventanaPremium.setLocationRelativeTo(this);
			ventanaPremium.setVisible(true);

			if (AppChat.getUsuarioActual().isPremium()) {
				// Refrescar la UI
				btnGenerarPDF.setEnabled(true);
				btnPremium.setText(" Premium ");
				btnPremium.setBackground(new Color(96, 238, 100)); 
			}
		});

		btnGenerarPDF.addActionListener(e -> {
			if (!AppChat.getUsuarioActual().isPremium()) {
				MensajeUtil.mostrarMensaje("Esta función solo está disponible para usuarios Premium", "Acceso denegado",
						new Color(130, 100, 0), new Color(255, 250, 210), VentanaPrincipal.this);

				return;
			}

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Guardar PDF");
			fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));

			int seleccion = fileChooser.showSaveDialog(this);
			if (seleccion == JFileChooser.APPROVE_OPTION) {
				String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
				if (!rutaArchivo.toLowerCase().endsWith(".pdf")) {
					rutaArchivo += ".pdf";
				}

				boolean exito = false;

				if (receptorActual != null) {
					exito = AppChat.getUnicaInstancia().generarPDFMensajesUsuario(receptorActual, rutaArchivo);
				} else {
					exito = AppChat.getUnicaInstancia().generarPDFContactos(rutaArchivo);
				}

				if (exito) {
					MensajeUtil.mostrarMensaje("PDF generado correctamente en: " + rutaArchivo, "Éxito", new Color(0, 100, 0),
							new Color(230, 255, 230), VentanaPrincipal.this);

				} else {
					MensajeUtil.mostrarMensaje("Error al generar el PDF", "Error", new Color(153, 0, 0), new Color(255, 230, 230), 
							VentanaPrincipal.this);

				}
			}
		});

		panelNorte.add(btnGenerarPDF);

		;
	}

	private void cargarChat(Usuario contacto) {
		receptorActual = contacto;
		grupoActual = null;

		panelChatContenido.removeAll(); 

		Chat chatExistente = AppChat.getUsuarioActual().obtenerChatCon(contacto);

		if (chatExistente != null && !chatExistente.getMensajes().isEmpty()) {
			for (Mensaje mensaje : chatExistente.getMensajes()) {
				boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());
				MensajeUtil.pintarBubblesMensaje(panelChatContenido, mensaje, esMio);
			}
		} else {
			JLabel noMessagesLabel = new JLabel("No hay mensajes con este contacto.");
			noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
			noMessagesLabel.setForeground(Color.GRAY);
			noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
			panelChatContenido.add(noMessagesLabel);
		}

		refrescarChat();
		cardLayout.show(panelCentro, "Chat");
	}

	private void cargarChat(Grupo grupo) {
		this.grupoActual = grupo;
		this.receptorActual = null;

		panelChatContenido.removeAll();

		List<Mensaje> mensajes = grupo.getMensajesEnviados();

		if (mensajes != null && !mensajes.isEmpty()) {
			for (Mensaje mensaje : mensajes) {
				boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());
				String texto = mensaje.getTexto();

				if (texto != null && texto.startsWith("EMOJI:")) {
					try {
						int emojiCode = Integer.parseInt(texto.substring(6));
						MensajeUtil.pintarBubblesEmoji(panelChatContenido, emojiCode, esMio);
					} catch (NumberFormatException e) {
						System.err.println("Error al parsear código de emoji: " + e.getMessage());
						MensajeUtil.pintarBubblesMensaje(panelChatContenido, mensaje, esMio);
					}
				} else {
					MensajeUtil.pintarBubblesMensaje(panelChatContenido, mensaje, esMio);
				}
			}
		} else {
			JLabel noMessagesLabel = new JLabel("No hay mensajes con este grupo.");
			noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
			noMessagesLabel.setForeground(Color.GRAY);
			noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
			panelChatContenido.add(noMessagesLabel);
		}

		refrescarChat();
		cardLayout.show(panelCentro, "Chat");
	}

	private void cargarChat(Chat chatSeleccionado) {
		Usuario usuarioActual = AppChat.getUsuarioActual();
		Usuario usuario1 = chatSeleccionado.getUsuario();
		Usuario usuario2 = chatSeleccionado.getOtroUsuario();
		Usuario receptor;

		grupoActual = null;

		if (usuario1.equals(usuarioActual)) {
			receptor = usuario2;
		} else if (usuario2.equals(usuarioActual)) {
			receptor = usuario1;
		} else {
			MensajeUtil.mostrarMensaje("El chat seleccionado no involucra al usuario actual.", "Error", new Color(153, 0, 0),
					new Color(255, 230, 230), VentanaPrincipal.this);

			return;
		}

		receptorActual = receptor;
		panelChatContenido.removeAll(); 

		List<Mensaje> mensajes = chatSeleccionado.getMensajes();

		if (mensajes != null && !mensajes.isEmpty()) {
			for (Mensaje mensaje : mensajes) {
				boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());
				MensajeUtil.pintarBubblesMensaje(panelChatContenido, mensaje, esMio);
			}
		} else {
			JLabel noMessagesLabel = new JLabel("No hay mensajes con este contacto.");
			noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
			noMessagesLabel.setForeground(Color.GRAY);
			noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
			panelChatContenido.add(noMessagesLabel);
		}

		refrescarChat();
	}

	private void actualizarListaChats(JList<Object> listaChats) {

		if (listaChats == null) {
			System.out.println("La lista de chats es nula, no se puede actualizar");
			return;
		}

		List<Chat> chats = AppChat.getUsuarioActual().getListaChats();

		chats.sort((c1, c2) -> {
			List<Mensaje> m1 = c1.getMensajes();
			List<Mensaje> m2 = c2.getMensajes();
			if (m1.isEmpty() && m2.isEmpty())
				return 0;
			if (m1.isEmpty())
				return 1; 
			if (m2.isEmpty())
				return -1;

			Mensaje ultimo1 = m1.get(m1.size() - 1);
			Mensaje ultimo2 = m2.get(m2.size() - 1);

			int fechaCompare = ultimo2.getFecha().compareTo(ultimo1.getFecha());
			if (fechaCompare != 0)
				return fechaCompare;

			return ultimo2.getHora().compareTo(ultimo1.getHora());
		});

		List<Grupo> grupos = AppChat.getUsuarioActual().getGrupos();

		List<Object> items = new ArrayList<>();
		items.addAll(chats);
		items.addAll(grupos);

		AbstractListModel<Object> modelo = new AbstractListModel<Object>() {
			private static final long serialVersionUID = 1L;

			@Override
			public int getSize() {
				return items.size();
			}

			@Override
			public Object getElementAt(int index) {
				return items.get(index);
			}
		};

		listaChats.setModel(modelo);

		listaChats.repaint();
	}


	public void refrescarChat() {
		SwingUtilities.invokeLater(() -> {
			panelChatContenido.revalidate();
			panelChatContenido.repaint();
		});
	}

	public void cargarMensajeEnChat(Usuario contacto, Mensaje mensajeBuscado) {
		receptorActual = contacto;
		grupoActual = null;

		final int[] posicionYMensaje = { -1 };

		if (scrollPanelChatMensajes.getVerticalScrollBar().getAdjustmentListeners().length > 0) {
			for (AdjustmentListener listener : scrollPanelChatMensajes.getVerticalScrollBar()
					.getAdjustmentListeners()) {
				scrollPanelChatMensajes.getVerticalScrollBar().removeAdjustmentListener(listener);
			}
		}

		scrollPanelChatMensajes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelChatContenido.removeAll(); 

		Chat chatExistente = AppChat.getUsuarioActual().obtenerChatCon(contacto);

		if (chatExistente != null && !chatExistente.getMensajes().isEmpty()) {
			List<Mensaje> mensajes = chatExistente.getMensajes();
			int totalMensajes = mensajes.size();

			for (int i = 0; i < totalMensajes; i++) {
				Mensaje mensaje = mensajes.get(i);
				boolean esMio = mensaje.getEmisor().equals(AppChat.getUsuarioActual());

				boolean esElMensajeBuscado = (mensajeBuscado != null
						&& mensaje.getCodigo() == mensajeBuscado.getCodigo());

				if (esElMensajeBuscado) {

					JPanel panelMarcaSuperior = new JPanel();
					panelMarcaSuperior.setBackground(new Color(255, 253, 170)); 
					JLabel etiquetaMarcaSuperior = new JLabel("▼ MENSAJE ENCONTRADO ▼");
					etiquetaMarcaSuperior.setFont(new Font("Arial", Font.BOLD, 12));
					etiquetaMarcaSuperior.setForeground(new Color(220, 0, 0)); 
					etiquetaMarcaSuperior.setHorizontalAlignment(SwingConstants.CENTER);
					panelMarcaSuperior.setLayout(new BorderLayout());
					panelMarcaSuperior.add(etiquetaMarcaSuperior, BorderLayout.CENTER);
					panelMarcaSuperior.setMaximumSize(
							new Dimension(Integer.MAX_VALUE, panelMarcaSuperior.getPreferredSize().height));
					panelChatContenido.add(panelMarcaSuperior);

					MensajeUtil.pintarBubblesMensajeResaltado(panelChatContenido, mensaje, esMio);

					JPanel panelMarcaInferior = new JPanel();
					panelMarcaInferior.setBackground(new Color(255, 253, 170)); 
					JLabel etiquetaMarcaInferior = new JLabel("▲ MENSAJE ENCONTRADO ▲");
					etiquetaMarcaInferior.setFont(new Font("Arial", Font.BOLD, 12));
					etiquetaMarcaInferior.setForeground(new Color(220, 0, 0)); 
					etiquetaMarcaInferior.setHorizontalAlignment(SwingConstants.CENTER);
					panelMarcaInferior.setLayout(new BorderLayout());
					panelMarcaInferior.add(etiquetaMarcaInferior, BorderLayout.CENTER);
					panelMarcaInferior.setMaximumSize(
							new Dimension(Integer.MAX_VALUE, panelMarcaInferior.getPreferredSize().height));
					panelChatContenido.add(panelMarcaInferior);

					posicionYMensaje[0] = panelChatContenido.getComponentCount() - 2; 
																						
				} else {
					MensajeUtil.pintarBubblesMensaje(panelChatContenido, mensaje, esMio);
				}
			}
		} else {
			JLabel noMessagesLabel = new JLabel("No hay mensajes con este contacto.");
			noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
			noMessagesLabel.setForeground(Color.GRAY);
			noMessagesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
			panelChatContenido.add(noMessagesLabel);
		}

		refrescarChat();
		cardLayout.show(panelCentro, "Chat");

		if (posicionYMensaje[0] >= 0) {
			for (int delay : new int[] { 200, 500, 1000 }) {
				Timer timer = new Timer(delay, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							Component[] componentes = panelChatContenido.getComponents();

							if (posicionYMensaje[0] < componentes.length) {
								Component targetComponent = componentes[posicionYMensaje[0]];

								Rectangle bounds = targetComponent.getBounds();

								// Hacer scroll solo verticalmente (no horizontalmente)
								scrollPanelChatMensajes.getViewport().setViewPosition(new Point(0, bounds.y));

								// Asegurarse de que el panel se actualice
								panelChatContenido.revalidate();
								scrollPanelChatMensajes.revalidate();
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		}
	}

	public boolean enviarMensajeGrupo(Usuario emisor, Grupo grupo, String texto) {
		return AppChat.getUnicaInstancia().enviarMensajeAGrupo(emisor, grupo, texto);
	}

}