package gamecode;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;

public class NetPanel extends JPanel implements KeyListener, Runnable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // 定义画板中要画的东西，比如坦克,子弹
    Herotank hero;
    Herotank hero2;
    Vector<Enemytank> enemytanks = new Vector<>();
    Vector<Bomb> bombs = new Vector<>();
    Socket socket = null;
    int[][] walls;
    int score;
    // 定义部分资源，如图片
    Image bg = null;
    Image board = null;
    Image bullet = null;
    Image enemybullet = null;
    Image bomb1 = null;
    Image bomb2 = null;
    Image bomb3 = null;
    Image bomb4 = null;
    Image bomb5 = null;
    Image bomb6 = null;
    Image wall = null;

    ObjectOutputStream oos=null;
    ObjectInputStream ois=null;

    public NetPanel() {
        setSize(1000, 700);
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

        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            System.out.println("玩家端连接到客户端");
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }
    @Override
    public void paint(Graphics g) {
        //super.paint(g);

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

        if(hero2.isLive())
            drawtank(g, hero2.getX(), hero2.getY(), hero2.getDir(), 2, hero2.getHp(),100);
        else
            System.out.println("Game Over");
        //敌方坦克
        for(Iterator<Enemytank> it=enemytanks.iterator();it.hasNext();)
        {
            Enemytank tmp=it.next();
            if(tmp.isLive())
                drawtank(g, tmp.getX(), tmp.getY(), tmp.getDir(), 1,tmp.getHp(),50);
            //是否存在问题？？for不能遍历删除
        }

        // 3.画出子弹，并删除死亡子弹
        //己方角色的子弹
        for(Iterator<Bullet> it = hero.getBullets().iterator();it.hasNext();)
        {
            Bullet tmpb=it.next();
            if(tmpb.getIsLive())
                drawbullet(g,tmpb.getX(),tmpb.getY(),0);
        }
        for(Iterator<Bullet> it = hero2.getBullets().iterator();it.hasNext();)
        {
            Bullet tmpb=it.next();
            if(tmpb.getIsLive())
                drawbullet(g,tmpb.getX(),tmpb.getY(),0);
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
            }
        }
        System.out.println("客户端完成了一次绘制");
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
        } else if(type == 1){
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
        }else{
            switch (dir) {
                case 0:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/hero/2/hero-u.png"), x,
                            y, 50, 50, this);
                    break;
                case 1:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/hero/2/hero-r.png"), x,
                            y, 50, 50, this);
                    break;
                case 2:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/hero/2/hero-d.png"), x,
                            y, 50, 50, this);
                    break;
                case 3:
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("gameresources/image/tank/hero/2/hero-l.png"), x,
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
        if(type==0||type==3)
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
                clientsend("0");
                // hero.moveup();
                // hero.setDir(0);
                break;
            case (KeyEvent.VK_RIGHT):
                clientsend("1");
                // hero.moveright();
                // hero.setDir(1);
                break;
            case (KeyEvent.VK_DOWN):
                clientsend("2");
                // hero.movedown();
                // hero.setDir(2);
                break;
            case (KeyEvent.VK_LEFT):
                clientsend("3");
                // hero.moveleft();
                // hero.setDir(3);
                break;
            case (KeyEvent.VK_SPACE):
                clientsend("4");
                // hero.shoot();
                break;
            default:
                break;
        }

        repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void clientsend(String s)
    {
        try {
            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("发送一条动作信息");
        //是否要加结束符
        //debug添加
    }
    public void clientreceive()
    {
        GameState gs=new GameState();
        try{
            //ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("可读取得字节数有:"+ois.available());
            gs = (GameState)ois.readObject();
            this.setHero(gs.getHero());
            this.setHero2(gs.getHero2());
            this.setEnemytanks(gs.getEnemytanks());
            this.setBombs(gs.getBombs());
            this.setWalls(gs.getWalls());
            this.setScore(gs.getScore());
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("收到后并加载成功地图信息,取到gs打印:");
        System.out.println(this.hero.getX()+this.hero.getY());
        System.out.println(this.hero2.getX()+this.hero2.getY());
        System.out.println("敌人0的x："+this.enemytanks.get(0).getX()+"敌人0的y:"+this.enemytanks.get(0).getY());
        System.out.println(this.walls);

    }

    @Override
    public void run() {
        while (true) {
            // try {
            //     Thread.sleep(SleepTime.SHORT_SLEEPTIME);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
            clientreceive();
            repaint();
        }

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