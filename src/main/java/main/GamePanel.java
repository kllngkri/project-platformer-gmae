package main;

import inputs.KeyboardInputs;


import javax.swing.*;
import java.awt.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {

    private Game game;

    public GamePanel(Game game){
        this.game = game;
        addKeyListener(new KeyboardInputs(this));
        gameSetSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }



    private  void gameSetSize() {
        Dimension gameSize = new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(gameSize);


    }

    public Game getGame() {
        return game;
    }
}
