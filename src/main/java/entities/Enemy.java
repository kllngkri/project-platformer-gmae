package entities;
import main.Game;
import utilz.Constants;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.*;



public abstract class Enemy extends Entity{
    protected int aniIndex;
    protected  int aniTick,aniSpeed = 10;
    protected  boolean firstUpdate = true;
    protected  boolean inAir = false;
    protected  float fallSpeed;
    protected  float gravity = 0.014f * Game.SCALE;
    protected  int enemyAction;
    protected  float walkSpeed = 1.0f * Game.SCALE;
    protected  int walkDir = LEFT;
    protected int tileY;
    protected int attackDistance = Game.TILES_SIZE;
    protected final int IDLE = 0,RUNNING = 1,ATTACK = 2,HIT = 3,DEAD_HIT = 4;
    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true;
    protected boolean attackCheck;

    public Enemy(float x, float y, int width, int height) {
        super(x, y, width, height);
        initHitBox(x,y,width,height);
        maxHealth = Constants.EnemyConstant.EnemyState.getCucumberMaxHealth();
        currentHealth = maxHealth;
    }

    protected void move(int[][] lvData){
        float xSpeed = 0;
        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitBox.x + xSpeed,hitBox.y, hitBox.width, hitBox.height,lvData))
            if (IsFloor(hitBox,xSpeed,lvData)){
                hitBox.x += xSpeed;
                return;
            }
        changeWalkDir();
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitBox.x > hitBox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected boolean canSeePlayer(int[][] lvlData,Player player) {
        int playerTileY = (int) (player.getHitBox().y / Game.TILES_SIZE);
        if (playerTileY == tileY)
            if (isPlayerInRange(player)) {
                if (IsSightClear(lvlData, hitBox, player.hitBox, tileY))
                    return true;
            }

        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance;
    }



    protected void newState(int enemyState,Constants.EnemyConstant.EnemyState enemyAction) {
        setEnemyType(enemyAction);
        this.enemyAction = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEAD_HIT, Constants.EnemyConstant.EnemyState.DEAD_HIT_CUCUMBER);
        else
            newState(HIT, Constants.EnemyConstant.EnemyState.HIT_CUCUMBER);
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitBox))
            player.changeHealth(-Constants.EnemyConstant.EnemyState.getCucumberDamage());
        attackCheck = true;
    }


    protected void updateInAir(int [][] lvData){
        if (CanMoveHere(hitBox.x,hitBox.y + fallSpeed, hitBox.width, hitBox.height, lvData)){
            hitBox.y += fallSpeed;
            fallSpeed += gravity;
        }else {
            inAir = false;
            hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox,fallSpeed);
            tileY = (int)(hitBox.y/Game.TILES_SIZE);
        }
    }

    protected void firstUpdateCheck(int[][] lvData){
        if (!IsEntityOnFloor(hitBox,lvData)){
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void updateAnimationTick(){
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount()) {
                aniIndex = 0;

                switch (enemyAction){
                    case ATTACK,HIT -> enemyAction = IDLE;
                    case DEAD_HIT -> active = false;
                }



            }
        }
    }

    abstract int getSpriteAmount();




    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public boolean isActive(){
        return active;
    }


    public int getAniIndex(){
        return aniIndex;
    }

    public void resetEnemy() {
        hitBox.x = x;
        hitBox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE, Constants.EnemyConstant.EnemyState.IDLE_CUCUMBER);
        active = true;
        fallSpeed = 0;

    }

    public abstract int getEnemyState();

    public abstract void setEnemyType(Constants.EnemyConstant.EnemyState state);


}
