package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Ventana para buscar mensajes.
 */
public class VentanaBusqueda {
    private final JFrame frame;
    private final JTextField txtTexto;
    private final JTextField txtNombre;
    private final JTextField txtTelefono;
    private final JButton btnBuscar;
    private final JButton btnCerrar;
    private final DefaultListModel<String> modeloResultados;
    private final JList<String> listaResultados;

    public VentanaBusqueda() {
        frame = new JFrame("Buscar mensajes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JPanel filtros = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx=0; c.gridy=0; filtros.add(new JLabel("Texto:"), c);
        c.gridx=1; txtTexto = new JTextField(15); filtros.add(txtTexto, c);
        c.gridx=0; c.gridy=1; filtros.add(new JLabel("Nombre:"), c);
        c.gridx=1; txtNombre = new JTextField(15); filtros.add(txtNombre, c);
        c.gridx=0; c.gridy=2; filtros.add(new JLabel("Teléfono:"), c);
        c.gridx=1; txtTelefono = new JTextField(15); filtros.add(txtTelefono, c);

        frame.add(filtros, BorderLayout.NORTH);

        modeloResultados = new DefaultListModel<>();
        listaResultados = new JList<>(modeloResultados);
        frame.add(new JScrollPane(listaResultados), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        btnBuscar = new JButton("Buscar");
        btnCerrar = new JButton("Cerrar");
        botones.add(btnBuscar);
        botones.add(btnCerrar);
        frame.add(botones, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public String getTexto() { return txtTexto.getText().trim(); }
    public String getNombre() { return txtNombre.getText().trim(); }
    public String getTelefono() { return txtTelefono.getText().trim(); }

    public void setResultados(List<String> resultados) {
        modeloResultados.clear();
        resultados.forEach(modeloResultados::addElement);
    }

    public void addSearchListener(ActionListener l) { btnBuscar.addActionListener(l); }
    public void addCloseListener(ActionListener l) { btnCerrar.addActionListener(l); }

    public void mostrar() { frame.setVisible(true); }
    public void ocultar() { frame.setVisible(false); }
}