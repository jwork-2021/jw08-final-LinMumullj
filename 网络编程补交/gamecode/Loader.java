package gamecode;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Loader {
    private static String saveplace = "saving/save.dat";
    private static FileInputStream fis = null;
    private static ObjectInputStream ois = null;
    private static boolean opensuc=true;
    public static boolean load(MyPanel panel) {
        opensuc=true;
        try {
            fis = new FileInputStream(saveplace);
            ois=new ObjectInputStream(fis);
            GameState gamestate = (GameState)ois.readObject();
            panel.setHero(gamestate.hero);
            for(int j=0;j<panel.hero.getBullets().size();j++)
            {
                new Thread(panel.hero.getBullets().get(j)).start();
            }
            panel.setEnemytanks(gamestate.enemytanks);
            for(int i=0;i<panel.enemytanks.size();i++)
            {
                Enemytank tmp = panel.enemytanks.get(i);
                for(int j=0;j<tmp.getBullets().size();j++)
                {
                    new Thread(tmp.getBullets().get(j)).start();
                }
                
                new Thread(tmp).start();
                
            }
            panel.setBombs(gamestate.bombs);
            for(int i=0;i<panel.bombs.size();i++)
            {
                new Thread(panel.bombs.get(i)).start();
            }
            panel.setWalls(gamestate.walls);
            panel.setScore(gamestate.score);
        } catch (Exception e) {
            opensuc=false;
            e.printStackTrace();
        } finally{
            if(ois!=null)
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        
        return opensuc;
    }

}