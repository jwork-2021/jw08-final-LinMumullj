package gamecode;

import java.io.Serializable;
import java.util.Vector;

public class Tank implements Serializable{
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int hp;
    private int x;
    private int y;
    private int dir;
    private int speed;
    private int type;
    private boolean isLive;
    //待优化，碰撞检测传入子弹，敌人vector，主角tank，walls
    protected Vector<Bullet> bullets;
    private Vector<Enemytank> others;
    private Herotank hero;
    private int[][] walls;
    public boolean iscollide(int x,int y)
    {
        //判断跟坦克的碰撞
        if(hero!=this)
        {
            if(x>(hero.getX()-50)&&x<(hero.getX()+50)&&y>(hero.getY()-50)&&y<(hero.getY()+50))
                return true;
        }
        for(int i=0;i<others.size();i++)
        {
            Tank tmp=others.get(i);
            if(tmp!=this)
            {
                if(x>(tmp.getX()-50)&&x<(tmp.getX()+50)&&y>(tmp.getY()-50)&&y<(tmp.getY()+50))
                    return true;
            }
        }
        

        //判断跟箱子的碰撞
        for(int i=0;i<14;i++)
        {
            for(int j=0;j<14;j++)
            {
                if(walls[i][j]==1)
                {
                    if(x>(i*50-50)&&x<(i*50+50)&&y>(j*50-50)&&y<(j*50+50))
                        return true;
                }
            }
        }
        return false;
    }
    Tank()
    {
        hp=100;
        x=0;
        y=0;
        dir=2;
        speed=1;
        type=1;
        isLive=true;
        bullets=new Vector<>();
    }

    public Tank(int hp, int x, int y, int dir,int type) {
        this.hp = hp;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.speed = 3;
        this.type=type;
        this.isLive = true;
        bullets=new Vector<>();
    }
    public void move(int dir)
    {
        switch(getDir()){
            case 0:
                moveup();
                break;
            case 1:
                moveright();
                break;
            case 2:
                movedown();
                break;
            case 3:
                moveleft();
                break;
            default:
                break;
        }
    }
    public void moveup(){
        if((y-speed)>=0&&(!iscollide(this.x,this.y-speed))){
            y -= speed;
            dir=0;
        }
    }
    public void moveright(){
        if((x+speed)<=650&&(!iscollide(this.x+speed,this.y)))
        {
            x += speed;
            dir=1;
        }
    }
    public void movedown(){
        if((y+speed)<=650&&(!iscollide(this.x,this.y+speed)))
        {
            y += speed;
            dir=2;
        }
    }
    public void moveleft(){
        if((x-speed)>=0&&(!iscollide(this.x-speed,this.y)))
        {
            x -= speed;
            dir=3;
        }
    }

    public void shoot() {
        switch(getDir()){
            case 0:
                addBullet(getX()+23,getY()-5,0,2);
                break;
            case 1:
                addBullet(getX()+50,getY()+23,1,2);
                break;
            case 2:
                addBullet(getX()+23,getY()+50,2,2);
                break;
            case 3:
                addBullet(getX()-5,getY()+23,3,2);
                break;
            default:
                break;
        }
    }
    public void addBullet(int x, int y, int dir, int speed) {
        Bullet newbl=new Bullet(x,y,dir,speed);
        bullets.add(newbl);
        new Thread(newbl).start();
    }


    //getter&setter
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    public void getattack(int hp){
        this.hp-=hp;
        if(this.hp<=0)
        {
            this.hp=0;
            setLive(false);
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

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Vector<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(Vector<Bullet> bullets) {
        this.bullets = bullets;
    }

    public Vector<? extends Tank> getOthers() {
        return others;
    }

    public void setOthers(Vector<Enemytank> others) {
        this.others = others;
    }

    public int[][] getWalls() {
        return walls;
    }

    public void setWalls(int[][] walls) {
        this.walls = walls;
    }

    public Herotank getHero() {
        return hero;
    }

    public void setHero(Herotank hero) {
        this.hero = hero;
    }
    
}