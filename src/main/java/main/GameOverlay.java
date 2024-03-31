package main;

import java.awt.*;


public class GameOverlay {

    private Game playing;

    public GameOverlay(Game game) {
        this.playing = game;
    }



    public void draw(Graphics g) {

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Game Over", Game.GAME_WIDTH / 2, 150);
        g.drawString("Press esc to enter Main Menu!", Game.GAME_WIDTH / 2, 300);

    }



}
