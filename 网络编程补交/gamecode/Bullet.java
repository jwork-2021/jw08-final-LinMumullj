package gamecode;

import java.io.Serializable;

public class Bullet implements Runnable ,Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private int dir;
    private int speed;
    private boolean isLive;

    public Bullet(int x, int y, int dir, int speed) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.speed = speed;
        isLive = true;
    }

    @Override
    public void run() {
        while (isLive) {
            move(dir);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }

    public void move(int dir)
    {
        switch(dir)
        {
            case 0:
                y-=speed;
                break;
            case 1:
                x+=speed;
                break;
            case 2:
                y+=speed;
                break;
            case 3:
                x-=speed;
                break;
            default:
                break;
        }
        if(!(x>=0&&x<=698&&y>=0&&y<=698))
            isLive=false;
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

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(boolean isLive) {
        this.isLive = isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }
}