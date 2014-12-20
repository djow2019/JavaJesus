package ca.javajesus.game.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import ca.javajesus.game.Game;
import ca.javajesus.game.InputHandler;

public class PauseGUI extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	private InputHandler input;
	private int tickCount = 0;
	private boolean canChange = false;
	
	public PauseGUI() {
		this.setFocusable(true);
		try {
			this.image = ImageIO.read(PauseGUI.class
					.getResource("/GUI/Main_Menu_Background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.input = new InputHandler(this);
		this.setBackground(Color.red);
	}
	
	public void tick() {
		if (input.esc.isPressed() && canChange) {
			input.esc.toggle(false);
			canChange = false;
			tickCount = 0;
			Game.removePause();
		}
		tickCount++;
		if (tickCount > 50) {
			canChange = true;
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}
