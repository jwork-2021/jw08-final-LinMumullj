import javax.swing.JFrame;
import java.awt.event.*;

import gamecode.Loader;
import gamecode.MyPanel;
import gamecode.NetPanel;
import gamecode.StartPanel;

public class Begin extends JFrame implements KeyListener{


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    StartPanel startpanel;
    MyPanel mypanel;
    NetPanel netpanel;
    public Begin()
    {
        
        //设置窗口属性：
        this.setSize(1010,730);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Tank War");
        this.setLocationRelativeTo(null);
        this.addKeyListener(this);
        
        startpanel = new StartPanel();
        this.add(startpanel);
        // //初始化面板
        // mypanel=new MyPanel();
        // //把面板放入窗口
        // this.add(mypanel);   
        // //添加键盘监听
        // this.addKeyListener(mypanel);
        // //让Panel自动刷新跑起来
        // Thread flashpanel =new Thread(mypanel);
        // flashpanel.start();


        
    }
    

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode())
        {
            case(KeyEvent.VK_1):
                //初始化面板
                mypanel=new MyPanel();
                //把面板放入窗口
                this.add(mypanel);   
                //添加键盘监听
                this.addKeyListener(mypanel);
                //让Panel自动刷新跑起来
                Thread flashpanel =new Thread(mypanel);
                flashpanel.start();
                break;
            case(KeyEvent.VK_2):
                //初始化面板
                mypanel=new MyPanel();
                //把面板放入窗口
                this.add(mypanel);   
                //添加键盘监听
                this.addKeyListener(mypanel);
                //让Panel自动刷新跑起来
                Thread flashpanel2 =new Thread(mypanel);
                flashpanel2.start();
                if(!Loader.load(mypanel))
                break;
            case(KeyEvent.VK_3):
                //初始化面板
                netpanel=new NetPanel();
                //把面板放入窗口
                this.add(netpanel);   
                //添加键盘监听
                this.addKeyListener(netpanel);
                //让Panel自动刷新跑起来
                Thread flashnetpanel =new Thread(netpanel);
                flashnetpanel.start();
                break;
            default:
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }


    
    public static void main(String[] args) {

        Begin newgame=new Begin();

    }
}