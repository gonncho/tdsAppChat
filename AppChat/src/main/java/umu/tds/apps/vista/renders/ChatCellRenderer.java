package umu.tds.apps.vista.renders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;


import tds.BubbleText;
import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Chat;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.Mensaje;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.utils.IconUtil;
import umu.tds.apps.vista.ventanas.VentanaRegistro;

public class ChatCellRenderer extends JPanel implements ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;

    private JLabel imageLabel;  
    private JLabel previewLabel;
    private JLabel nameLabel;       
    private JButton addButton;      
    private Rectangle addButtonBounds;

    public ChatCellRenderer() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        imageLabel = new JLabel();
        nameLabel = new JLabel();
        previewLabel = new JLabel();
        
        addButton = new JButton("+");
        addButton.setToolTipText("Añadir a contactos");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setForeground(new Color(0, 128, 128));
        addButtonBounds = new Rectangle();

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setOpaque(false);
        topPanel.add(imageLabel, BorderLayout.WEST);
        topPanel.add(nameLabel, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(previewLabel, BorderLayout.CENTER);
        
        
    }

    public JButton getAddButton() {
        return addButton;
    }

    @Override
    // Este método se llama para renderizar cada celda de la lista, donde se hace:
    // 1. Verificar el tipo de objeto (Chat o Grupo).
    // 2. Llamar al método correspondiente para renderizar el objeto.
    // 3. Configurar los colores de fondo y texto según si la celda está seleccionada.
    public Component getListCellRendererComponent(
            JList<? extends Object> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
        if (value instanceof Chat) {
            Chat chat = (Chat) value;
            renderChat(list, chat, index, isSelected);
        } 
        else if (value instanceof Grupo) {
            Grupo grupo = (Grupo) value;
            renderGrupo(list, grupo, index, isSelected);
        } 
        else {
            setBackground(list.getBackground());
            nameLabel.setText("(Objeto desconocido)");
            previewLabel.setText("");
            imageLabel.setIcon(null);
            addButton.setVisible(false);

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                nameLabel.setForeground(list.getSelectionForeground());
                previewLabel.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                nameLabel.setForeground(list.getForeground());
                previewLabel.setForeground(Color.DARK_GRAY);
            }
        }

        if (value instanceof Chat) {
            Chat chat = (Chat) value;
            Usuario usuarioActual = AppChat.getUsuarioActual();
            Usuario usuarioOtro = chat.getUsuario().equals(usuarioActual) ? 
                                  chat.getOtroUsuario() : chat.getUsuario();
            
            ContactoIndividual contactoExistente = AppChat.getUnicaInstancia()
                    .obtenerContactoPorTelefono(usuarioOtro.getTelefono());
            
            addButton.setVisible(contactoExistente == null);
        } else {

            addButton.setVisible(false);
        }

        return this;
    }
    
    
    private void renderChat(JList<?> list, Chat chat, int index, boolean isSelected) {
        Usuario usuarioActual = AppChat.getUsuarioActual();
        Usuario usuarioOtro = (chat.getUsuario().equals(usuarioActual))
                ? chat.getOtroUsuario()
                : chat.getUsuario();

        ContactoIndividual contacto = AppChat.getUnicaInstancia()
                .obtenerContactoPorTelefono(usuarioOtro.getTelefono());
        if (contacto != null) {
            nameLabel.setText(contacto.getNombre());
            addButton.setVisible(false);
        } else {
            nameLabel.setText(usuarioOtro.getTelefono());
            addButton.setVisible(true);
        }

        String path = usuarioOtro.getFotoPerfil().getDescription();
        setProfileImage(path);

        List<Mensaje> mensajes = chat.getMensajes();
        if (!mensajes.isEmpty()) {
            Mensaje ultimo = mensajes.get(mensajes.size() - 1);
            String texto = ultimo.getTexto();
            String horaStr = String.format("%02d:%02d",
                    ultimo.getHora().getHour(), 
                    ultimo.getHora().getMinute());

            if (texto != null && texto.startsWith("EMOJI:")) {
                try {
                    int emojiCode = Integer.parseInt(texto.substring(6));
                    ImageIcon emojiIcon = BubbleText.getEmoji(emojiCode);
                    if (emojiIcon != null) {
                        previewLabel.setIcon(emojiIcon);
                        previewLabel.setText(" " + horaStr);
                    } else {
                        previewLabel.setText("[Emoji] " + horaStr);
                        previewLabel.setIcon(null);
                    }
                } catch (NumberFormatException e) {
                    previewLabel.setText(texto + "  " + horaStr);
                    previewLabel.setIcon(null);
                }
            } else if (texto != null) {
                if (texto.length() > 20) {
                    texto = texto.substring(0, 20) + "...";
                }
                previewLabel.setText(texto + "  " + horaStr);
                previewLabel.setIcon(null);
            } else {
                previewLabel.setText("[Mensaje sin contenido] " + horaStr);
                previewLabel.setIcon(null);
            }
        } else {
            previewLabel.setText("No hay mensajes");
            previewLabel.setIcon(null);
        }

        applySelectionColors(list, isSelected);
    }

    private void renderGrupo(JList<?> list, Grupo grupo, int index, boolean isSelected) {
        nameLabel.setText(grupo.getNombreGrupo());
        addButton.setVisible(false);  

        ImageIcon iconGrupo = grupo.getImagenGrupo(); 
        if (iconGrupo != null) {
            try {
                Image img = iconGrupo.getImage();
                BufferedImage buffImg;
                if (img instanceof BufferedImage) {
                    buffImg = (BufferedImage) img;
                } else {
                    buffImg = new BufferedImage(
                        img.getWidth(null), 
                        img.getHeight(null), 
                        BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = buffImg.createGraphics();
                    g2d.drawImage(img, 0, 0, null);
                    g2d.dispose();
                }
                imageLabel.setIcon(IconUtil.createCircularIcon(buffImg, 50));
            } catch (Exception e) {
                imageLabel.setIcon(iconGrupo);
            }
        } else {
            imageLabel.setIcon(new ImageIcon(ChatCellRenderer.class.getResource("/umu/tds/apps/resources/personas.png")));
        }

        List<Mensaje> mensajes = grupo.getMensajesEnviados();
        if (mensajes != null && !mensajes.isEmpty()) {
            Mensaje ultimoMensaje = mensajes.get(mensajes.size() - 1);
            String texto = ultimoMensaje.getTexto();
            String horaStr = String.format("%02d:%02d",
                    ultimoMensaje.getHora().getHour(), 
                    ultimoMensaje.getHora().getMinute());

            if (texto != null && texto.startsWith("EMOJI:")) {
                try {
                    int emojiCode = Integer.parseInt(texto.substring(6));
                    ImageIcon emojiIcon = BubbleText.getEmoji(emojiCode);
                    if (emojiIcon != null) {
                        previewLabel.setIcon(emojiIcon);
                        previewLabel.setText(" " + horaStr);
                    } else {
                        previewLabel.setText("[Emoji] " + horaStr);
                        previewLabel.setIcon(null);
                    }
                } catch (NumberFormatException e) {
                    previewLabel.setText(texto + "  " + horaStr);
                    previewLabel.setIcon(null);
                }
            } else if (texto != null) {
                if (texto.length() > 20) {
                    texto = texto.substring(0, 20) + "...";
                }
                previewLabel.setText(texto + "  " + horaStr);
                previewLabel.setIcon(null);
            } else {
                previewLabel.setText("[Mensaje sin contenido] " + horaStr);
                previewLabel.setIcon(null);
            }
        } else {
            previewLabel.setText("Sin historial"); 
            previewLabel.setIcon(null);
        }

        applySelectionColors(list, isSelected);
    }

    private void applySelectionColors(JList<?> list, boolean isSelected) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            nameLabel.setForeground(list.getSelectionForeground());
            previewLabel.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            nameLabel.setForeground(list.getForeground());
            previewLabel.setForeground(Color.DARK_GRAY);
        }
    }

    @SuppressWarnings("deprecation")
    private void setProfileImage(String path) {
        try {
            BufferedImage image;
            if (path.startsWith("http")) {
                image = ImageIO.read(new URL(path));
            } else if (path.startsWith("/")) {
                image = ImageIO.read(VentanaRegistro.class.getResource(path));
            } else {
                image = ImageIO.read(new File(path));
            }

            ImageIcon icon = IconUtil.createCircularIcon(image, 50);
            imageLabel.setIcon(icon);

        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
            imageLabel.setIcon(null);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }

    
    public Rectangle getAddButtonBounds() {
        return addButtonBounds;
    }
}
