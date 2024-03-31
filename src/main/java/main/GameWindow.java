package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow extends JFrame {
    private  JFrame jFrame;
    private  GamePanel gamePanel;


    public GameWindow(GamePanel gamePanel){
        jFrame = new JFrame();
        this.gamePanel = gamePanel;
        jFrame.add(gamePanel);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setFocusable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLose();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        });
//        jFrame.setFocusable(true);
    }

}
