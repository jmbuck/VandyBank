package org.vandy.client;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.polaris.engine.App;
import com.polaris.engine.Gui;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Font;

public class MainGui extends Gui
{

	private Font boldFont;
	private Font font;
	
	private List<Infographic> graphics;
	private List<Transaction> transactions;
	
	public MainGui(App app) 
	{
		super(app);
		
		graphics = new ArrayList<Infographic>();
		transactions = new ArrayList<Transaction>();
	}

	@Override
	public void init()
	{
		boldFont = Font.createFont(new File("Hero.ttf"), 128);
		font = Font.createFont(new File("Hero Light.ttf"), 128);
	}
	
	@Override
	public void render(double delta)
	{
		super.render(delta);
		GL11.glEnable(GL11.GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		drawBackground();
		
		drawBars();
		
		drawSideMenu();
		
		GL11.glColor4f(1, 1, 1, 1);
		boldFont.bind();
		boldFont.draw("Hey Killian,", 1920 / 16, 1280 / 14, 1, .75f);
		font.bind();
		font.draw("You are looking great today!", 1920 / 16, 1280 / 8, 1, .5f);
		font.unbind();
		
		
		if(application.getInput().getKey(GLFW.GLFW_KEY_ESCAPE).isPressed())
		{
			application.close();
		}
	}
	
	public void drawBackground()
	{
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(VandyApp.normal.x, VandyApp.normal.y, VandyApp.normal.z, 1f);
		Draw.rect(0, 0, 1920, 1080, -100);
		GL11.glEnd();
	}
	
	public void drawBars()
	{
		GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1f);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rect(1920 / 28, 1280 / 7, 1920 * 3 / 5, 1280 / 7 + 5, -5);
		GL11.glEnd();
	}
	
	public void drawSideMenu()
	{
		GL11.glColor4f(VandyApp.dark.x, VandyApp.dark.y, VandyApp.dark.z, 1f);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rect(0, 0, 1920 / 32, 1280, -10);
		GL11.glEnd();
	}
	
}
