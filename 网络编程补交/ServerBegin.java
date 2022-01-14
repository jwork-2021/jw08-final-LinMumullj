import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import gamecode.Bomb;
import gamecode.Bullet;
import gamecode.Enemytank;
import gamecode.GameState;
import gamecode.Herotank;
import gamecode.SleepTime;
import gamecode.Tank;

public class ServerBegin {

    private Vector<socketmanager> socketmanagers = null;
    private ServerSocket serversocket = null;
    public rungame nowgame = null;
    public Herotank hero;
    public Herotank hero2;
    public Vector<Enemytank> enemytanks = new Vector<>();
    public Vector<Bomb> bombs = new Vector<>();
    public int[][] walls;
    public int score;

    public ServerBegin() {
        System.out.println("服务器正在监听9999端口");
        socketmanagers = new Vector<>();
        int num = 0;
        try {
            serversocket = new ServerSocket(9999);
            while (socketmanagers.size() < 2) {
                Socket socket = serversocket.accept();
                socketmanagers.add(new socketmanager(socket, socketmanagers.size()));
                ++num;
                System.out.println(num + "个玩家连接成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serversocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(num + "个玩家准备开始游戏");
        nowgame = new rungame();
        new Thread(nowgame).start();
    }

    class socketmanager implements Runnable {
        Socket socket = null;
        int SID = 0;
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;

        public socketmanager(Socket socket, int SID) {
            this.socket = socket;
            this.SID = SID;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }

        public void receive() {
            try {
                //ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String s = (String) ois.readObject();
                Herotank thishero = null;
                if (SID == 0)
                    thishero = getHero();
                else
                    thishero = getHero2();

                System.out.println("服务端"+SID+"接收到的动作信息为："+ s);
                switch (s) {
                    case ("0"):
                        thishero.moveup();
                        thishero.setDir(0);
                        break;
                    case ("1"):
                        thishero.moveright();
                        thishero.setDir(1);
                        break;
                    case ("2"):
                        thishero.movedown();
                        thishero.setDir(2);
                        break;
                    case ("3"):
                        thishero.moveleft();
                        thishero.setDir(3);
                        break;
                    case ("4"):
                        thishero.shoot();
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void send() {
            System.out.println("创建GameState前的地图信息:");
            System.out.println(hero.getX()+hero.getY());
            System.out.println(hero2.getX()+hero2.getY());
            System.out.println("敌人0的x："+enemytanks.get(0).getX()+"敌人0的y:"+enemytanks.get(0).getY());
            GameState gs = new GameState(getHero(), getHero2(), getEnemytanks(), getBombs(), getWalls(), getScore());
            //
            System.out.println("创建GameState后的地图信息:");
            System.out.println(gs.getHero().getX()+gs.getHero().getY());
            System.out.println(gs.getHero2().getX()+gs.getHero2().getY());
            System.out.println("敌人0的x："+gs.getEnemytanks().get(0).getX()+"敌人0的y:"+gs.getEnemytanks().get(0).getY());
            try {
                //ObjectOutputStream oos = new ObjectOutputStream(socketmanagers.get(0).socket.getOutputStream());
                oos.writeObject(gs);
                oos.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // try {
            //     //ObjectOutputStream oos = new ObjectOutputStream(socketmanagers.get(1).socket.getOutputStream());
            //     oos.writeObject(gs);
            // } catch (IOException e1) {
            //     e1.printStackTrace();
            // }
            // 是否要加结束符
        }
        public class receiver implements Runnable{
            @Override
            public void run() {
                while(true)
                {
                    receive();
                    System.out.println("从客户端"+SID+" 收到一条动作信息并加载成功");
                }
            }
            
        }
        @Override
        public void run() {
            send();
            System.out.println("向客户端"+SID+" 发送一条地图信息");
            new Thread(new receiver()).start();
            System.out.println("启动对客户端"+SID+"的循环接收动作信息");
            while (true) {
                try {
                    Thread.sleep(SleepTime.SHORT_SLEEPTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                send();
                System.out.println("向客户端"+SID+" 发送一条地图信息");
            }
        }
    }


    ////////////////////////////////////////////// 跑游戏/////////////////////////////////////////////////////////////
    class rungame implements Runnable {

        public rungame()
        {
            for (int i = 0; i < 2; i++) {
                new Thread(socketmanagers.get(i)).start();
            }
            System.out.println("服务器启动了所有socketmanagers");
            score = 0;
            walls = new int[14][14];
            createWall();
            //hero
            hero = new Herotank(300, 640, 0);
            hero.setWalls(walls);
            hero.setOthers(enemytanks);
            hero.setHero(hero);
            //hero2
            hero2 = new Herotank(400, 640, 0);
            hero2.setWalls(walls);
            hero2.setOthers(enemytanks);
            hero2.setHero(hero2);
            //
            createEnemy();
        }
        @Override
        public void run() {

            // for (int i = 0; i < 2; i++) {
            //     new Thread(socketmanagers.get(i)).start();
            // }
            // System.out.println("服务器启动了所有socketmanagers");
            // score = 0;
            // walls = new int[14][14];
            // createWall();
            // //hero
            // hero = new Herotank(300, 640, 0);
            // hero.setWalls(walls);
            // hero.setOthers(enemytanks);
            // hero.setHero(hero);
            // //hero2
            // hero2 = new Herotank(400, 640, 0);
            // hero2.setWalls(walls);
            // hero2.setOthers(enemytanks);
            // hero2.setHero(hero2);
            // //
            // createEnemy();
            while (hero.isLive()) {

                try {
                    Thread.sleep(SleepTime.SHORT_SLEEPTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //删子弹，加炸弹
                // 2.画出坦克,并删除死亡坦克
                //己方角色
                if(!hero.isLive())
                    System.out.println("Game Over");
                if(!hero2.isLive())
                    System.out.println("Game Over");
                //敌方坦克
                for(Iterator<Enemytank> it=enemytanks.iterator();it.hasNext();)
                {
                    Enemytank tmp=it.next();
                    if(!tmp.isLive())
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
                    if(!tmpb.getIsLive())
                        it.remove();
                }
                //敌方坦克的子弹
                for(int i=0;i<enemytanks.size();i++)
                {
                    Enemytank tmp=enemytanks.get(i);
                    for(Iterator<Bullet> it = tmp.getBullets().iterator();it.hasNext();)
                    {
                        Bullet tmpb2=it.next();
                        if(!tmpb2.getIsLive())
                            it.remove();
                    }
                }

                //4.画出爆炸
                for(Iterator<Bomb> it=bombs.iterator();it.hasNext();)
                {
                    Bomb tmp=it.next();
                    if(!tmp.isLive())
                    {
                        it.remove();
                    }
                }

                //5.碰撞检测
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
                if(hero2.getBullets()!=null)
                {
                    for(int i=0;i<hero2.getBullets().size();i++)
                    {
                        Bullet tmp=hero2.getBullets().get(i);
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

                System.out.println("客户端逻辑更新了一次状态");
            }


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
                enemyadd.setWalls(walls);
                enemyadd.setOthers(enemytanks);
                enemyadd.setHero(hero);
                enemytanks.add(enemyadd);
                new Thread(enemyadd).start();
            }
        }

        public void addBomb(int x,int y)
        {
            Bomb bombadd=new Bomb(x, y);
            bombs.add(bombadd);
            new Thread(bombadd).start();
        }

            
    }



    public static void main(String[] args) {
        ServerBegin serverbegin=new ServerBegin();
        //new Thread(serverbegin.nowgame).start();
        
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