package umu.tds.apps.utils;


import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class IconUtil {
	
	// Crea un icono circular a partir de una imagen original.
    public static ImageIcon createCircularIcon(BufferedImage original, int diameter) {
        if (original == null) {
            return new ImageIcon(); 
        }

        int size = Math.min(original.getWidth(), original.getHeight());

        int x = (original.getWidth()  - size) / 2;
        int y = (original.getHeight() - size) / 2;

        BufferedImage squareSubimage = original.getSubimage(x, y, size, size);

        BufferedImage circleBuffer = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));

            g2.drawImage(squareSubimage, 0, 0, diameter, diameter, null);

        } finally {
            g2.dispose();
        }

        return new ImageIcon(circleBuffer);
    }

}
