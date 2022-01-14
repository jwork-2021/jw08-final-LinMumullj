package gamecode;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;

public class MyPanel extends JPanel implements KeyListener,Runnable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // 定义画板中要画的东西，比如坦克,子弹
    Herotank hero;
    Vector<Enemytank> enemytanks=new Vector<>();
    //Vector<Bullet> bullets=new Vector<>();
    Vector<Bomb> bombs=new Vector<>();

    int[][] walls;
    int score;
    // 定义部分资源，如图片
    Image bg = null;
    Image board = null;
    Image bullet=null;
    Image enemybullet=null;
    Image bomb1=null;
    Image bomb2=null;
    Image bomb3=null;
    Image bomb4=null;
    Image bomb5=null;
    Image bomb6=null;
    Image wall=null;

    public MyPanel() {
        setSize(1000, 700);
        score=0;
        walls=new int[14][14];
        createWall();
        hero = new Herotank(400, 640, 0);
        hero.setWalls(walls);
        hero.setOthers(enemytanks);
        hero.setHero(hero);
        createEnemy();
        bg = Toolkit.getDefaultToolkit().getImage("gameresources/image/bg-sand.jpg");
        board = Toolkit.getDefaultToolkit().getImage("gameresources/image/bg-grass.jpg");
        bullet = Toolkit.getDefaultToolkit().getImage("gameresources/image/bullet/normal.png");
        enemybullet = Toolkit.getDefaultToolkit().getImage("gameresources/image/bullet/missile.png");
        bomb1 = Toolkit.getDefaultToolkit().getImage("gameresources/image/explosion/6.png");
        bomb2 = Toolkit.getDefaultToolkit().getImage("gameresources/image/explosion/5.png");
        bomb3 = Toolkit.getDefaultToolkit().getImage("gameresources/image/explosion/4.png");
        bomb4 = Toolkit.getDefaultToolkit().getImage("gameresources/image/explosion/3.png");
        bomb5 = Toolkit.getDefaultToolkit().getImage("gameresources/image/explosion/2.png");
        bomb6 = Toolkit.getDefaultToolkit().getImage("gameresources/image/explosion/1.png");
        wall = Toolkit.getDefaultToolkit().getImage("gameresources/image/wall/wood.gif");
    }
    public void createWall()
    {
        for(int i=0;i<14;i++)
        {
            for(int j=0;j<14;j++)
            {
                walls[i][j]=0;
            }
        }
        walls[4][4]=1;
        walls[4][9]=1;
        walls[5][5]=1;
        walls[5][8]=1;
        walls[6][6]=1;
        walls[6][7]=1;
        walls[7][7]=1;
        walls[7][6]=1;
        walls[8][8]=1;
        walls[8][5]=1;
        walls[9][9]=1;
        walls[9][4]=1;
    }


    public void createEnemy()
    {
        for(int i=0;i<3;i++)
        {
            Enemytank enemyadd=new Enemytank(20+i*200, 10, 2);
            enemytanks.add(enemyadd);
            enemyadd.setWalls(walls);
            enemyadd.setOthers(enemytanks);
            enemyadd.setHero(hero);
            new Thread(enemyadd).start();
        }
    }

    public void addBomb(int x,int y)
    {
        Bomb bombadd=new Bomb(x, y);
        bombs.add(bombadd);
        new Thread(bombadd).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制所有游戏中出现的东西

        // 1.绘制背景&&得分&&墙壁
        //背景
        g.drawImage(bg, 0, 0, 700, 700, this);
        g.drawImage(board, 700, 0, 300, 700, this);
        //得分
        g.setColor(Color.CYAN);
        Font font = new Font("宋体",Font.BOLD,30);
        g.setFont(font);
        g.drawString("得分：",720,50);
        g.drawString(score+"", 810, 50);

        g.setColor(Color.BLACK);
        g.drawString("按“S”保存进度",720,200);
        g.drawString("按“L”加载存档",720,300);
        //墙壁
        for(int i=0;i<14;i++)
        {
            for(int j=0;j<14;j++)
            {
                if(walls[i][j]==1)
                {
                    g.drawImage(wall, i*50, j*50, 50, 50, this);
                }
            }
        }


        // 2.画出坦克,并删除死亡坦克
        //己方角色
        if(hero.isLive())
            drawtank(g, hero.getX(), hero.getY(), hero.getDir(), 0, hero.getHp(),100);
        else
            System.out.println("Game Over");

        //敌方坦克
        for(Iterator<Enemytank> it=enemytanks.iterator();it.hasNext();)
        {
            Enemytank tmp=it.next();
            if(tmp.isLive())
                drawtank(g, tmp.getX(), tmp.getY(), tmp.getDir(), 1,tmp.getHp(),50);
            else
            {
                addBomb(tmp.getX(), tmp.getY());
                it.remove();
                score+=100;
            }

            //是否存在问题？？for不能遍历删除
        }

        // 3.画出子弹，并删除死亡子弹
        //己方角色的子弹
        for(Iterator<Bullet> it = hero.getBullets().iterator();it.hasNext();)
        {
            Bullet tmpb=it.next();
            if(tmpb.getIsLive())
                drawbullet(g,tmpb.getX(),tmpb.getY(),0);
            else
                it.remove();
        }
        //敌方坦克的子弹
        for(int i=0;i<enemytanks.size();i++)
        {
            Enemytank tmp=enemytanks.get(i);
            for(Iterator<Bullet> it = tmp.getBullets().iterator();it.hasNext();)
            {
                Bullet tmpb2=it.next();
                if(tmpb2.getIsLive())
                    drawbullet(g,tmpb2.getX(),tmpb2.getY(),1);
                else
                    it.remove();
            }
        }

        //4.画出爆炸
        for(Iterator<Bomb> it=bombs.iterator();it.hasNext();)
        {
            Bomb tmp=it.next();
            if(tmp.isLive())
            {
                int tmp2=tmp.getNowtime();
                if(tmp2>7){
                    g.drawImage(bomb1, tmp.getX(), tmp.getY(), 50, 50, this);
                }else if(tmp2>6){
                    g.drawImage(bomb2, tmp.getX(), tmp.getY(), 50, 50, this);
                }else if(tmp2>5){
                    g.drawImage(bomb3, tmp.getX(), tmp.getY(), 50, 50, this);
                }else if(tmp2>4){
                    g.drawImage(bomb4, tmp.getX(), tmp.getY(), 50, 50, this);
                }else if(tmp2>3){
                    g.drawImage(bomb5, tmp.getX(), tmp.getY(), 50, 50, this);
                }else{
                    g.drawImage(bomb6, tmp.getX(), tmp.getY(), 50, 50, this);
                }
            }else{
                it.remove();
            }
        }
    }

    // 类型自己为0，敌人为1
    // 方向上为0，顺时针增加
    public void drawtank(Graphics g, int x, int y, int dir, int type, int hp,int maxhp) {
        if (type == 0) {
            switch (dir) {
                case 0:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/enemy/2/tank-u.png"), x,
                            y, 50, 50, this);
                    break;
                case 1:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/enemy/2/tank-r.png"), x,
                            y, 50, 50, this);
                    break;
                case 2:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/enemy/2/tank-d.png"), x,
                            y, 50, 50, this);
                    break;
                case 3:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/enemy/2/tank-l.png"), x,
                            y, 50, 50, this);
                    break;
            }
        } else {
            switch (dir) {
                case 0:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/enemy/1/tank-u.png"), x,
                            y, 50, 50, this);
                    break;
                case 1:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/enemy/1/tank-r.png"), x,
                            y, 50, 50, this);
                    break;
                case 2:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/enemy/1/tank-d.png"), x,
                            y, 50, 50, this);
                    break;
                case 3:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/enemy/1/tank-l.png"), x,
                            y, 50, 50, this);
                    break;
            }
        }
        g.setColor(Color.white);
        g.fillRect(x, y-5, 50, 5);
        g.setColor(Color.red);
        g.fillRect(x, y-5, (int)(50*((double)hp/maxhp)), 5);
    }

    public void drawbullet(Graphics g, int x, int y,int type)
    {
        if(type==0)
            g.drawImage(bullet, x, y, 5,5,this);
        else    
            g.drawImage(enemybullet, x, y, 5,5,this);
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_UP):
                hero.moveup();
                hero.setDir(0);
                break;
            case (KeyEvent.VK_RIGHT):
                hero.moveright();
                hero.setDir(1);
                break;
            case (KeyEvent.VK_DOWN):
                hero.movedown();
                hero.setDir(2);
                break;
            case (KeyEvent.VK_LEFT):
                hero.moveleft();
                hero.setDir(3);
                break;
            case (KeyEvent.VK_SPACE):
                hero.shoot();
                break;
            case (KeyEvent.VK_S):
                Recorder.save(this);
                break;
            case (KeyEvent.VK_L):
                Loader.load(this);
                break;
            default:
                break;
        }

        //repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    //碰撞检测函数
    public void isCollide(Bullet bullet,Tank tank)
    {
        if((bullet.getX()>tank.getX()&&bullet.getX()<tank.getX()+50)&&(bullet.getY()>tank.getY()&&bullet.getY()<tank.getY()+50))
        {
            tank.getattack(10);
            bullet.setIsLive(false);
        }

        
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //碰撞检测
            //判断己方子弹与敌方碰撞
            if(hero.getBullets()!=null)
            {
                for(int i=0;i<hero.getBullets().size();i++)
                {
                    Bullet tmp=hero.getBullets().get(i);
                    for(int j=0;j<enemytanks.size();j++)
                    {
                        Enemytank tmp2=enemytanks.get(j);
                        isCollide(tmp,tmp2);
                    }
                    //判断与箱子碰撞
                    for(int m=0;m<14;m++)
                    {
                        for(int n=0;n<14;n++)
                        {
                            if(walls[m][n]==1)
                            {
                                if((tmp.getX()>m*50&&tmp.getX()<(m*50+50))&&(tmp.getY()>n*50&&tmp.getY()<(n*50+50)))
                                {
                                    tmp.setIsLive(false);
                                    walls[m][n]=0;
                                }
                            }
                        }
                    }
                }

            }   
            //判断敌方子弹与己方碰撞
            for(int m=0;m<enemytanks.size();m++)
            {
                Vector<Bullet> tmp=enemytanks.get(m).getBullets();
                if(tmp!=null)
                {
                    for(int i=0;i<tmp.size();i++)
                    {
                        Bullet tmp2=tmp.get(i);
                        isCollide(tmp2, hero);
                        //判断箱子
                        for(int p=0;p<14;p++)
                        {
                            for(int q=0;q<14;q++)
                            {
                                if(walls[p][q]==1)
                                {
                                    if((tmp2.getX()>p*50&&tmp2.getX()<(p*50+50))&&(tmp2.getY()>q*50&&tmp2.getY()<(q*50+50)))
                                    {
                                        tmp2.setIsLive(false);
                                        walls[p][q]=0;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            
            repaint();
        }

    }

    public Herotank getHero() {
        return hero;
    }

    public void setHero(Herotank hero) {
        this.hero = hero;
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