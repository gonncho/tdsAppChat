import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import tds.BubbleText;

public class ChatAppDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Si en 4K o HiDPI necesitas gestionar el zoom
            // BubbleText.noZoom();

            // 1) Ventana principal
            JFrame frame = new JFrame("Demo de Chat");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // 2) Panel de chat dentro de scroll
            JPanel chatPanel = new JPanel();
            chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
            chatPanel.setBackground(Color.WHITE);
            chatPanel.setOpaque(true);
            // tamanho “fijo” ayudará al cálculo interno
            Dimension chatSize = new Dimension(400, 500);
            chatPanel.setPreferredSize(chatSize);
            chatPanel.setMinimumSize(chatSize);
            chatPanel.setMaximumSize(chatSize);

            JScrollPane scroll = new JScrollPane(chatPanel);
            scroll.setPreferredSize(chatSize);
            frame.add(scroll, BorderLayout.CENTER);

            // 3) Zona de entrada (texto + botón)
            JPanel inputPanel = new JPanel(new BorderLayout(5,5));
            JTextField txtInput = new JTextField();
            JButton btnSend   = new JButton("Enviar");
            inputPanel.add(txtInput, BorderLayout.CENTER);
            inputPanel.add(btnSend, BorderLayout.EAST);
            inputPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            frame.add(inputPanel, BorderLayout.SOUTH);

            // 4) Acción al pulsar “Enviar”
            ActionListener sendAction = e -> {
                String text = txtInput.getText().trim();
                if (text.isEmpty()) return;
                // 4.a) Añade burbuja enviada
                BubbleText sent = new BubbleText(
                    chatPanel,
                    text,
                    new Color(135, 206, 250),
                    "Tú",
                    BubbleText.SENT
                );
                chatPanel.add(sent);
                chatPanel.add(Box.createVerticalStrut(5)); // espaciado

                // 4.b) Limpia el campo y actualiza vista
                txtInput.setText("");
                chatPanel.revalidate();
                chatPanel.repaint();

                // 4.c) Auto-scroll al final
                SwingUtilities.invokeLater(() -> {
                    JScrollBar bar = scroll.getVerticalScrollBar();
                    bar.setValue(bar.getMaximum());
                });

                // (opcional) Eco automático
                SwingUtilities.invokeLater(() -> {
                    BubbleText reply = new BubbleText(
                        chatPanel,
                        "Eco: " + text,
                        Color.LIGHT_GRAY,
                        "Bot",
                        BubbleText.RECEIVED
                    );
                    chatPanel.add(reply);
                    chatPanel.add(Box.createVerticalStrut(5));
                    chatPanel.revalidate();
                    chatPanel.repaint();
                    SwingUtilities.invokeLater(() -> {
                        JScrollBar bar2 = scroll.getVerticalScrollBar();
                        bar2.setValue(bar2.getMaximum());
                    });
                });
            };
            btnSend.addActionListener(sendAction);
            txtInput.addActionListener(sendAction);  // Enter también envía

            // 5) Mostrar ventana
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
