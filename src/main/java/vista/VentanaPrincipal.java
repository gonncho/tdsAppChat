package vista;

import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Container;

import javax.swing.Box;
import java.awt.Dimension;
import java.awt.ScrollPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.JTextField;
import tds.BubbleText;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldMensaje;
	private JPanel panelConversacion = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    // 1) Desactiva el zoom HiDPI antes de crear cualquier ventana
	    BubbleText.noZoom();

	    // 2) Lanza la UI en el Event Dispatch Thread
	    SwingUtilities.invokeLater(() -> {
	        try {
	            VentanaPrincipal frame = new VentanaPrincipal();
	            frame.setVisible(true);

	            // 3) Simula la llegada de un mensaje de “Alice” tras 2000 ms
	            Timer timer = new Timer(2_000, evt -> {
	                frame.addReceivedBubble("Alice", "¡Hola! ¿Cómo estás?");
	            });
	            timer.setRepeats(false);  // solo una vez
	            timer.start();
	            
	            // 4) Simula llegada de un emoji 1s despues del mensaje
	            Timer emojiTimer = new Timer(3_000, evt -> {
	                frame.addReceivedEmoji("Alice", 2);
	            });
	            emojiTimer.setRepeats(false);  // solo una vez
	            emojiTimer.start();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });
	}


	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JComboBox selectorContacto = new JComboBox();
		panel.add(selectorContacto);
		
		JButton btnEnviarContacto = new JButton("");
		panel.add(btnEnviarContacto);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panel.add(rigidArea);
		
		JButton btnBuscar = new JButton("");
		panel.add(btnBuscar);
		
		JButton btnContactos = new JButton("Contactos");
		panel.add(btnContactos);
		
		JButton btnPremium = new JButton("Premium");
		panel.add(btnPremium);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		panel.add(rigidArea_1);
		
		JLabel lblNombreUser = new JLabel("Usuario");
		panel.add(lblNombreUser);
		
		JLabel lblFotoPerfil = new JLabel("foto");
		panel.add(lblFotoPerfil);
		
		JPanel panelMensajes = new JPanel();
		contentPane.add(panelMensajes, BorderLayout.WEST);
		panelMensajes.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panelMensajes.add(scrollPane_1);
		
		JList list = new JList();
		scrollPane_1.setViewportView(list);
		
		JPanel panelChat = new JPanel();
		contentPane.add(panelChat, BorderLayout.CENTER);
		panelChat.setLayout(new BorderLayout(0, 0));
		
		panelConversacion.setLayout(new BoxLayout(panelConversacion, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(panelConversacion,
		        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelChat.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panelEnviarMensaje = new JPanel();
		panelChat.add(panelEnviarMensaje, BorderLayout.SOUTH);
		
		textFieldMensaje = new JTextField();
		textFieldMensaje.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String msg = textFieldMensaje.getText().trim();
					if (msg.isEmpty()) {
						return;
					}
					
					addSentBubble(msg);
				} 
			}
		});
		panelEnviarMensaje.add(textFieldMensaje);
		textFieldMensaje.setColumns(10);
		
		JButton btnEnviarMensaje = new JButton("");
		btnEnviarMensaje.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String msg = textFieldMensaje.getText().trim();
				if (msg.isEmpty()) {
					return;
				}
				
				addSentBubble(msg);
			}
		});
		
		JPopupMenu emojiMenu = new JPopupMenu();
		// Por ejemplo, los ocho primeros emoticonos
		for (int i = 0; i < 8; i++) {
		    ImageIcon icon = BubbleText.getEmoji(i);
		    JMenuItem item = new JMenuItem(icon);
		    final int idx = i;
		    item.addActionListener(e -> {
		    	 addSentEmoji(idx);
		    });
		    emojiMenu.add(item);
		}
		
		JButton btnEmojis = new JButton("😃");
		btnEmojis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Abre menu 
				emojiMenu.show(btnEmojis, 0, btnEmojis.getHeight());
			}
		});
		panelEnviarMensaje.add(btnEmojis);
		panelEnviarMensaje.add(btnEnviarMensaje);
		
	}
	
	private void scrollToBottom(JPanel chatPanel) {
	    Container parent = chatPanel.getParent(); 
	    if (parent instanceof JViewport) {
	        JViewport vp = (JViewport) parent;
	        Rectangle r = chatPanel.getBounds();
	        vp.scrollRectToVisible(
	            new Rectangle(0, r.height - 1, 1, 1)
	        );
	    }
	}
	
	/**
	 * Añade al panel una burbuja de mensaje enviado.
	 */
	private void addSentBubble(String texto) {
		//Creamos BubleText con texto
		BubbleText burbuja = new BubbleText(panelConversacion, texto, Color.GREEN, "yo",BubbleText.SENT);
		
		JPanel wrapper = new JPanel(new BorderLayout());
	    wrapper.setOpaque(false); 					// transparente
	    wrapper.add(burbuja, BorderLayout.EAST);
	    wrapper.setMaximumSize(new Dimension(
	    	    Integer.MAX_VALUE,
	    	    wrapper.getPreferredSize().height
	    ));
	    
	    //Añadir al panelConversacion
	    panelConversacion.add(wrapper);
		panelConversacion.revalidate();   			// recalcula el layout
		panelConversacion.repaint();      			// repinta el panel

		//Scroll automático al final
        SwingUtilities.invokeLater(() ->
            scrollToBottom(panelConversacion)
        );
		
        //Limpiar el campo de texto
        textFieldMensaje.setText("");
	}

	/**
	 * Añade al panel una burbuja de mensaje recibido.
	 */
	public void addReceivedBubble(String autor, String texto) {
		//Creamos BubleText con texto
		BubbleText burbuja = new BubbleText(panelConversacion, texto, Color.LIGHT_GRAY, autor,BubbleText.RECEIVED);
		
		JPanel wrapper = new JPanel(new BorderLayout());
	    wrapper.setOpaque(false); 					// transparente
	    wrapper.add(burbuja, BorderLayout.WEST);
	    wrapper.setMaximumSize(new Dimension(
	    	    Integer.MAX_VALUE,
	    	    wrapper.getPreferredSize().height
	    ));
	    
	    //Añadir al panelConversacion
	    panelConversacion.add(wrapper);
		panelConversacion.revalidate();   			// recalcula el layout
		panelConversacion.repaint();      			// repinta el panel

		//Scroll automático al final
        SwingUtilities.invokeLater(() ->
            scrollToBottom(panelConversacion)
        );
	}
	
	private void addSentEmoji(int emojiIndex) {
		  BubbleText burbuja = new BubbleText(
			panelConversacion,
			emojiIndex,      		// emoticono en lugar de texto
		    Color.GREEN,
		    "yo",
		    BubbleText.SENT,
		    24               		// tamaño
		  );
		  JPanel wrapper = new JPanel(new BorderLayout());
		  wrapper.setOpaque(false);
		  wrapper.add(burbuja, BorderLayout.EAST);
		  wrapper.setMaximumSize(new Dimension(
				  Integer.MAX_VALUE,
				  wrapper.getPreferredSize().height
		  ));

		  //Añadir al panelConversacion
		  panelConversacion.add(wrapper);
		  panelConversacion.revalidate();   			// recalcula el layout
		  panelConversacion.repaint();      			// repinta el panel
		  
		  //Scroll automático al final
	      SwingUtilities.invokeLater(() ->
	            scrollToBottom(panelConversacion)
	      );
	}
	
	private void addReceivedEmoji(String autor, int emojiIndex) {
		  BubbleText burbuja = new BubbleText(
			panelConversacion,
			emojiIndex,      		// emoticono en lugar de texto
		    Color.LIGHT_GRAY,
		    autor,
		    BubbleText.RECEIVED,
		    24               		// tamaño
		  );
		  JPanel wrapper = new JPanel(new BorderLayout());
		  wrapper.setOpaque(false);
		  wrapper.add(burbuja, BorderLayout.WEST);
		  wrapper.setMaximumSize(new Dimension(
				  Integer.MAX_VALUE,
				  wrapper.getPreferredSize().height
		  ));

		  //Añadir al panelConversacion
		  panelConversacion.add(wrapper);
		  panelConversacion.revalidate();   			// recalcula el layout
		  panelConversacion.repaint();      			// repinta el panel
		  
		  //Scroll automático al final
	      SwingUtilities.invokeLater(() ->
	            scrollToBottom(panelConversacion)
	      );
	}

}
