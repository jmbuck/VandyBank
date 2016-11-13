package org.vandy.client.main;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.vandy.client.Bank;
import org.vandy.client.Customer;
import org.vandy.client.GuiScreen;
import org.vandy.client.VandyApp;

import com.polaris.engine.App;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Font;
import com.polaris.engine.render.Texture;
import com.polaris.engine.util.MathHelper;

public class MainGui extends GuiScreen
{
	
	private Customer customer;

	private MainState prevState;
	private MainState state;

	private Texture accountsTexture;
	private Texture transfersTexture;
	private Texture billsTexture;
	private Texture locationTexture;
	private Texture settingsTexture;
	private Texture exitTexture;

	private float sideMenuSize = 1920 / 16;

	private boolean clicked = false;

	public MainGui(App app) 
	{
		super(app);
		customer = Bank.getCurrentCustomer();
	}

	@Override
	public void init()
	{
		super.init();
		state = new MainAccounts(this, customer.getAccounts());

		accountsTexture = application.getTextureManager().genTexture("Accounts", new File("textures/bank.png"));
		transfersTexture = application.getTextureManager().genTexture("Transfers", new File("textures/transfer.png"));
		billsTexture = application.getTextureManager().genTexture("Bills", new File("textures/bill.png"));
		locationTexture = application.getTextureManager().genTexture("Location", new File("textures/location.png"));
		settingsTexture = application.getTextureManager().genTexture("Settings", new File("textures/settings.png"));
		exitTexture = application.getTextureManager().genTexture("Exit", new File("textures/exit.png"));
	}

	@Override
	public void render(double delta)
	{
		super.render(delta);

		drawTitle();

		drawSideMenu(delta);

		state.render(delta);

		if(application.getInput().getMouse(GLFW.GLFW_MOUSE_BUTTON_LEFT).isPressed() && inScreen)
		{
			if(application.getMouseX() < sideMenuSize + 20 && 
					application.getMouseY() >= 1080 * 4 / 7 - 50 - 300 - 10 && 
					application.getMouseY() < 1080 * 4 / 7 + 60 + 300 && prevState == null)
			{
				prevState = state;
				if(application.getMouseY() < 1080 * 4 / 7 - 40 - 200 && !(state instanceof MainAccounts))
				{
					state = new MainAccounts(this, customer.getAccounts());
				}
				else if(application.getMouseY() < 1080 * 4 / 7 - 20 - 100 && !(state instanceof MainTransfers))
				{
					state = new MainTransfers(this);
				}
				else if(application.getMouseY() < 1080 * 4 / 7 && !(state instanceof MainBills))
				{
					state = new MainBills(this);
				}
				else if(application.getMouseY() < 1080 * 4 / 7 + 20 + 100 && !(state instanceof MainLocation))
				{
					state = new MainLocation(this);
				}
				else if(application.getMouseY() < 1080 * 4 / 7 + 40 + 200 && !(state instanceof MainSettings))
				{
					state = new MainSettings(this);
				}
				else
				{
					application.close();
				}
				if(state == prevState)
				{
					prevState = null;
				}
				else
				{
					prevState.close();
				}
				clicked = true;
			}
		}

	}

	public void drawTitle()
	{
		GL11.glColor4f(1, 1, 1, 1);
		boldFont.bind();
		boldFont.draw("Hello Killian,", 1920 / 6 + 40, 1080 / 14, 1, .75f);
		font.bind();
		font.draw("You are looking great today!", 1920 / 6 + 40, 1080 / 8, 1, .5f);
		font.unbind();

		GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1f);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rect(sideMenuSize + 8, 1080 / 7, 1920 - 8, 1080 / 7 - 5, -5);
		GL11.glEnd();
	}

	public void drawSideMenu(double delta)
	{
		boolean flag = !clicked || application.getMouseX() < 1920 / 16;
		if(flag)
			clicked = false;
		float toValue = application.getMouseX() < sideMenuSize + 20 && inScreen && flag ? 1920 / 6 : 1920 / 16;
		sideMenuSize = MathHelper.getExpValue(sideMenuSize, toValue, .15f, (float) delta);
		sideMenuSize = MathHelper.getLinearValue(sideMenuSize, toValue, 2, (float) delta);

		if(application.getMouseX() < sideMenuSize + 20 && 
				application.getMouseY() >= 1080 * 4 / 7 - 50 - 300 - 10 && 
				application.getMouseY() < 1080 * 4 / 7 + 60 + 300)
		{
			GL11.glColor4f(VandyApp.dark.x, VandyApp.dark.y, VandyApp.dark.z, 1);
			GL11.glBegin(GL11.GL_QUADS);
			if(application.getMouseY() < 1080 * 4 / 7 - 40 - 200)
			{
				Draw.rect(0, 1080 * 4 / 7 - 60 - 300, sideMenuSize, 1080 * 4 / 7 - 40 - 200, 21);
			}
			else if(application.getMouseY() < 1080 * 4 / 7 - 20 - 100)
			{
				Draw.rect(0, 1080 * 4 / 7 - 40 - 200, sideMenuSize, 1080 * 4 / 7 - 20 - 100, 21);
			}
			else if(application.getMouseY() < 1080 * 4 / 7)
			{
				Draw.rect(0, 1080 * 4 / 7 - 20 - 100, sideMenuSize, 1080 * 4 / 7, 21);
			}
			else if(application.getMouseY() < 1080 * 4 / 7 + 20 + 100)
			{
				Draw.rect(0, 1080 * 4 / 7, sideMenuSize, 1080 * 4 / 7 + 20 + 100, 21);
			}
			else if(application.getMouseY() < 1080 * 4 / 7 + 40 + 200)
			{
				Draw.rect(0, 1080 * 4 / 7 + 20 + 100, sideMenuSize, 1080 * 4 / 7 + 40 + 200, 21);
			}
			else
			{
				Draw.rect(0, 1080 * 4 / 7 + 40 + 200, sideMenuSize, 1080 * 4 / 7 + 60 + 300, 21);
			}
			GL11.glEnd();
		}

		GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1f);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rect(0, 0, sideMenuSize, 1080, 20);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		accountsTexture.bind();
		GL11.glColor4f(.9f, .9f, .9f, 1);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(sideMenuSize - 110, 1080 * 4 / 7 - 50 - 300, sideMenuSize - 10, 1080 * 4 / 7 - 50 - 200, 21);
		GL11.glEnd();

		transfersTexture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(sideMenuSize - 110, 1080 * 4 / 7 - 30 - 200, sideMenuSize - 10, 1080 * 4 / 7 - 30 - 100, 21);
		GL11.glEnd();

		billsTexture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(sideMenuSize - 110, 1080 * 4 / 7 - 10 - 100, sideMenuSize - 10, 1080 * 4 / 7 - 10, 21);
		GL11.glEnd();

		locationTexture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(sideMenuSize - 110, 1080 * 4 / 7 + 10, sideMenuSize - 10, 1080 * 4 / 7 + 100 + 10, 21);
		GL11.glEnd();

		settingsTexture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(sideMenuSize - 110, 1080 * 4 / 7 + 30 + 100, sideMenuSize - 10, 1080 * 4 / 7 + 200 + 30, 21);
		GL11.glEnd();

		exitTexture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(sideMenuSize - 110, 1080 * 4 / 7 + 50 + 200, sideMenuSize - 10, 1080 * 4 / 7 + 300 + 50, 21);
		GL11.glEnd();

		boldFont.bind();
		boldFont.draw("Accounts", sideMenuSize - 110 - 100 - boldFont.getWidth("Accounts") * .3f / 2, 1080 * 4 / 7 - 50 - 233, 22, .3f);
		boldFont.draw("Transfers", sideMenuSize - 110 - 100 - boldFont.getWidth("Transfers") * .3f / 2, 1080 * 4 / 7 - 30 - 133, 22, .3f);
		boldFont.draw("Bills", sideMenuSize - 110 - 100 - boldFont.getWidth("Bills") * .3f / 2, 1080 * 4 / 7 - 10 - 33, 22, .3f);
		boldFont.draw("Locations", sideMenuSize - 110 - 100 - boldFont.getWidth("Locations") * .3f / 2, 1080 * 4 / 7 + 10 + 66, 22, .3f);
		boldFont.draw("Settings", sideMenuSize - 110 - 100 - boldFont.getWidth("Settings") * .3f / 2, 1080 * 4 / 7 + 30 + 166, 22, .3f);
		boldFont.draw("Exit", sideMenuSize - 110 - 100 - boldFont.getWidth("Exit") * .3f / 2, 1080 * 4 / 7 + 50 + 266, 22, .3f);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public void close()
	{
		boldFont.destroy();
		font.destroy();
		accountsTexture.destroy();
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
