package gamecode;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.*;
public class StartPanel extends JPanel implements KeyListener{

    private Image bg=null;
    private Image board=null;
    public StartPanel(){
        setSize(1000, 700);
        bg = Toolkit.getDefaultToolkit().getImage("gameresources/image/bg-sand.jpg");
        board = Toolkit.getDefaultToolkit().getImage("gameresources/image/bg-grass.jpg");
    }

    @Override
    public void paint(Graphics g) {
        
        g.drawImage(bg, 0, 0, 700, 700, this);
        g.drawImage(board, 700, 0, 300, 700, this);
        Font font = new Font("宋体",Font.BOLD,50);
        g.setFont(font);
        g.drawString("按“1”开始新游戏", 100, 200);
        g.drawString("按“2”继续上局游戏", 100, 300);
        g.drawString("按“3”进行双人游戏", 100, 400);
    }



    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    
}