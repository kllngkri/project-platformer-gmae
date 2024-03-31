package main;


import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game implements Runnable{
    private  GameWindow gameWindow;
    private  GamePanel gamePanel;
    Thread gameThread;
    private GameOverlay gameOverlay;
    private  final  int FPS_SET = 120;
    private final  int UPS_SET = 120;
    private Player player;
    private EnemyManager enemyManager;

    private LevelManager levelManager;

    private int xLvOffset;

    //ขอบซ้าย = 20%;
    private int leftBorder = (int) (0.2*Game.GAME_WIDTH);
    //ขอบขวา = 80%;
    private int rightBorder = (int)(0.8 *GAME_WIDTH);
    private int lvTileWidth = LoadSave.GetLevelDate()[0].length;
    private int maxTileOffset = lvTileWidth-Game.TILES_IN_WIDTH;
    private int maxLvOffsetX = maxTileOffset*Game.TILES_SIZE;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE*SCALE);
    public final static int GAME_WIDTH = TILES_SIZE*TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE*TILES_IN_HEIGHT;
    private BufferedImage backgroundIMG_1;
    private BufferedImage backgroundIMG_2;
    private BufferedImage backgroundIMG_3;
    private BufferedImage rock_1;
    private int[] rock1Pos;
    private Random rnd = new Random();
    private boolean gameOver;


    public Game(){
        initClass();
        rock_1 = LoadSave.GetSpriteAtlas(LoadSave.ROCK_1);
        backgroundIMG_1 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG_1);
        backgroundIMG_2 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG_2);
        backgroundIMG_3 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG_3);
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        rock1Pos = new int[20];
        setGroundEV();
        startGameLoop();

    }

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox);
    }

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }


    private void setGroundEV() {
        for (int i = 0;i < rock1Pos.length;i++){
            int value = (int)(90*Game.SCALE) + rnd.nextInt((int)(2100*Game.SCALE));

            if (value >= 650 && value <= 1000){
                i--;
                continue;
            } else rock1Pos[i] = value;

        }
    }


    private void initClass() {
        levelManager = new LevelManager(this);
        enemyManager = new EnemyManager(this);
        player = new Player(200,200,(int) (64 * SCALE),(int) (40 * SCALE),this);
        player.loadLvData(levelManager.getCurrentLevel().getLevelData());
        gameOverlay = new GameOverlay(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {
        double timePerFrame = Math.pow(10, 9) / FPS_SET;
        double timePerUpdate = Math.pow(10, 9) / UPS_SET;

        long previousTime = System.nanoTime();

        int frameRate = 0;
        int update = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();


            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                updates();
                update++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                deltaF--;
                frameRate++;
            }


            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                frameRate = 0;
                update = 0;
            }

        }

    }

    public void updates() {
        enemyManager.update(levelManager.getCurrentLevel().getLevelData(),player);
        player.update();
        levelManager.update();
        checkCloseToBorder();
    }

    private void checkCloseToBorder() {
        int playerX = (int)player.getHitBox().x;
        int diff = playerX - xLvOffset;

        if (diff > rightBorder)
            xLvOffset += diff - rightBorder;
        else if (diff < leftBorder) {
            xLvOffset += diff - leftBorder;
        }

        if (xLvOffset > maxLvOffsetX)
            xLvOffset = maxLvOffsetX;
        else if (xLvOffset < 0) {
            xLvOffset = 0;
        }
    }

    public void render(Graphics g){
        drawBG(g);
        enemyManager.draw(g,xLvOffset);
        levelManager.draw(g,xLvOffset);
        player.render(g,xLvOffset);
        if (gameOver){
            gameOverlay.draw(g);
        }


    }

    public void resetAll() {
        gameOver = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
    }

    public void drawBG(Graphics g){
        g.drawImage(backgroundIMG_1,0,0,GAME_WIDTH,GAME_HEIGHT,null);
        g.drawImage(backgroundIMG_2,0,0,GAME_WIDTH,GAME_HEIGHT,null);
        g.drawImage(backgroundIMG_3,0,0,GAME_WIDTH,GAME_HEIGHT,null);
        drawGroundEV(g);


    }

    private void drawGroundEV(Graphics g) {
        for (int i = 0;i < rock1Pos.length;i++){
            int value = rock1Pos[i] - (int)(xLvOffset*0.01);

            if (value >= 650 && value <= 1000){
                i--;
                continue;
            } else  g.drawImage(rock_1,rock1Pos[i] - (int)(xLvOffset*0.9), 464, Constants.Environment.ROCK_1.getWidthSize(), Constants.Environment.ROCK_1.getHeightSize(), null);

        }
    }


    public Player getPlayer() {
        return player;
    }

    public void windowFocusLose() {
        player.serDirBoolean();

    }



}
