package net;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Receiver;

import asciiPanel.AsciiPanel;
import myruguelike.Creature;
import myruguelike.CreatureFactory;
import myruguelike.Items;
import myruguelike.World;
import myruguelike.WorldBuilder;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import myruguelike.screens.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//Net UDPversion
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetPlayScreen extends PlayScreen implements Screen,Serializable {
	/**
	 *
	 */
    //public Creature player2;
	public Items player2;
    public transient DatagramSocket socket;
    transient ByteArrayOutputStream bos;
    transient ObjectOutputStream oos;
    public int port;
	public int sendport;
    public int tarport;
	
	public NetPlayScreen(){
        super();
        //Net
        try {
            //socket =new DatagramSocket(6666);
            bos=new ByteArrayOutputStream();
            oos=new ObjectOutputStream(new BufferedOutputStream(bos));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	}
    public NetPlayScreen(int port,int sendport,int tarport){
        super();
        //Net
		this.sendport=sendport;
        this.port=port;
        this.tarport=tarport;
		//
		this.player2=Items.PLAYER2;
		bos=null;
		oos=null;
		//
        try {
            //socket =new DatagramSocket(port);
            bos=new ByteArrayOutputStream();
            oos=new ObjectOutputStream(new BufferedOutputStream(bos));
        } catch (Exception e) {
            e.printStackTrace();
        }

		//开线程接包
        new Thread(new InfoReceive(port,this)).start();
	}
	public NetPlayScreen(World world,Creature player,int screenWidth,int screenHeight,List<String> messages,int score,int level,boolean iswin,boolean ispause){
		this.score=score;
		this.level=level;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.iswin=iswin;
		this.ispause=ispause;
		this.messages = messages;
		this.world=world;
		this.player=player;
        try {
            socket =new DatagramSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public NetPlayScreen(NetPlayScreen tmp){
		score=tmp.getScore();
		level=tmp.getLevel();
		screenWidth = tmp.getScreenWidth();
		screenHeight = tmp.getScreenHeight();
		iswin=tmp.iswin;
		ispause=tmp.ispause;
		messages = tmp.getMessages();
		world=tmp.getWorld();
		player=tmp.getPlayer();
		//未改动的：
		CreatureFactory creatureFactory = new CreatureFactory(world);
		createCreatures(creatureFactory);
        socket=tmp.socket;
	}
	@Override
	protected void createCreatures(CreatureFactory creatureFactory){
		player = creatureFactory.newPlayer(messages,this);  //将消息列表传递给playerAI
		// player2 =creatureFactory.newPlayer2(this);
		// for (int i = 0; i < 5; i++){
		// 	creatureFactory.newBat(this);
		// }
		// for (int i = 0; i < 10; i++){
		// 	creatureFactory.newEBat(player,this);
		// }
		// for (int i = 0; i < 3; i++){
		// 	creatureFactory.newHeart();
		// }
		// creatureFactory.newGourd();
		creatureFactory.newPlayer2();
	}
	public class content implements Serializable{
		public World world;
		public Creature player;
		public int score;
		public int level;
		public boolean iswin;
		public boolean ispause;
		content(World world,Creature player, int score,int level, boolean iswin,boolean ispause)
		{
			this.world=world;
			this.player=player;
			this.score=score;
			this.level=level;
			this.iswin=iswin;
			this.ispause=ispause;
		}
	}

	@Override
	public void displayOutput(AsciiPanel terminal) {
		int left = getScrollX();
		int top = getScrollY(); 
		
		displayTiles(terminal, left, top);
		displayMessages(terminal, messages);
		
		terminal.writeCenter("-- press [Esc] to back press [S] to save--", 26);

		String stats = String.format("HP: %3d/%3d", player.hp(), player.maxHp());
		if(player.hp()>70)
		{
			terminal.write(stats, 1, 24,AsciiPanel.green);
		}
		else if(player.hp()<=70&&player.hp()>40)
		{
			terminal.write(stats, 1, 24,AsciiPanel.yellow);
		}
		else
		{
			terminal.write(stats, 1, 24,AsciiPanel.red);
		}

		score=world.score;
		String stats2 = String.format("SCORE: %d", score);
		terminal.write(stats2,1,25,AsciiPanel.brightYellow);
		String stats3 = String.format("LEVEL: %d", level);
		terminal.write(stats3,1,26);

		terminal.write("Team members: ",64,24);
		terminal.write((char)2,64,26,AsciiPanel.brightCyan);
	}
	@Override
	public Screen respondToUserInput(KeyEvent key){
		switch (key.getKeyCode()){
		case KeyEvent.VK_ESCAPE: return new StartScreen();
		case KeyEvent.VK_S: 
		{
			try {
				save();
				System.out.println("Success");
			} catch (Exception e) {
				return this;
			}
		}
		case KeyEvent.VK_UP:player.moveBy( 0,-1); break;
		case KeyEvent.VK_DOWN:player.moveBy( 0, 1); break;
		case KeyEvent.VK_LEFT:player.moveBy(-1, 0); break;
		case KeyEvent.VK_RIGHT:player.moveBy( 1, 0); break;

		}
		//每次按键响应后，更新一次世界
		world.update();

        //UDP发送当前状态
        //1、把所有信息保存起来

        //PlayScreen tmp=new PlayScreen(world,player,screenWidth,screenHeight,messages,score,level,iswin,ispause);
		content tmp=new content(world, player, score, level, iswin, ispause);
        //2、通过socket发送
        try {
			socket=new DatagramSocket(sendport);
            oos.writeObject(tmp);
            oos.flush();
            byte[] datas2=bos.toByteArray();
            DatagramPacket packet2 =new DatagramPacket(datas2,0,datas2.length,new InetSocketAddress("localhost",tarport));
            //发送包裹
            socket.send(packet2);

			System.out.println(sendport+"向"+tarport+"发了一个包");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //释放资源
        if(socket!=null)
            socket.close();
        
		//
		System.out.println("玩家一发出信息");
		//world.update();
        //
        if(iswin==true)
			return new WinScreen();
		if (player.hp() < 1)
			return new LoseScreen();
		
		return this;
	}

	public Screen checkwin()
	{
		if(iswin==true)
			return new WinScreen();
		return this;
	}
}
