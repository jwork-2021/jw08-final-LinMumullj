package net;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import asciiPanel.AsciiPanel;
import myruguelike.Creature;
import myruguelike.CreatureFactory;
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

public class NetPlayScreen2 extends PlayScreen implements Screen,Serializable {
	/**
	 *
	 */
    public Creature player2;
    public DatagramSocket socket;
    ByteArrayOutputStream bos;
    ObjectOutputStream oos;
    public int port;
    public int tarport;
	public NetPlayScreen2(){
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
    public NetPlayScreen2(int port,int tarport){
        super();
        //Net
        this.port=port;
        this.tarport=tarport;
        try {
            //socket =new DatagramSocket(port);
            bos=new ByteArrayOutputStream();
            oos=new ObjectOutputStream(new BufferedOutputStream(bos));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	}
	public NetPlayScreen2(World world,Creature player,int screenWidth,int screenHeight,List<String> messages,int score,int level,boolean iswin,boolean ispause){
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
	public NetPlayScreen2(NetPlayScreen tmp){
		score=tmp.getScore();
		level=tmp.getLevel();
		screenWidth = tmp.getScreenWidth();
		screenHeight = tmp.getScreenHeight();
		iswin=tmp.iswin;
		ispause=tmp.ispause;
		messages = tmp.getMessages();
		world=tmp.getWorld();
		player=tmp.getPlayer();
		//???????????????
		CreatureFactory creatureFactory = new CreatureFactory(world);
		createCreatures(creatureFactory);
        socket=tmp.socket;
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


		// if(ispause==false&&key.getKeyCode()==KeyEvent.VK_SPACE)
		// {

        //UDP??????????????????
        //1??????????????????????????????

        PlayScreen tmp=new PlayScreen(world,player,screenWidth,screenHeight,messages,score,level,iswin,ispause);
        //2?????????socket??????
        try {
			socket=new DatagramSocket(port);
            oos.writeObject(tmp);
            oos.flush();
            byte[] datas2=bos.toByteArray();
            DatagramPacket packet2 =new DatagramPacket(datas2,0,datas2.length,new InetSocketAddress("localhost",tarport));
            //????????????
            socket.send(packet2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //????????????
        if(socket!=null)
            socket.close();
		// 
        
		//??????????????????????????????????????????
		world.update();
        System.out.println("?????????????????????");
        //


        //??????,??????????????????
		try {
			socket=new DatagramSocket(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
        byte[] data=new byte[1024*60];
        DatagramPacket packet= new DatagramPacket(data,0,data.length);
        //????????????
        Object r=null;
        try {
            socket.receive(packet);
            //????????????
            byte[] datas=packet.getData();
            int len=packet.getLength();
        
            ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(datas)));
            r=ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
		if(socket!=null)
			socket.close();
        
        System.out.println("?????????????????????");
    
        if(r instanceof PlayScreen)
        {
            NetPlayScreen m=(NetPlayScreen)r;
            this.score=m.getScore();
            this.level=m.getLevel();
            this.screenWidth = m.getScreenWidth();
            this.screenHeight = m.getScreenHeight();
            this.iswin=m.iswin;
            this.ispause=m.ispause;
            this.world=m.getWorld();
            this.player=m.getPlayer();
        }
        world.update();
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
