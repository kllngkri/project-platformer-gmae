package levels;

public class Level {
    private int[][] lvData;

    public Level(int[][] MapLvData) {
        this.lvData = MapLvData;
    }

    public int getSpriteIndex(int x,int y){
        return lvData[y][x];
    }

    public int[][] getLevelData(){
        return lvData;
    }

}
