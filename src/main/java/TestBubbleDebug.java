
import javax.swing.*;
import java.awt.*;
import tds.BubbleText;

public class TestBubbleDebug {
    public static void main(String[] args) {
        // 1) Atrapa excepciones silenciadas
        Thread.setDefaultUncaughtExceptionHandler((t,e)-> e.printStackTrace());
        // 2) Comprueba la librería
        System.out.println("Versión BubbleText: " + BubbleText.getVersion());

        SwingUtilities.invokeLater(() -> {
            try {
                // Crear ventana y panel
                JFrame frame = new JFrame("Debug Bubble");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(new BorderLayout());

                JPanel chat = new JPanel();
                chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
                chat.setBackground(Color.WHITE);
                chat.setOpaque(true);

                // Meter el chat en un scroll
                JScrollPane scroll = new JScrollPane(chat);
                scroll.setPreferredSize(new Dimension(300,300));
                frame.getContentPane().add(scroll, BorderLayout.CENTER);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // 3) Imprime tamaños tras mostrar
                System.out.println("Después de setVisible → chat.size=" 
                    + chat.getSize() 
                    + ", preferred=" + chat.getPreferredSize());

                // 4) Retrasa la creación de la burbuja hasta tener ancho
                SwingUtilities.invokeLater(() -> {
                    System.out.println("Justo antes de crear BubbleText → chat.width=" 
                        + chat.getWidth());
                    // Intenta añadir la burbuja
                    BubbleText b = new BubbleText(chat,
                        "¡Hola mundo debug!",
                        Color.MAGENTA,
                        "Tester",
                        BubbleText.SENT
                    );
                    chat.add(b);
                    chat.revalidate();
                    chat.repaint();
                    System.out.println("Burbujas añadidas OK.");
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
