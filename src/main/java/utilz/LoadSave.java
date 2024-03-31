package utilz;

import entities.Cucumber;
import main.Game;
import utilz.Constants.EnemyConstant;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


public class LoadSave {

    public final static String PLAYER_ATLAS = "KING_HUMAN(78x58).png";
    public final static String LEVEL_ATLAS = "outside_sprites.png";
    public final static String LEVEL_ONE_DATA = "3.png";
    public final static String PLAYING_BG_IMG_1 = "background_layer_1.png";
    public final static String PLAYING_BG_IMG_2 = "background_layer_2.png";
    public final static String PLAYING_BG_IMG_3 = "background_layer_3.png";
    public final static String ROCK_1 = "rock_1.png";
    public final static String CUCUMBER_IDLE = "res/cucumber/idle/";
    public final static String CUCUMBER_RUNNING = "res/cucumber/running/";
    public final static String CUCUMBER_ATTACK = "res/3-Enemy-Cucumber/7-Attack/";
    public final static String CUCUMBER_HIT = "res/3-Enemy-Cucumber/9-Hit/";
    public final static String CUCUMBER_DEAD_HIT = "res/3-Enemy-Cucumber/10-Dead Hit/";
    public final static String HEALTH_POWER_BAR = "health_power_bar.png";


    public static BufferedImage GetSpriteAtlas(String fileName){

        BufferedImage animation = null;

        File file = new File("res/"+fileName);

        try {
            animation = ImageIO.read(file);

        }catch (Exception e){
            e.getStackTrace();
        }
        return animation;
    }









    public static ArrayList<Cucumber> GetCucumbers(){
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Cucumber> list = new ArrayList<>();

        for (int j = 0;j < img.getHeight();j++)
            for (int i = 0;i < img.getWidth();i++){

                Color color = new Color(img.getRGB(i,j));
                int value = color.getGreen();
                if (value == EnemyConstant.EnemyState.getCucumber())
                    list.add(new Cucumber(i* Game.TILES_SIZE,j*Game.TILES_SIZE));

            }
        return list;
    }


    public static BufferedImage[] getCucumbersState(int nSprite,String prefixGameState){
        BufferedImage[] animation = new BufferedImage[36];
        try {
            for (int i = 0 ;i < nSprite;i++){
                String index = Integer.toString(i+1);

                File file = new File(prefixGameState + index + ".png");
                animation[i] = ImageIO.read(file);
            }

        }catch (Exception e){
            e.getStackTrace();
        }
        return animation;
    }



    public static int[][] GetLevelDate(){
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);

        //+1
        int [][] lvData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0;j < img.getHeight();j++)
            for (int i = 0;i < img.getWidth();i++){


                Color color = new Color(img.getRGB(i,j));
                int value = color.getRed();
                if (value >= 32)
                    value = 0;

                lvData[j][i] = value;
            }

        return lvData;
    }




}
