package entities;

import main.Game;
import utilz.Constants.EnemyConstant;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Game game;



    BufferedImage[][] cucumber;
    private ArrayList<Cucumber> cucumbers = new ArrayList<>();

    int checkWin = cucumbers.size();
    public EnemyManager(Game game){
        this.game = game;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        cucumbers = LoadSave.GetCucumbers();
    }

    public void update(int[][] lvData,Player player){
        for (Cucumber cucumber1 : cucumbers)
            if (cucumber1.isActive())
                cucumber1.update(lvData,player );
    }

    public void draw(Graphics g,int xLvOffset){
       drawCucumber(g,xLvOffset);
    }

    private void drawCucumber(Graphics g, int xLvOffset){
        for (Cucumber c : cucumbers){
            if (c.isActive()){
                g.drawImage(cucumber[c.getEnemyState()][c.getAniIndex()],
                        (int)c.getHitBox().x - xLvOffset - EnemyConstant.EnemyState.cucumber_DRAWOFFSET_X() + c.flipX(),
                        ((int)c.getHitBox().y-70) - EnemyConstant.EnemyState.cucumber_DRAWOFFSET_Y(),
                        c.getWidth()*c.flipW(),
                        c.getHeight(),null);

//                c.drawAttackBox(g,xLvOffset);
            }

        }

    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {

        for (Cucumber c : cucumbers)
            if (c.isActive())
                if (attackBox.intersects(c.getHitBox())) {
                    c.hurt(10);
                    return;
                }

    }

    private void loadEnemyImgs() {
        cucumber = new BufferedImage[5][36];
//        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CUCUMBER_ATLAS);
//        for (int j = 0;j < cucumber.length;j++)
//            for (int i = 0; i < cucumber.length;i++)
//                cucumber[j][i] = temp.getSubimage(i*EnemyConstant.Cucumber.getWidthDefault(),j*EnemyConstant.Cucumber.getHeightDefault(),EnemyConstant.Cucumber.getWidthDefault(),EnemyConstant.Cucumber.getHeightDefault());
//

        cucumber[2] = LoadSave.getCucumbersState(EnemyConstant.EnemyState.ATTACK_CUCUMBER.getSpriteN(),LoadSave.CUCUMBER_ATTACK);
        cucumber[4] = LoadSave.getCucumbersState(EnemyConstant.EnemyState.DEAD_HIT_CUCUMBER.getSpriteN(),LoadSave.CUCUMBER_DEAD_HIT);
        cucumber[3] = LoadSave.getCucumbersState(EnemyConstant.EnemyState.HIT_CUCUMBER.getSpriteN(),LoadSave.CUCUMBER_HIT);
        cucumber[0] = LoadSave.getCucumbersState(EnemyConstant.EnemyState.IDLE_CUCUMBER.getSpriteN(),LoadSave.CUCUMBER_IDLE);
        cucumber[1] = LoadSave.getCucumbersState(EnemyConstant.EnemyState.RUNNING_CUCUMBER.getSpriteN(),LoadSave.CUCUMBER_RUNNING);
    }


    public void resetAllEnemies() {
        for (Cucumber cucumber1 : cucumbers)
            cucumber1.resetEnemy();
    }


}
