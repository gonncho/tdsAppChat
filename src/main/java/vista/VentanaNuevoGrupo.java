package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Ventana para crear un grupo a partir de contactos existentes.
 */
public class VentanaNuevoGrupo {
    private final JFrame frame;
    private final JTextField txtNombre;
    private final JList<String> listaContactos;
    private final DefaultListModel<String> modelo;
    private final JButton btnAceptar;
    private final JButton btnCancelar;

    public VentanaNuevoGrupo() {
        frame = new JFrame("Nuevo grupo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx=0; c.gridy=0; form.add(new JLabel("Nombre:"), c);
        c.gridx=1; txtNombre = new JTextField(15); form.add(txtNombre, c);
        frame.add(form, BorderLayout.NORTH);

        modelo = new DefaultListModel<>();
        listaContactos = new JList<>(modelo);
        listaContactos.setVisibleRowCount(8);
        listaContactos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        frame.add(new JScrollPane(listaContactos), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        botones.add(btnAceptar);
        botones.add(btnCancelar);
        frame.add(botones, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void setContactos(List<String> nombres) {
        modelo.clear();
        nombres.forEach(modelo::addElement);
    }

    public String getNombre() { return txtNombre.getText().trim(); }
    public List<String> getSeleccionados() { return listaContactos.getSelectedValuesList(); }

    public void addAcceptListener(ActionListener l) { btnAceptar.addActionListener(l); }
    public void addCancelListener(ActionListener l) { btnCancelar.addActionListener(l); }

    public void mostrar() { frame.setVisible(true); }
    public void ocultar() { frame.setVisible(false); }
}