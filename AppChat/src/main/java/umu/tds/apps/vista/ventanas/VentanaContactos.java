package umu.tds.apps.vista.ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.border.LineBorder;

import umu.tds.apps.controlador.AppChat;
import umu.tds.apps.modelo.Contacto;
import umu.tds.apps.modelo.ContactoIndividual;
import umu.tds.apps.modelo.Grupo;
import umu.tds.apps.modelo.RepositorioUsuarios;
import umu.tds.apps.modelo.Usuario;
import umu.tds.apps.persistencia.adaptador.AdaptadorContactoIndividualTDS;
import umu.tds.apps.persistencia.adaptador.AdaptadorUsuarioTDS;

import javax.swing.border.EmptyBorder;


public class VentanaContactos extends JPanel {

	private static final long serialVersionUID = 1L;

	private JList<String> listaContactos;
    private JList<String> listaGrupos;
    private DefaultListModel<String> modeloContactos;
    private DefaultListModel<String> modeloGrupos;
    private JButton btnAgregarContacto;
    private JButton btnAgregarGrupo;
    private JButton btnMoverDerecha;
    private JButton btnMoverIzquierda;

    public VentanaContactos() {
        Usuario usuarioActual = AppChat.getUsuarioActual();

        List<Contacto> contactos = usuarioActual.getListaContactos();

        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(192, 192, 192));
        setLayout(new GridBagLayout());

        JPanel panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBackground(new Color(240, 240, 240));
        modeloContactos = new DefaultListModel<>();
        contactos.forEach(contacto -> modeloContactos.addElement(contacto.getNombre()));
        listaContactos = new JList<>(modeloContactos);
        listaContactos.setBackground(new Color(255, 225, 225));
        listaContactos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPaneContactos = new JScrollPane(listaContactos);
        scrollPaneContactos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneContactos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelContactos.add(scrollPaneContactos, BorderLayout.CENTER);

        btnAgregarContacto = new JButton("Añadir Contacto");
        btnAgregarContacto.setForeground(new Color(255, 255, 255));
        btnAgregarContacto.setBorder(new LineBorder(new Color(128, 128, 150), 2));
        btnAgregarContacto.setBackground(new Color(255, 102, 102));
        panelContactos.add(btnAgregarContacto, BorderLayout.SOUTH);

        JPanel panelGrupos = new JPanel(new BorderLayout());
        panelGrupos.setBackground(new Color(240, 240, 240));
        modeloGrupos = new DefaultListModel<>();
        listaGrupos = new JList<>(modeloGrupos);
        listaGrupos.setBackground(new Color(255, 225, 225));
        listaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPaneGrupos = new JScrollPane(listaGrupos);
        scrollPaneGrupos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneGrupos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelGrupos.add(scrollPaneGrupos, BorderLayout.CENTER);

        btnAgregarGrupo = new JButton("Añadir Grupo");
        btnAgregarGrupo.setBorder(new LineBorder(new Color(143, 239, 208), 2));
        btnAgregarGrupo.setForeground(new Color(255, 255, 255));
        btnAgregarGrupo.setBackground(new Color(255, 102, 102));
        panelGrupos.add(btnAgregarGrupo, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 5));
        panelBotones.setBackground(new Color(240, 240, 240));
        btnMoverDerecha = new JButton(">>");
        btnMoverDerecha.setForeground(new Color(255, 255, 255));
        btnMoverDerecha.setFont(new Font("Arial", Font.BOLD, 11));
        btnMoverDerecha.setBackground(new Color(255, 102, 102));
        btnMoverIzquierda = new JButton("<<");
        btnMoverIzquierda.setForeground(new Color(255, 255, 255));
        btnMoverIzquierda.setFont(new Font("Arial", Font.BOLD, 11));
        btnMoverIzquierda.setBackground(new Color(255, 102, 102));
        panelBotones.add(btnMoverDerecha);
        panelBotones.add(btnMoverIzquierda);

        GridBagConstraints gbcContactos = new GridBagConstraints();
        gbcContactos.gridx = 0;
        gbcContactos.gridy = 0;
        gbcContactos.gridheight = 2;
        gbcContactos.fill = GridBagConstraints.BOTH;
        gbcContactos.weightx = 0.5;
        gbcContactos.weighty = 1.0;
        add(panelContactos, gbcContactos);

        GridBagConstraints gbcBotones = new GridBagConstraints();
        gbcBotones.insets = new Insets(5, 5, 5, 5);
        gbcBotones.gridx = 1;
        gbcBotones.gridy = 0;
        gbcBotones.gridheight = 2;
        gbcBotones.fill = GridBagConstraints.NONE;
        gbcBotones.weightx = 0;
        gbcBotones.weighty = 0;
        add(panelBotones, gbcBotones);

        GridBagConstraints gbcGrupos = new GridBagConstraints();
        gbcGrupos.gridx = 2;
        gbcGrupos.gridy = 0;
        gbcGrupos.gridheight = 2;
        gbcGrupos.fill = GridBagConstraints.BOTH;
        gbcGrupos.weightx = 0.5;
        gbcGrupos.weighty = 1.0;
        add(panelGrupos, gbcGrupos);


        btnAgregarContacto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoContactoNombre = JOptionPane.showInputDialog("Introduce el nombre del nuevo contacto:");
                if (nuevoContactoNombre == null || nuevoContactoNombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El nombre del contacto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String nuevoContactoTelefono = JOptionPane.showInputDialog("Introduce el número del nuevo contacto:");
                if (nuevoContactoTelefono == null || nuevoContactoTelefono.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El número del contacto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Usuario usuarioActual = AppChat.getUsuarioActual();
                
                if (usuarioActual.getTelefono().equals(nuevoContactoTelefono)) {
                    JOptionPane.showMessageDialog(null, "No puedes añadirte a ti mismo como contacto.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean contactoExistente = usuarioActual.getListaContactos().stream()
                        .filter(c -> c instanceof ContactoIndividual)
                        .map(c -> (ContactoIndividual) c)
                        .anyMatch(c -> c.getTelefono().equals(nuevoContactoTelefono));
                        
                if (contactoExistente) {
                    JOptionPane.showMessageDialog(null, "Ya tienes un contacto con este número.", 
                        "Contacto duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Usuario usuarioExistente = RepositorioUsuarios.getUnicaInstancia().getUsuario(nuevoContactoTelefono);
                if (usuarioExistente == null) {
                    JOptionPane.showMessageDialog(null, "El número introducido no pertenece a ningún usuario registrado.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (usuarioExistente.getCodigo() <= 0) {
                    AdaptadorUsuarioTDS.getUnicaInstancia().registrarUsuario(usuarioExistente);
                }

                ContactoIndividual nuevoContacto = new ContactoIndividual(nuevoContactoNombre, nuevoContactoTelefono, usuarioExistente);
                try {
                    AdaptadorContactoIndividualTDS.getUnicaInstancia().registrarContactoIndividual(nuevoContacto);

                    if (usuarioActual.añadirContacto(nuevoContacto)) {
                        AdaptadorUsuarioTDS.getUnicaInstancia().modificarUsuario(usuarioActual);
                        modeloContactos.addElement(nuevoContactoNombre);
                        JOptionPane.showMessageDialog(null, "Contacto añadido correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo añadir el contacto.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al añadir el contacto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        btnAgregarGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nombreGrupoField = new JTextField();
                JButton seleccionarImagenBtn = new JButton("Seleccionar Imagen");
                JLabel imagenSeleccionadaLabel = new JLabel("No se ha seleccionado ninguna imagen");
                final ImageIcon[] imagenIcono = {null};

                seleccionarImagenBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
                        int returnValue = fileChooser.showOpenDialog(null);
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            String rutaImagen = fileChooser.getSelectedFile().getAbsolutePath();
                            imagenIcono[0] = new ImageIcon(rutaImagen);
                            imagenSeleccionadaLabel.setText("Imagen seleccionada: " + fileChooser.getSelectedFile().getName());
                        }
                    }
                });

                final String[] nombreGrupo = {null}; 
                boolean nombreValido = false;

                do {
                    Object[] message = {
                        "Nombre del grupo:", nombreGrupoField,
                        "Imagen del grupo:", seleccionarImagenBtn,
                        imagenSeleccionadaLabel
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "Crear Grupo", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                        return; 
                    }

                    nombreGrupo[0] = nombreGrupoField.getText().trim();

                    if (nombreGrupo[0].isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El nombre del grupo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    boolean nombreDuplicado = AppChat.getUnicaInstancia().existeGrupoConNombre(nombreGrupo[0]);

                    if (nombreDuplicado) {
                        int respuesta = JOptionPane.showConfirmDialog(null, 
                                "Ya existe un grupo con este nombre. ¿Desea añadir contactos a este grupo existente?", 
                                "Grupo existente", 
                                JOptionPane.YES_NO_OPTION);
                        
                        if (respuesta == JOptionPane.NO_OPTION) {
                            continue; 
                        }
                    }

                    nombreValido = true;
                } while (!nombreValido);

                List<String> nombresSeleccionados = new ArrayList<>();
                for (int i = 0; i < modeloGrupos.size(); i++) {
                    nombresSeleccionados.add(modeloGrupos.getElementAt(i));
                }

                if (nombresSeleccionados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe haber al menos un contacto en el grupo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<ContactoIndividual> contactosSeleccionados = 
                        AppChat.getUnicaInstancia().obtenerContactosPorNombres(nombresSeleccionados);
                
                if (contactosSeleccionados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No se encontraron contactos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Grupo grupoExistente = AppChat.getUnicaInstancia().obtenerGrupoPorNombre(nombreGrupo[0]);

                boolean resultado;
                
                if (grupoExistente != null) {
                    resultado = AppChat.getUnicaInstancia().agregarContactosAGrupo(grupoExistente, contactosSeleccionados);
                    if (resultado) {
                        JOptionPane.showMessageDialog(null, "Se han añadido nuevos contactos al grupo existente.", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se añadió ningún contacto nuevo al grupo.", 
                                "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    try {
                        AppChat.getUnicaInstancia().crearGrupo(nombreGrupo[0], contactosSeleccionados, 
                                Optional.ofNullable(imagenIcono[0]));
                        JOptionPane.showMessageDialog(null, "Grupo creado con éxito.", 
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                modeloGrupos.clear();
            }
        });


        btnMoverDerecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> contactosSeleccionados = listaContactos.getSelectedValuesList();
                for (String contacto : contactosSeleccionados) {
                    if (!modeloGrupos.contains(contacto)) {
                        modeloGrupos.addElement(contacto); 
                    }
                }
            }
        });


        btnMoverIzquierda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contactoSeleccionado = listaGrupos.getSelectedValue();
                if (contactoSeleccionado != null) {
                    if (!modeloContactos.contains(contactoSeleccionado)) {
                        modeloContactos.addElement(contactoSeleccionado);
                    }
                    modeloGrupos.removeElement(contactoSeleccionado);
                }
            }
        });

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame frame = new JFrame("Ventana de Contactos");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(600, 400);

                    VentanaContactos panelContactos = new VentanaContactos();
                    frame.getContentPane().add(panelContactos);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
