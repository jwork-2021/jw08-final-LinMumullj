package myruguelike;

import java.awt.Color;
import java.util.Random;
import asciiPanel.AsciiPanel;

public enum Tile{
	// FLOOR((char)250, AsciiPanel.yellow),
	// WALL((char)177, AsciiPanel.red),
	FLOOR((char)249, AsciiPanel.yellow),
	WALL((char)216, AsciiPanel.red),
	BOUNDS('x', AsciiPanel.brightBlack);
	
	public boolean isdiggable;

	private char glyph;
	public char glyph() { return glyph; }
	
	private Color color;
	public Color color() { return color; }
	
	Tile(char glyph, Color color){
		this.glyph = glyph;
		this.color = color;
		this.isdiggable=true;
	}

	public boolean isGround() {
		return this != WALL && this != BOUNDS;
	}

	public boolean isDiggable() {
		return(this == Tile.WALL);
	}
}
