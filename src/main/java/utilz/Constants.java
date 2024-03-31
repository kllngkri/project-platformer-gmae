package utilz;


import main.Game;


public class Constants {


    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }



    public  enum Environment {
       ROCK_1(20,11);


        private final int widthDefault;
        private final int heightDefault;

        Environment(int widthDefault, int heightDefault) {
            this.widthDefault = widthDefault;
            this.heightDefault = heightDefault;
        }

        public int getWidthSize(){
            return (int)(widthDefault* Game.SCALE);
        }

        public int getHeightSize(){
            return (int)(heightDefault* Game.SCALE);
        }

    }




    public  enum PlayerState {
        RUNNING(10,8),
        IDLE(8,11) ,
        ATTACKING(0,3),
        JUMPING(9,1),
        FALLING(5,1);


        private final int playerAction;
        private final int spriteN;


        PlayerState( int playerAction1, int spriteN1) {
            this.playerAction = playerAction1;
            this.spriteN = spriteN1;
        }


        public int getPlayerAction() {
            return playerAction;
        }

        public int getSpriteN() {
            return spriteN;
        }

    }

    public static class EnemyConstant {

        public enum EnemyState {
            IDLE_CUCUMBER(0, 36),
            RUNNING_CUCUMBER(1, 12),
            ATTACK_CUCUMBER(2, 11),
            HIT_CUCUMBER(3, 8),
            DEAD_HIT_CUCUMBER(4, 6);

            private final int enemyAction;
            private final int spriteN;

            EnemyState(int enemyAction, int spriteN) {
                this.enemyAction = enemyAction;
                this.spriteN = spriteN;
            }


            public int getEnemyAction() {
                return enemyAction;
            }

            public int getSpriteN() {
                return spriteN;
            }

            public static int getWidth() {
                return (int) (64 * Game.SCALE);
            }

            public static int getHeight() {
                return (int) (68 * Game.SCALE);
            }

            public static int getWidthDefault_cucumber() {
                return 64;
            }

            public static int getHeightDefault_cucumber() {
                return 68;
            }

            public static int getCucumber() {
                return 0;
            }


            public static int cucumber_DRAWOFFSET_X() {
                return (int)Game.SCALE*22;
            }

            public static int cucumber_DRAWOFFSET_Y() {
                return (int)Game.SCALE*9;
            }

            public static int getCucumberMaxHealth(){
                return 10;
            }

            public static int getCucumberDamage(){
                return 15;
            }


        }



    }

}
