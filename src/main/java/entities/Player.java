package entities;

import main.Game;
import utilz.Constants;
import utilz.HelpMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerState.*;
import static utilz.HelpMethods.*;
import static utilz.HelpMethods.CanMoveHere;

public class Player extends Entity{

    private int countAni,indexAni,timeAni = 10;
    private Constants.PlayerState playerState = IDLE;
    private boolean right,left,jump;
    private float playerSpeed = 2.0f;
    private boolean moving = false;
    private boolean attacking = false;
    private int [][]lvData;
    private BufferedImage [][] animation;
    private float xDrawOffset = 5*Game.SCALE;
    private float yDrawOffset = 5*Game.SCALE;
    private float airSpeed = 1.5f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.8f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    //status bar

    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // AttackBox
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;


    Game game;

    public Player(int x,int y,int width,int height,Game game) {
        super(x,y,width,height);
        this.game = game;
        loadIdleAnimation();
        initHitBox(x,y,28* Game.SCALE,25*Game.SCALE);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
    }

    public void  update(){
        updateHealthBar();
        if (currentHealth <= 0){
            game.setGameOver(false);
            game.resetAll();
            return;
        }

        updateAttackBox();

        updatePos();
        if (attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();


    }

    private void checkAttack() {
        if (attackChecked || indexAni != 1)
            return;
        attackChecked = true;
        game.checkEnemyHit(attackBox);


    }

    private void updateAttackBox() {
        if (right)
            attackBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 10);
        else if (left)
            attackBox.x = hitBox.x - hitBox.width - (int) (Game.SCALE * 10);

        attackBox.y = hitBox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }


    public void  render(Graphics g, int xLvOffset){
        g.drawImage(animation[playerState.getPlayerAction()][indexAni],
                (int)(hitBox.x - xDrawOffset) - xLvOffset + flipX,
                (int)(hitBox.y - yDrawOffset),width * flipW,height,null);
//        drawHitBox(g, xLvOffset);
        drawUI(g);
//        drawAttackBox(g,xLvOffset);

    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - lvlOffsetX, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);

    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void loadIdleAnimation() {
            BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
            animation = new BufferedImage[11][11];
            for (int i = 0 ;i < animation.length;i++)
                for (int j = 0;j < animation[i].length ; j++)
                    animation[i][j] = image.getSubimage(78*j,58*i,78,58);

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.HEALTH_POWER_BAR);
    }

    public  void  loadLvData(int [][] lvData){
        this.lvData = lvData;
        if (!IsEntityOnFloor(hitBox,lvData))
            inAir = true;
    }

    public void updatePos(){

        moving = false;

        if (jump)
            jump();



        if (!inAir)
            if (!left && !right || (right&&left))
                return;

        float xSpeed = 0, ySpeed = 0;

       if (right){
           xSpeed += playerSpeed;
           flipX = 0;
           flipW = 1;
       }


        if (left){
            xSpeed -= playerSpeed;
            flipX = width;
            flipW = -1;
        }


        if (!inAir){
            if (!IsEntityOnFloor(hitBox,lvData)){
                inAir = true;
            }
        }

        if (inAir){
            if (CanMoveHere(hitBox.x+ xSpeed, hitBox.y+airSpeed, hitBox.width, hitBox.height,lvData)){
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            }else {
                hitBox.y = HelpMethods.GetEntityYPosUnderRoofOrAboveFloor(hitBox,airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        }else {
            updateXPos(xSpeed);
        }
        moving = true;



    }



    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir  = false;
        airSpeed = 0;
    }


    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitBox.x+ xSpeed, hitBox.y, hitBox.width, hitBox.height,lvData)){
            hitBox.x += xSpeed;
        }else {
            hitBox.x = GetEntityXPosNextToWall(hitBox,xSpeed);
        }
    }

    public void changeHealth(int value) {
        currentHealth += value;

        if (currentHealth <= 0)
            currentHealth = 0;
        else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }


    public void updateAnimationTick(){
        countAni++;
        if (countAni >= timeAni){
            countAni = 0;
            indexAni++;
            if (indexAni >= playerState.getSpriteN() ){
                indexAni = 0;
                attacking = false;
                attackChecked = false;
            }
        }

    }

    private void resetAniTick() {
        countAni = 0;
        indexAni = 0;
    }


    public void setAnimation(){
        Constants.PlayerState startAction = playerState;

        if (moving)playerState = RUNNING;
        else playerState = IDLE;

        if (attacking){
            playerState = ATTACKING;
            if (startAction != ATTACKING){
                indexAni = 1;
                countAni = 0;
                return;
            }
        }

        if (inAir){
            if(airSpeed > 0){
                playerState = JUMPING;
            } else playerState = FALLING;
        }

        if (startAction != playerState)resetAniTick();
    }


    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }



    public void serDirBoolean() {
        right = false;
        left = false;

    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        inAir = false;
        attacking = false;
        moving = false;
        playerState = IDLE;
        currentHealth = maxHealth;
        hitBox.x = x;
        hitBox.y = y;
        if (!IsEntityOnFloor(hitBox,lvData))
            inAir = true;
    }
}
