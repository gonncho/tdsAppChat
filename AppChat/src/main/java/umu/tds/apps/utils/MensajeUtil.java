package umu.tds.apps.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tds.BubbleText;
import umu.tds.apps.modelo.Mensaje;

public class MensajeUtil {
	
	public static void mostrarMensaje(String texto, String titulo, Color colorTexto, Color fondo, Component contentPane) {
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
	
	public static void pintarBubblesEmoji(JPanel panel, int emoji, boolean esMio) {
		BubbleText burbuja = new BubbleText(panel, emoji, esMio ? Color.GREEN : Color.LIGHT_GRAY, "",
				esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
		panel.add(burbuja);
	}
	
	public static void pintarBubblesMensaje(JPanel panel, Mensaje mensaje, boolean esMio) {
		String texto = mensaje.getTexto();
		if (texto != null && texto.startsWith("EMOJI:")) {
			try {
				int emojiCode = Integer.parseInt(texto.substring(6));
				pintarBubblesEmoji(panel, emojiCode, esMio);
			} catch (NumberFormatException e) {
				BubbleText burbuja = new BubbleText(panel, "[Emoji inválido]", esMio ? Color.GREEN : Color.LIGHT_GRAY,
						"", esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
				panel.add(burbuja);
			}

		} else if (texto != null) {
			BubbleText burbuja = new BubbleText(panel,
					texto + "  "
							+ String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
					esMio ? Color.GREEN : Color.LIGHT_GRAY, "", esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
			panel.add(burbuja);
		} else {
			BubbleText burbuja = new BubbleText(panel,
					"[Mensaje sin contenido] "
							+ String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
					esMio ? Color.GREEN : Color.LIGHT_GRAY, "", esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
			panel.add(burbuja);
		}
	}
	
	public static void pintarBubblesMensajeResaltado(JPanel panel, Mensaje mensaje, boolean esMio) {
		String texto = mensaje.getTexto();

		if (texto != null && texto.startsWith("EMOJI:")) {
			try {
				int emojiCode = Integer.parseInt(texto.substring(6));
				BubbleText burbuja = new BubbleText(panel, emojiCode,
						esMio ? new Color(200, 255, 200) : new Color(255, 253, 170), "",
						esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
				panel.add(burbuja);
			} catch (NumberFormatException e) {
				BubbleText burbuja = new BubbleText(panel, "[Emoji inválido]",
						esMio ? new Color(200, 255, 200) : new Color(255, 253, 170), "",
						esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
				panel.add(burbuja);
			}
		} else if (texto != null) {
			BubbleText burbuja = new BubbleText(panel,
					texto + "  "
							+ String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
					esMio ? new Color(200, 255, 200) : new Color(255, 253, 170), "",
					esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
			panel.add(burbuja);
		} else {
			BubbleText burbuja = new BubbleText(panel,
					"[Mensaje sin contenido] "
							+ String.format("%02d:%02d", mensaje.getHora().getHour(), mensaje.getHora().getMinute()),
					esMio ? new Color(200, 255, 200) : new Color(255, 253, 170), "",
					esMio ? BubbleText.SENT : BubbleText.RECEIVED, 18);
			panel.add(burbuja);
		}
	}

}
