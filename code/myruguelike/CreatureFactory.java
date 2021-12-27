package myruguelike;

import java.util.List;

import asciiPanel.AsciiPanel;
import myruguelike.screens.PlayScreen;
import java.io.Serializable;
public class CreatureFactory implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private World world;
	
	public CreatureFactory(World world){
		this.world = world;
	}
	
	public Creature newPlayer(List<String> messages,PlayScreen plsc){
		Creature player = new Creature(world, (char)2, AsciiPanel.brightCyan, 100, 20, 5,plsc);
		world.addAtEmptyLocation(player);
		new PlayerAi(player, messages, plsc);
		return player;
	}
	//net
	public Creature newPlayer2(PlayScreen plsc){
		Creature player2 = new Creature(world, (char)2, AsciiPanel.brightWhite, 100, 20, 5,plsc);
		world.addAtEmptyLocation(player2);
		return player2;
	}
	public Creature newBat(PlayScreen plsc){
		Creature bat = new Creature(world, (char)157, AsciiPanel.brightRed, 100, 5, 0,plsc);
		world.addAtEmptyLocation(bat);
		//Thread t=new Thread(new BatAi(bat, plsc));
		//t.start();
		new BatAi(bat, plsc);
		return bat;
	}

	public Creature newEBat(Creature player,PlayScreen plsc){
		Creature bat = new Creature(world, (char)157, AsciiPanel.brightMagenta, 150, 10, 0,plsc);
		world.addAtEmptyLocation(bat);
		//Thread t=new Thread(new EBatAi(bat, player, plsc));
		//t.start();
		new EBatAi(bat, player, plsc);
		return bat;
	}

	public Items newHeart(){
		Items item = Items.HEART;
		//Items item = new Items((char)3, AsciiPanel.brightGreen, "heart" ,10);
		world.addAtEmptyLocation(item);
		return item;
	}

	public Items newGourd(){
		Items item = Items.GOURD;
		//Items item = new Items((char)2, AsciiPanel.brightMagenta, "gourd1" ,10);
		world.addAtEmptyLocation(item);
		return item;
	}

	public Items newPlayer2(){
		Items item = Items.PLAYER2;
		//Items item = new Items((char)1, AsciiPanel.brightMagenta, "gourd1" ,10);
		world.addAtEmptyLocation(item);
		return item;
	}
}
