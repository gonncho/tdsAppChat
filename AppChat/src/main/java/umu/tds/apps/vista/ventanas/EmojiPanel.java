package umu.tds.apps.vista.ventanas;

import javax.swing.*;
import tds.BubbleText;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.EtchedBorder;


public class EmojiPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public EmojiPanel(ActionListener emojiClickListener) {
        setLayout(new GridLayout(5,3,0,0)); 
        setBackground(new Color(0, 128, 128));
        setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        setSize(265, 250); 
        setVisible(false); 

        for (int i = 0; i < 12; i++) {
            JLabel emoji = new JLabel();
            emoji.setIcon(BubbleText.getEmoji(i)); 
            emoji.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
            
            final int emojiIndex = i; 
            
            emoji.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    emojiClickListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(emojiIndex)));
                    setVisible(false);
                }
            });
            
            add(emoji);
        }
    }
}
