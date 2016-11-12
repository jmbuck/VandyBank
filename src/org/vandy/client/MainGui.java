package org.vandy.client;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;

import java.io.File;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.polaris.engine.App;
import com.polaris.engine.Gui;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Font;
import com.polaris.engine.render.Texture;
import com.polaris.engine.util.MathHelper;

public class MainGui extends Gui
{

	private Font boldFont;
	private Font font;

	private MainState state;

	private Texture accountsTexture;
	
	private float sideMenuSize = 1920 / 32;

	private List<Transaction> transactions;

	public MainGui(App app) 
	{
		super(app);

		state = new MainState(this);
	}

	@Override
	public void init()
	{
		boldFont = Font.createFont(new File("Hero.ttf"), 128);
		font = Font.createFont(new File("Hero Light.ttf"), 128);
		
		accountsTexture = application.getTextureManager().genTexture("Accounts", new File("textures/bank.png"));
	}

	@Override
	public void render(double delta)
	{
		super.render(delta);
		GL11.glEnable(GL11.GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		drawBackground();

		drawTitle();

		drawSideMenu(delta);

		state.render(delta);

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

	public void drawTitle()
	{
		GL11.glColor4f(1, 1, 1, 1);
		boldFont.bind();
		boldFont.draw("Hey Killian,", 1920 / 7, 1280 / 14, 1, .75f);
		font.bind();
		font.draw("You are looking great today!", 1920 / 7, 1280 / 8, 1, .5f);
		font.unbind();

		GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1f);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rect(sideMenuSize + 8, 1280 / 7, 1920 - 8, 1280 / 7 + 5, -5);
		GL11.glEnd();
	}

	public void drawSideMenu(double delta)
	{
		if(!MathHelper.isEqual(application.getMouseX(), 0f))
		{
			float toValue = application.getMouseX() < sideMenuSize + 20 ? 1920 / 8 : 1920 / 32;
			sideMenuSize = MathHelper.getExpValue(sideMenuSize, toValue, .15f, (float) delta);
			sideMenuSize = MathHelper.getLinearValue(sideMenuSize, toValue, 2, (float) delta);
		}

		GL11.glColor4f(VandyApp.dark.x, VandyApp.dark.y, VandyApp.dark.z, 1f);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rect(0, 0, sideMenuSize, 1280, 20);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		accountsTexture.bind();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(0, 0, 100, 100, 21);
		Draw.rectUV(sideMenuSize - (1920 / 32 - 10), 1280 / 4 - 20, sideMenuSize - 10, 1280 / 4 + 20, 21);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	public void close()
	{
		boldFont.destroy();
		font.destroy();
		accountsTexture.destroy();
	}

	public Transaction getTransaction(int index)
	{
		return transactions.get(index);
	}

	public Font getBold()
	{
		return boldFont;
	}

	public Font getLight()
	{
		return font;
	}
}
