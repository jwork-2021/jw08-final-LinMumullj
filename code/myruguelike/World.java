package myruguelike;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class World implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Tile[][] tiles;
	private int width;
	public int width() { return width; }
	public int score;
	private int height;
	public int height() { return height; }
	
	private List<Creature> creatures;
	public Items[][] items;
	
	public World(Tile[][] tiles){
		score=0;
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
		this.creatures = new ArrayList<Creature>();
		this.items = new Items[width][height];
	}

	public Creature creature(int x, int y){
		for (Creature c : creatures){
			if (c.x == x && c.y == y)
				return c;
		}
		return null;
	}
	
	public Tile tile(int x, int y){
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Tile.BOUNDS;
		else
			return tiles[x][y];
	}
	public Items item(int x, int y){
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;
		else
			return items[x][y];
	}
	
	public char glyph(int x, int y){
		Creature creature = creature(x, y);
		if (creature != null)
			return creature.glyph();
		
		if (item(x,y) != null)
			return item(x,y).glyph();
		
		return tile(x, y).glyph();
	}
	
	public Color color(int x, int y){
		Creature creature = creature(x, y);
		if (creature != null)
			return creature.color();
		
		if (item(x,y) != null)
			return item(x,y).color();
		
		return tile(x, y).color();
	}

	public void dig(int x, int y) {
		if (tile(x,y).isDiggable())
			tiles[x][y] = Tile.FLOOR;
	}
	
	public void addAtEmptyLocation(Creature creature){
		int x;
		int y;
		
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		} 
		while (!tile(x,y).isGround() || creature(x,y) != null);
		
		creature.x = x;
		creature.y = y;
		creatures.add(creature);
	}
	public void addAtEmptyLocation(Items item){
		int x;
		int y;
		
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		} 
		while (!tile(x,y).isGround() || creature(x,y) != null);
		
		item.x = x;
		item.y = y;
		items[x][y] = item;
	}
	
	public void update(){
		List<Creature> toUpdate = new ArrayList<Creature>(creatures);
		for (Creature creature : toUpdate){
			creature.update();
		}
	}

	public void remove(Creature other) {
		creatures.remove(other);
	}
	public void removeitem(int x, int y) {
		items[x][y] = null;
	}
}
