package umu.tds.apps.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import umu.tds.apps.controlador.AppChat;

public class ImagenUtil {
	
	@SuppressWarnings("deprecation")
	public static void cargarImagenPerfil(JLabel label, String path) {
		if (path == null || path.isEmpty()) {
			ImageIcon iconoBase = new ImageIcon(ImagenUtil.class.getResource("/umu/tds/apps/resources/usuario.png"));
			Image imagenEscalada = iconoBase.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(imagenEscalada));
			return;
		}
		try {
			BufferedImage imagen;
			if (path.startsWith("http")) {
				imagen = ImageIO.read(new URL(path));
			} else {
				imagen = ImageIO.read(new File(path));
			}
			if (imagen == null) {
				throw new IOException("La imagen devuelta es nula");
			}
			ImageIcon icon = IconUtil.createCircularIcon(imagen, 50);
			label.setIcon(icon);
		} catch (IOException e) {
			ImageIcon iconoBase = new ImageIcon(ImagenUtil.class.getResource("/umu/tds/apps/resources/usuario.png"));
			Image imagenEscalada = iconoBase.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(imagenEscalada));

		}
	}
	
	@SuppressWarnings("deprecation")
	public static void seleccionarFotoPerfil(JLabel label, Component contentPane) {
		String[] opciones = { "Introducir enlace", "Seleccionar archivo" };
		int seleccion = JOptionPane.showOptionDialog(contentPane, "Seleccione cómo desea cargar la imagen:",
				"Cargar Imagen", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones,
				opciones[0]);

		if (seleccion == 0) {
			String urlImagen = JOptionPane.showInputDialog(contentPane, "Introduzca el enlace de la imagen:",
					"Cargar Imagen desde URL", JOptionPane.PLAIN_MESSAGE);

			if (urlImagen != null && !urlImagen.isEmpty()) {
				try {
					BufferedImage imagen = ImageIO.read(new URL(urlImagen));

					if (imagen == null) {
						throw new IOException("No se pudo cargar la imagen. El formato puede no ser compatible.");
					}

					ImageIcon icono = new ImageIcon(imagen.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
					icono.setDescription(urlImagen);

					AppChat.getUnicaInstancia().cambiarImagenPerfil(icono);

					label.setIcon(IconUtil.createCircularIcon(imagen, 50));
					label.revalidate();
					label.repaint();

					MensajeUtil.mostrarMensaje("Foto de perfil actualizada correctamente.", "Éxito", new Color(0, 100, 0),
							new Color(230, 255, 230), contentPane);
				} catch (IOException ex) {
					MensajeUtil.mostrarMensaje("No se pudo cargar la imagen desde el enlace: " + ex.getMessage(), "Error",
							new Color(153, 0, 0), new Color(255, 230, 230), contentPane);
				}
			}
		} else if (seleccion == 1) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));
			int resultado = fileChooser.showOpenDialog(contentPane);
			if (resultado == JFileChooser.APPROVE_OPTION) {
				try {
					File archivo = fileChooser.getSelectedFile();
					BufferedImage imagen = ImageIO.read(archivo);

					if (imagen == null) {
						throw new IOException("No se pudo cargar la imagen. El formato puede no ser compatible.");
					}

					ImageIcon icono = new ImageIcon(imagen.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
					icono.setDescription(archivo.getAbsolutePath());

					AppChat.getUnicaInstancia().cambiarImagenPerfil(icono);

					label.setIcon(IconUtil.createCircularIcon(imagen, 50));
					label.revalidate();
					label.repaint();

					MensajeUtil.mostrarMensaje("Foto de perfil actualizada correctamente.", "Éxito", new Color(0, 100, 0),
							new Color(230, 255, 230), contentPane);
				} catch (IOException ex) {
					MensajeUtil.mostrarMensaje("No se pudo cargar la imagen desde el archivo: " + ex.getMessage(), "Error",
							new Color(153, 0, 0), new Color(255, 230, 230), contentPane);
				}
			}
		}
	}
}