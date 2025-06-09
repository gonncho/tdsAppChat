package vista;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import tds.BubbleText;
import java.util.function.BiConsumer;

public class VentanaPrincipal extends JFrame {

    private final JComboBox<String>    comboContactos;
    private final JButton              btnContactos, btnPremium, btnBuscar;
    private final JLabel               lblUsuario, lblFotoPerfil;
    private final DefaultListModel<String> contactosModel;
    private final JList<String>             listContactos;
    private final JPanel               panelConversacion;
    private final JTextField           txtMensaje;
    private final JButton              btnEnviar, btnEmojis;
    private final JPopupMenu           emojiMenu;

    public VentanaPrincipal() {
        super("AppChat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        panelConversacion = new JPanel();
        panelConversacion.setLayout(new BoxLayout(panelConversacion, BoxLayout.Y_AXIS));

        // Norte: barra de usuario
        JPanel north = new JPanel();
        lblUsuario    = new JLabel("Usuario");
        lblFotoPerfil = new JLabel(new ImageIcon()); // placeholder
        btnContactos = new JButton("Contactos");
        btnPremium   = new JButton("Premium");
        btnBuscar    = new JButton("Buscar");
        north.add(btnContactos);
        north.add(btnPremium);
        north.add(btnBuscar);
        north.add(lblUsuario);
        north.add(lblFotoPerfil);
        add(north, BorderLayout.NORTH);

        // Oeste: lista de contactos
        contactosModel = new DefaultListModel<>();
        listContactos  = new JList<>(contactosModel);
        add(new JScrollPane(listContactos), BorderLayout.WEST);

        // Centro: chat
        JScrollPane scroll = new JScrollPane(panelConversacion,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        // Sur: envío de mensaje
        JPanel south = new JPanel();
        txtMensaje  = new JTextField(20);
        btnEmojis   = new JButton("😃");
        btnEnviar   = new JButton("Enviar");
        south.add(txtMensaje);
        south.add(btnEmojis);
        south.add(btnEnviar);
        add(south, BorderLayout.SOUTH);

        // Menu de emojis
        emojiMenu = new JPopupMenu();
        for (int i = 0; i < 8; i++) {
            JMenuItem it = new JMenuItem(new ImageIcon(BubbleText.getEmoji(i)));
            final int idx=i;
            it.addActionListener(e -> fireEmojiSent(idx));
            emojiMenu.add(it);
        }
        btnEmojis.addActionListener(e ->
            emojiMenu.show(btnEmojis, 0, btnEmojis.getHeight())
        );

        pack();
        setLocationRelativeTo(null);
    }

    // — Exponer modelo de contactos y selección —
    public void setContactos(java.util.List<String> lista) {
        contactosModel.clear();
        lista.forEach(contactosModel::addElement);
    }
    public String getContactoSeleccionado() {
        return listContactos.getSelectedValue();
    }

    /** Texto introducido en el campo de mensaje */
    public String getTextoMensaje() {
        return txtMensaje.getText().trim();
    }

    /** Muestra el nombre e imagen del usuario logado */
    public void setUsuarioInfo(String nombre, Icon foto) {
        lblUsuario.setText(nombre != null ? nombre : "Usuario");
        lblFotoPerfil.setIcon(foto);
    }
    
    // — Métodos para inyectar listeners —
    public void addContactosListener(ActionListener l)   { btnContactos.addActionListener(l); }
    public void addPremiumListener(ActionListener l)     { btnPremium.addActionListener(l); }
    public void addBuscarListener(ActionListener l)      { btnBuscar.addActionListener(l); }
    public void addSendListener(ActionListener l)        { btnEnviar.addActionListener(l); }
    public void addMessageKeyListener(KeyListener l)     { txtMensaje.addKeyListener(l); }
    public void addContactSelectionListener(ListSelectionListener l) {
        listContactos.addListSelectionListener(l);
    }

    // — Callbacks internos para emojis —
    private BiConsumer<String,Integer> onEmojiSent;
    public void setEmojiSentHandler(BiConsumer<String,Integer> h) {
        this.onEmojiSent = h;
    }
    private void fireEmojiSent(int idx) {
        if (onEmojiSent != null) onEmojiSent.accept(getContactoSeleccionado(), idx);
    }

    // — Añadir burbujas al chat —
    public void addSentBubble(String texto) {
        BubbleText bt = new BubbleText(panelConversacion, texto, Color.GREEN, "Yo", BubbleText.SENT);
        panelConversacion.add(bt);
        panelConversacion.revalidate();
        panelConversacion.repaint();
        scrollToBottom();
        txtMensaje.setText("");
    }

    public void addReceivedBubble(String autor, String texto) {
        BubbleText bt = new BubbleText(panelConversacion, texto, Color.LIGHT_GRAY, autor, BubbleText.RECEIVED);
        panelConversacion.add(bt);
        panelConversacion.revalidate();
        panelConversacion.repaint();
        scrollToBottom();
    }
    
    /** Elimina todas las burbujas del panel de conversación */
    public void limpiarConversacion() {
        panelConversacion.removeAll();
        panelConversacion.revalidate();
        panelConversacion.repaint();
    }


    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = ((JScrollPane) panelConversacion.getParent().getParent())
                              .getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    // — Mostrar / ocultar —
    public void mostrar() { setVisible(true); }
    public void ocultar()  { setVisible(false); }
}
