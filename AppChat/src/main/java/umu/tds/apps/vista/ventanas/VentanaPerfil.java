package umu.tds.apps.vista.ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.utils.IconUtil;


public class VentanaPerfil extends JDialog {
	
    private static final long serialVersionUID = 1L;
    private JTextArea txtSaludo;
    private Usuario usuarioActual;

    @SuppressWarnings("deprecation")
	public VentanaPerfil(JFrame parent) {
        super(parent, "Mi Perfil", true);
        setSize(400, 550);
        setLocationRelativeTo(parent);
        
        // Obtener usuario actual
        usuarioActual = AppChat.getUsuarioActual();
        
        // Panel principal con fondo degradado
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(255, 128, 128));
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel para la foto de perfil
        JPanel panelFoto = new JPanel(new BorderLayout());
        panelFoto.setOpaque(false);
        
        // Foto de perfil
        JLabel labelFoto = new JLabel();
        labelFoto.setHorizontalAlignment(JLabel.CENTER);
        
        try {
            // Cargar y redimensionar imagen de perfil
            String rutaImagen = usuarioActual.getFotoPerfil().getDescription();
            BufferedImage imagen;
            
            if (rutaImagen.startsWith("http")) {
                imagen = ImageIO.read(new URL(rutaImagen));
            } else {
                imagen = ImageIO.read(new File(rutaImagen));
            }
            
            // icono circular
            ImageIcon iconoPerfil = IconUtil.createCircularIcon(imagen, 200);
            labelFoto.setIcon(iconoPerfil);
        } catch (IOException e) {
            // Imagen por defecto si hay error
            ImageIcon iconoPorDefecto = new ImageIcon(
                VentanaPerfil.class.getResource("/umu/tds/apps/resources/usuario.png")
            );
            Image imagenEscalada = iconoPorDefecto.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            labelFoto.setIcon(new ImageIcon(imagenEscalada));
        }
        
        panelFoto.add(labelFoto, BorderLayout.CENTER);
        
        // Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setBackground(new Color(255, 128, 128));
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        
        // Estilos para etiquetas

        Font fuenteInfo = new Font("Arial", Font.PLAIN, 14);
        Color colorTexto = Color.WHITE;
        
        // Etiquetas de información
        JLabel lblNombre = new JLabel("Nombre: " + usuarioActual.getUsuario());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre.setForeground(colorTexto);
        
        JLabel lblTelefono = new JLabel("Teléfono: " + usuarioActual.getTelefono());
        lblTelefono.setFont(new Font("Arial", Font.BOLD, 14));
        lblTelefono.setForeground(colorTexto);
        
        JLabel lblFechaNacimiento = new JLabel("Fecha de Nacimiento: " + usuarioActual.getFechaNacimiento());
        lblFechaNacimiento.setFont(new Font("Arial", Font.BOLD, 14));
        lblFechaNacimiento.setForeground(colorTexto);
        
        // Panel para saludo
        JPanel panelSaludo = new JPanel(new BorderLayout());
        panelSaludo.setOpaque(false);
        
        JLabel lblSaludoTitulo = new JLabel("Saludo:");
        lblSaludoTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaludoTitulo.setForeground(colorTexto);
        
        txtSaludo = new JTextArea(usuarioActual.getSaludo().orElse("Escribe tu saludo aquí..."));
        txtSaludo.setFont(fuenteInfo);
        txtSaludo.setForeground(Color.DARK_GRAY);
        txtSaludo.setLineWrap(true);
        txtSaludo.setWrapStyleWord(true);
        txtSaludo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txtSaludo.setPreferredSize(new Dimension(300, 80));
        
        JButton btnGuardarSaludo = new JButton("Guardar Saludo");
        btnGuardarSaludo.setBackground(new Color(255, 64, 64));
        btnGuardarSaludo.setForeground(Color.WHITE);
        btnGuardarSaludo.addActionListener(e -> guardarSaludo());
        
        panelSaludo.add(lblSaludoTitulo, BorderLayout.NORTH);
        panelSaludo.add(new JScrollPane(txtSaludo), BorderLayout.CENTER);
        panelSaludo.add(btnGuardarSaludo, BorderLayout.SOUTH);
        
        JLabel lblEstadoPremium = new JLabel(
            usuarioActual.isPremium() ? "Estado: Usuario Premium" : "Estado: Usuario Estándar"
        );
        lblEstadoPremium.setFont(new Font("Arial", Font.BOLD, 14));
        lblEstadoPremium.setForeground(colorTexto);
        
        // Añadir componentes
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblTelefono);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblFechaNacimiento);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblEstadoPremium);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(panelSaludo);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setOpaque(false);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnCerrar);
        
        // Añadir al panel principal
        panelPrincipal.add(panelFoto, BorderLayout.NORTH);
        panelPrincipal.add(panelInfo, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        // Establecer contenido
        setContentPane(panelPrincipal);
    }

    private void guardarSaludo() {
        String nuevoSaludo = txtSaludo.getText().trim();
        
        if (nuevoSaludo.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, 
                "El saludo no puede estar vacío.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        try {
            // Actualizar el saludo en el modelo
            AppChat.getUnicaInstancia().cambiarSaludo(nuevoSaludo);
            
            JOptionPane.showMessageDialog(
                this, 
                "Saludo actualizado correctamente.", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, 
                "Error al guardar el saludo: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void mostrarPerfil(JFrame padre) {
        VentanaPerfil ventanaPerfil = new VentanaPerfil(padre);
        ventanaPerfil.setVisible(true);
    }
}