package gamecode;

import java.io.Serializable;

public class Bomb implements Runnable ,Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private int nowtime;
    private int maxtime;
    private boolean isLive;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
        this.maxtime = 9;
        this.nowtime = 9;
        isLive = true;
    }

    @Override
    public void run() {
        while (isLive) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            nowtime--;
            if(nowtime<0)
                isLive=false;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getNowtime() {
        return nowtime;
    }

    public void setNowtime(int nowtime) {
        this.nowtime = nowtime;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }
    
}