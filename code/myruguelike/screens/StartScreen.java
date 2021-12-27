package myruguelike.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import net.NetPlayScreen;
import net.NetPlayScreen2;
public class StartScreen implements Screen {

	@Override
	public void displayOutput(AsciiPanel terminal) {
        terminal.writeCenter("WELCOME TO THE", 3,AsciiPanel.brightMagenta);
		terminal.writeCenter(" Grandpa Saves Gourd Babies", 6,AsciiPanel.brightCyan);
        terminal.write((char)2,31,10,AsciiPanel.brightRed);
        terminal.write((char)2,34,10,AsciiPanel.yellow);
        terminal.write((char)2,37,10,AsciiPanel.brightYellow);
        terminal.write((char)2,40,10,AsciiPanel.brightGreen);
        terminal.write((char)2,43,10,AsciiPanel.brightCyan);
        terminal.write((char)2,46,10,AsciiPanel.brightBlue);
        terminal.write((char)2,49,10,AsciiPanel.brightMagenta);
		terminal.writeCenter("--press[enter] to start/[B] to handbook/[Esc] to quit/[L] to continue--", 26);
        terminal.writeCenter("--playerone please press[F]/playerone please press[S]--", 27);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
        switch(key.getKeyCode())
        {
            case KeyEvent.VK_ENTER:
            return new PlayScreen();
            case KeyEvent.VK_B:
            return new TipScreen();
            case KeyEvent.VK_L:
            return new LoadScreen();
            case KeyEvent.VK_F:
            return new NetPlayScreen(6666,7777,8888);
            case KeyEvent.VK_S:
            return new NetPlayScreen(8888,7666,6666);
            case KeyEvent.VK_ESCAPE:
            return null;
            default:
            return this;
        }
	}
}
