package net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import javax.xml.crypto.Data;

import myruguelike.screens.PlayScreen;
import net.NetPlayScreen.content;
public class InfoReceive implements Runnable{
    DatagramSocket socket=null;
    private int port;
    NetPlayScreen t;
    public InfoReceive (int port,NetPlayScreen t)
    {
        this.t=t;
        this.port=port;
        try{
            socket=new DatagramSocket(port);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                byte[] data=new byte[1024*60];
                DatagramPacket packet= new DatagramPacket(data,0,data.length);
                //接受包裹
                socket.receive(packet);
                //分析数据
                System.out.println(port+": 收到一个包");
                byte[] datas=packet.getData();
                int len=packet.getLength();
            
                ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(datas)));
                Object r=ois.readObject();
                if(r instanceof content)
                {
                    content tmp=(content)r;
                    t.iswin=tmp.iswin;
                    t.ispause=tmp.ispause;
                    t.world=tmp.world;
                    t.world.items[t.player2.x][t.player2.y]=null;
                    t.world.items[tmp.player.x][tmp.player.y]=t.player2;
                    t.player2.x=tmp.player.x;
                    t.player2.y=tmp.player.y;
                    //t.player2=tmp.player;
                    t.score=tmp.score;
                    t.level=tmp.level;
                }

                // if(r instanceof PlayScreen)
                // {
                //     NetPlayScreen m=(NetPlayScreen)r;
                //     t.score=m.getScore();
                //     t.level=m.getLevel();
                //     t.screenWidth = m.getScreenWidth();
                //     t.screenHeight = m.getScreenHeight();
                //     t.iswin=m.iswin;
                //     t.ispause=m.ispause;
                //     t.world=m.getWorld();
                //     t.player=m.getPlayer();
                // }
                //
                System.out.println(port+": 更新了一次地图");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // if(socket!=null)
        // {
        //     socket.close();
        // }
    }
}