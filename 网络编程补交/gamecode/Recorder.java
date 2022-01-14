package gamecode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Recorder {
    private static String saveplace = "saving/save.dat";
    private static ObjectOutputStream oos = null;
    private static FileOutputStream fw = null;
    private static GameState gamestate=null;
    public static void save(MyPanel panel){
        try {
            fw = new FileOutputStream(saveplace);
            oos=new ObjectOutputStream(fw);
            gamestate=new GameState(panel.getHero(), panel.getEnemytanks(), panel.getBombs(), panel.getWalls(), panel.getScore());
            oos.writeObject(gamestate);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(oos!=null)
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        

    }

}

