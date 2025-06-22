package umu.tds.apps.vista;

import java.awt.EventQueue;

import umu.tds.apps.vista.ventanas.VentanaLogin;

public class Launcher {
	public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaLogin ventanaLogin = new VentanaLogin();
                ventanaLogin.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
