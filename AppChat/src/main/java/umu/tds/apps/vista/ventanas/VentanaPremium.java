// VentanaPremium.java
package umu.tds.apps.vista.ventanas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.estrategia.EstrategiaDescuento;
import umu.tds.apps.estrategia.descuento.DescuentoFecha;
import umu.tds.apps.estrategia.descuento.DescuentoMensaje;

import java.awt.Color;
import java.awt.Font;

public class VentanaPremium extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblPrecio;
	private double precioActual;
	private boolean descuentoFechaAplicado = false;
	private boolean descuentoMensajeAplicado = false;

	public static void main(String[] args) {
		try {
			VentanaPremium dialog = new VentanaPremium();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public VentanaPremium() {
		setTitle("Activar Premium");
		setBounds(100, 100, 565, 376);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		precioActual = AppChat.PRECIO_PREMIUM;

		JPanel panelPremium = new JPanel();
		panelPremium.setBackground(new Color(255, 185, 185));
		panelPremium.setBorder(new TitledBorder("Activación Premium"));
		contentPanel.add(panelPremium, BorderLayout.CENTER);
		GridBagLayout gbl_panelPremium = new GridBagLayout();
		gbl_panelPremium.columnWidths = new int[] { 25, 111, 0, 191, 25, 0 };
		gbl_panelPremium.rowHeights = new int[] { 10, 20, 20, 17, 25, 0, 0 };
		gbl_panelPremium.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelPremium.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panelPremium.setLayout(gbl_panelPremium);

		JLabel lblFechaRegistro = new JLabel("Fecha de Registro: " + AppChat.getUsuarioActual().getFechaRegistro());
		lblFechaRegistro.setFont(new Font("Arial", Font.BOLD, 11));
		GridBagConstraints gbc_lblFechaRegistro = new GridBagConstraints();
		gbc_lblFechaRegistro.gridwidth = 3;
		gbc_lblFechaRegistro.fill = GridBagConstraints.BOTH;
		gbc_lblFechaRegistro.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaRegistro.gridx = 1;
		gbc_lblFechaRegistro.gridy = 1;
		panelPremium.add(lblFechaRegistro, gbc_lblFechaRegistro);

		JButton btnCalcularDescuentoFecha = new JButton("Aplicar Descuento por Fecha");
		btnCalcularDescuentoFecha.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCalcularDescuentoFecha.setForeground(new Color(255, 255, 255));
		btnCalcularDescuentoFecha.setBackground(new Color(255, 113, 113));
		btnCalcularDescuentoFecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aplicarDescuentoPorFecha();
			}
		});

		JLabel lblMensajesEnviados = new JLabel(
				"Mensajes enviados: " + AppChat.getUsuarioActual().getNumeroMensajesUltimoMes());
		lblMensajesEnviados.setFont(new Font("Arial", Font.BOLD, 11));
		GridBagConstraints gbc_lblMensajesEnviados = new GridBagConstraints();
		gbc_lblMensajesEnviados.gridwidth = 3;
		gbc_lblMensajesEnviados.fill = GridBagConstraints.BOTH;
		gbc_lblMensajesEnviados.insets = new Insets(0, 0, 5, 5);
		gbc_lblMensajesEnviados.gridx = 1;
		gbc_lblMensajesEnviados.gridy = 2;
		panelPremium.add(lblMensajesEnviados, gbc_lblMensajesEnviados);

		lblPrecio = new JLabel("Precio Final: " + String.format("%.2f", precioActual) + " €");
		lblPrecio.setFont(new Font("Arial", Font.BOLD, 14));
		GridBagConstraints gbc_lblPrecio = new GridBagConstraints();
		gbc_lblPrecio.insets = new Insets(0, 0, 5, 0);
		gbc_lblPrecio.gridwidth = 3;
		gbc_lblPrecio.gridx = 1;
		gbc_lblPrecio.gridy = 3;
		panelPremium.add(lblPrecio, gbc_lblPrecio);
		GridBagConstraints gbc_btnCalcularDescuentoFecha = new GridBagConstraints();
		gbc_btnCalcularDescuentoFecha.insets = new Insets(0, 0, 5, 5);
		gbc_btnCalcularDescuentoFecha.gridx = 1;
		gbc_btnCalcularDescuentoFecha.gridy = 4;
		panelPremium.add(btnCalcularDescuentoFecha, gbc_btnCalcularDescuentoFecha);

		JButton btnCalcularDescuentoMensajes = new JButton("Aplicar Descuento por Mensajes");
		btnCalcularDescuentoMensajes.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCalcularDescuentoMensajes.setForeground(new Color(255, 255, 255));
		btnCalcularDescuentoMensajes.setBackground(new Color(255, 113, 113));
		btnCalcularDescuentoMensajes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aplicarDescuentoPorMensajes();
			}
		});
		GridBagConstraints gbc_btnCalcularDescuentoMensajes = new GridBagConstraints();
		gbc_btnCalcularDescuentoMensajes.insets = new Insets(0, 0, 5, 5);
		gbc_btnCalcularDescuentoMensajes.gridx = 3;
		gbc_btnCalcularDescuentoMensajes.gridy = 4;
		panelPremium.add(btnCalcularDescuentoMensajes, gbc_btnCalcularDescuentoMensajes);

		// Calcular precio inicial
		precioActual = AppChat.getUnicaInstancia().calcularPrecioPremium();

		// Botón para activar Premium
		JButton btnActivarPremium = new JButton("Activar Premium");
		btnActivarPremium.setFont(new Font("Arial", Font.BOLD, 12));
		btnActivarPremium.setForeground(new Color(255, 255, 255));
		btnActivarPremium.setBackground(new Color(255, 66, 66));
		btnActivarPremium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activarPremium();
			}
		});

		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(new Color(255, 185, 185));
		contentPanel.add(panelBotones, BorderLayout.SOUTH);
		panelBotones.add(btnActivarPremium);

		if (AppChat.getUsuarioActual().isPremium()) {
			JOptionPane.showMessageDialog(this, "¡Ya eres Premium! Disfruta de las ventajas.",
					"Usuario Premium", JOptionPane.INFORMATION_MESSAGE);

			btnActivarPremium.setText("Ya eres Premium");
			btnActivarPremium.setEnabled(false);
			btnCalcularDescuentoFecha.setEnabled(false);
			btnCalcularDescuentoMensajes.setEnabled(false);

			lblPrecio.setText("Usuario Premium Activo");
		}
	}

	private void aplicarDescuentoPorFecha() {

		if (descuentoFechaAplicado) {
			JOptionPane.showMessageDialog(this, "El descuento por fecha ya ha sido aplicado.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {

			LocalDate fechaRegistro = AppChat.getUsuarioActual().getFechaRegistro();

			if (fechaRegistro == null) {
				JOptionPane.showMessageDialog(this, "La fecha de registro no está disponible.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}


			if (ChronoUnit.DAYS.between(fechaRegistro, LocalDate.now()) >= 7) {
				JOptionPane.showMessageDialog(this, "Tu fecha de registro no cumple los requisitos para el descuento.",
						"Información", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			EstrategiaDescuento estrategia = new EstrategiaDescuento();
			estrategia.setEstrategiaDescuento(new DescuentoFecha(fechaRegistro));
			precioActual = estrategia.calcularPrecioFinal(precioActual);

			lblPrecio.setText("Precio Final: " + String.format("%.2f", precioActual) + " €");

			JOptionPane.showMessageDialog(this, "¡Descuento aplicado! Precio reducido un 20%.", "Descuento aplicado",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al aplicar el descuento: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		descuentoFechaAplicado = true;
	}

	private void aplicarDescuentoPorMensajes() {

		if (descuentoMensajeAplicado) {
			JOptionPane.showMessageDialog(this, "El descuento por mensajes ya ha sido aplicado.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {

			long mensajesEnviados = AppChat.getUsuarioActual().getNumeroMensajesUltimoMes();

			if (mensajesEnviados <=5) {
				JOptionPane.showMessageDialog(this, "Necesitas más de 5 mensajes enviados para recibir uno de los descuentos.",
						"Información", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			double precioBase = AppChat.PRECIO_PREMIUM;
			EstrategiaDescuento estrategia = new EstrategiaDescuento();
			estrategia.setEstrategiaDescuento(new DescuentoMensaje((int) mensajesEnviados));

			precioActual = estrategia.calcularPrecioFinal(precioBase);

			lblPrecio.setText("Precio Final: " + String.format("%.2f", precioActual) + " €");

			int porcentajeDescuento = 0;
			if (mensajesEnviados > 10)
				porcentajeDescuento = 35;
			else if (mensajesEnviados > 7)
				porcentajeDescuento = 30;
			else if (mensajesEnviados > 5)
				porcentajeDescuento = 25;

			JOptionPane.showMessageDialog(this, "Se ha aplicado el descuento. Precio reducido un " + porcentajeDescuento + "%.",
					"Descuento aplicado", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al aplicar el descuento: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		descuentoMensajeAplicado = true;
	}

	private void activarPremium() {

		if (AppChat.getUsuarioActual().isPremium()) {
			JOptionPane.showMessageDialog(this, "Ya eres un usuario Premium.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
			return;
		}

		int opcion = JOptionPane.showConfirmDialog(this,
				"¿Confirmar el pago de " + String.format("%.2f", precioActual) + " € para activar Premium?",
				"Confirmar Pago", JOptionPane.YES_NO_OPTION);

		if (opcion == JOptionPane.YES_OPTION) {
			boolean exito = AppChat.getUnicaInstancia().activarPremium();

			if (exito) {
				JOptionPane.showMessageDialog(this, "¡Felicidades! Ahora eres un usuario Premium.", "Premium Activado",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Ha ocurrido un error al activar Premium. Inténtalo más tarde.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}