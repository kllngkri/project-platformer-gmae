package entities;

import main.Game;
import utilz.Constants.EnemyConstant;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.RIGHT;


public class Cucumber extends Enemy{
    private EnemyConstant.EnemyState enemyState ;
    private int width = EnemyConstant.EnemyState.getWidth();
    private int height = EnemyConstant.EnemyState.getHeight();

    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Cucumber(float x, float y) {
        super(x, y, EnemyConstant.EnemyState.getWidth(), EnemyConstant.EnemyState.getHeight());
        initHitBox(x,y,(int) Game.SCALE*22,(int) Game.SCALE*19);
        enemyState = EnemyConstant.EnemyState.IDLE_CUCUMBER;
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (40 * Game.SCALE), (int) (20* Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 5);
    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x - attackBoxOffsetX;
        attackBox.y = hitBox.y;

    }


    public void update(int[][] lvData,Player player){
        updateBehavior(lvData,player);
        updateAnimationTick();
        updateAttackBox();
    }



    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public void updateBehavior(int[][] lvData, Player player){


        if (firstUpdate){
            firstUpdateCheck(lvData);
        }

        if (inAir){
            updateInAir(lvData);
        }else {

            switch (getEnemyState()){
                case IDLE :
                    newState(RUNNING,EnemyConstant.EnemyState.RUNNING_CUCUMBER);
                    break;
                case RUNNING :
                    if (canSeePlayer(lvData, player))
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK, EnemyConstant.EnemyState.ATTACK_CUCUMBER);

                   move(lvData);
                    break;
                case ATTACK :
                    if (aniIndex == 0)
                        attackCheck = false;

                    if (aniIndex == 6  && !attackCheck)
                        checkPlayerHit(attackBox,player);
                    break;
                case DEAD_HIT :
                    break;
            }
        }
    }




    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;

    }



    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    int getSpriteAmount() {
        return enemyState.getSpriteN();
    }

    @Override
    public int getEnemyState() {
        return enemyAction;
    }


    @Override
    public void setEnemyType(EnemyConstant.EnemyState state) {
        enemyState = state;
    }



}
