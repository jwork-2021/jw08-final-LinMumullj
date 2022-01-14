package gamecode;

import java.io.Serializable;
import java.util.Vector;

public class GameState implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Herotank hero;
    Herotank hero2;
    Vector<Enemytank> enemytanks;
    Vector<Bomb> bombs;
    int[][] walls;
    int score;

    public GameState()
    {
        this.hero = null;
        this.hero2=null;
        this.enemytanks = null;
        this.bombs = null;
        this.walls = null;
        this.score = 0;
    }
    public GameState(Herotank hero, Vector<Enemytank> enemytanks, Vector<Bomb> bombs, int[][] walls, int score) {
        this.hero = hero;
        this.hero2=null;
        this.enemytanks = enemytanks;
        this.bombs = bombs;
        this.walls = walls;
        this.score = score;
    }

    public GameState(Herotank hero, Herotank hero2, Vector<Enemytank> enemytanks, Vector<Bomb> bombs, int[][] walls,
            int score) {
        this.hero = hero;
        this.hero2 = hero2;
        this.enemytanks = enemytanks;
        this.bombs = bombs;
        this.walls = walls;
        this.score = score;
    }

    public Herotank getHero() {
        return hero;
    }

    public void setHero(Herotank hero) {
        this.hero = hero;
    }

    public Herotank getHero2() {
        return hero2;
    }

    public void setHero2(Herotank hero2) {
        this.hero2 = hero2;
    }

    public Vector<Enemytank> getEnemytanks() {
        return enemytanks;
    }

    public void setEnemytanks(Vector<Enemytank> enemytanks) {
        this.enemytanks = enemytanks;
    }

    public Vector<Bomb> getBombs() {
        return bombs;
    }

    public void setBombs(Vector<Bomb> bombs) {
        this.bombs = bombs;
    }

    public int[][] getWalls() {
        return walls;
    }

    public void setWalls(int[][] walls) {
        this.walls = walls;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    

}