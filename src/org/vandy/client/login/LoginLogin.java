package org.vandy.client.login;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.opengl.GL11;
import org.vandy.client.Bank;
import org.vandy.client.Customer;
import org.vandy.client.Search;
import org.vandy.client.VandyApp;
import org.vandy.client.packet.GuiMainPacket;

import com.polaris.engine.options.Key;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Font;
import com.polaris.engine.render.Texture;
import com.polaris.engine.util.MathHelper;

public class LoginLogin extends LoginState
{

	private Key enterKey;
	private Key deleteKey;
	private Key backspaceKey;

	private String currentText = "";
	private String[] potentials;

	private long nextDeleteTime = 0;

	private double shakeTicks = 0;
	
	private boolean end = false;
	private double endTicks = 0;
	
	private float sideMenuSize = 0;
	private float topTextSize = 0;
	private float barSize = 0;
	
	private Texture accountsTexture;
	private Texture transfersTexture;
	private Texture billsTexture;
	private Texture locationTexture;
	private Texture settingsTexture;
	private Texture exitTexture;

	public LoginLogin(LoginGui gui, Font boldFont)
	{
		super(gui, boldFont);

		deleteKey = loginGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_DELETE);
		backspaceKey = loginGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_BACKSPACE);
		enterKey = loginGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_ENTER);
	}

	public void init()
	{
		GLFW.glfwSetCharCallback(loginGui.getApplication().getWindow(), GLFWCharCallback.create((window, codepoint) -> {
			if(Character.isAlphabetic((char) codepoint) || codepoint == 32 || codepoint == ',')
			{
				if(currentText.length() < 48)
				{
					currentText += (char) codepoint;
					potentials = Search.doSearch(5, currentText);
				}
			}
		}));
		
		accountsTexture = loginGui.getApplication().getTextureManager().genTexture("Accounts", new File("textures/bank.png"));
		transfersTexture = loginGui.getApplication().getTextureManager().genTexture("Transfers", new File("textures/transfer.png"));
		billsTexture = loginGui.getApplication().getTextureManager().genTexture("Bills", new File("textures/bill.png"));
		locationTexture = loginGui.getApplication().getTextureManager().genTexture("Location", new File("textures/location.png"));
		settingsTexture = loginGui.getApplication().getTextureManager().genTexture("Settings", new File("textures/settings.png"));
		exitTexture = loginGui.getApplication().getTextureManager().genTexture("Exit", new File("textures/exit.png"));
	}

	public void render(double delta)
	{
		super.render(delta);
		
		if(end)
		{
			endTicks += delta;
		}

		shakeTicks = Math.max(0, shakeTicks - delta);

		GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1);
		GL11.glBegin(GL11.GL_QUADS);
		float f = Math.max(0, (1 - (float) Math.max(endTicks - .25, 0)));
		Draw.rect(1920 / 2 - 400 * f, 1080 / 2 + 210 * .3f, 1920 / 2 + 400 * f, 1080 / 2 + 220 * .3f, 2);
		GL11.glEnd();

		GL11.glColor4f(1, 1, 1, 1 * (float) (1 - endTicks));
		font.bind();
		if(currentText.length() == 0)
		{
			String s = "";
			for(int i = 1; i < (ticksExisted * 3 % 4); i++)
			{
				s += " .";
			}
			String name = "Please Type Your Name";
			font.draw(name + s, 1920 / 2 - font.getWidth(name, .3f) / 2, 1080 / 2 + 128 * .3f, 0, .3f);

			if(loginGui.getApplication().getMouseX() >= 1920 / 2 - 400 && loginGui.getApplication().getMouseX() <= 1920 / 2 + 400)
			{
				if(loginGui.getApplication().getMouseY() >= 1080 / 2 + 220 * .3f && loginGui.getApplication().getMouseY() <= 1080 / 2 + 438 * .3f)
				{
					if(loginGui.getApplication().getInput().getMouse(0).isPressed())
					{
						loginGui.setState(new LoginCreateAccount(loginGui, font));
					}
					GL11.glColor4f(.6f, .6f, .6f, 1f);
				}
			}
			font.draw("Create an Account", 1920 / 2 - font.getWidth("Create an Account", .3f) / 2, 1080 / 2 + 388 * .3f, 0, .3f);
		}
		else
		{
			boolean flag = false;
			if((deleteKey.isPressed() && deleteKey.getPressedTime() < 10000000L)
					|| (backspaceKey.isPressed() && backspaceKey.getPressedTime() < 10000000L))
			{
				flag = true;
			}
			else if((deleteKey.isPressed() && deleteKey.getPressedTime() > nextDeleteTime) 
					|| (backspaceKey.isPressed() && backspaceKey.getPressedTime() > nextDeleteTime))
			{
				flag = true;
				nextDeleteTime += 500000000L;
			}
			if(flag)
			{
				currentText = currentText.substring(0, currentText.length() - 1);
				potentials = Search.doSearch(5, currentText);
			}
			else
			{
				nextDeleteTime = 500000000L;
			}
			if(enterKey.wasQuickPressed() && currentText.length() > 0)
			{
				enterKey.removeQuickPress();
				Customer customer = Bank.findCustomer(currentText);
				if(customer != null)
				{
					Bank.setCurrentCustomer(customer);
					end = true;
				}
				else
				{
					shakeTicks = .25;
				}
			}
			float shake = 0;
			shake = 10 * (float) (1 - (shakeTicks % (1 / 16d) * 16));
			String s = (potentials[0] != null && potentials[0].length() > currentText.length() ? potentials[0].substring(currentText.length()) : "");
			font.draw(currentText, 1920 / 2 - font.getWidth(currentText + s, .3f) / 2 + shake, 1080 / 2 + 128 * .3f, 0, .3f);
			GL11.glColor4f(.6f, .6f, .6f, 1 - (float) endTicks);
			if(s.length() > 0)
				font.draw(s, 1920 / 2 - font.getWidth(currentText + s, .3f) / 2 + font.getWidth(currentText, .3f) + font.getWidth(s.charAt(0) + "", .3f) / 2 + shake, 1080 / 2 + 128 * .3f, 0, .3f);
			
			float y = 1080 / 2 + 388 * .3f;
			for(int i = 0; i < potentials.length; i++)
			{
				s = potentials[i];
				if(s == null)
					break;
				font.draw(s, 1920 / 2 - font.getWidth(s, .3f) / 2, y, 0, .3f);
				y += 150 * .3f;
			}
		}
		font.unbind();
		
		if(endTicks > .75)
		{
			sideMenuSize = MathHelper.getExpValue(sideMenuSize + .1f, 1920 / 16, .125f, (float) delta);
			topTextSize = MathHelper.getExpValue(topTextSize + .1f, 1080 / 8, .125f, (float) delta);
			barSize = MathHelper.getExpValue(barSize + .1f, 1920 - (1920 / 16 + 8), .125f, (float) delta);
			
			GL11.glColor4f(1, 1, 1, 1);
			font.bind();
			font.draw("Hello Killian,", 1920 / 6 + 40, topTextSize - (1080 / 8 - 1080 / 14), 1, .75f);
			loginGui.getFont().bind();
			loginGui.getFont().draw("You are looking great today!", 1920 / 6 + 40, topTextSize, 1, .5f);
			loginGui.getFont().unbind();
			
			GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1f);
			GL11.glBegin(GL11.GL_QUADS);
			Draw.rect(1920 - barSize, 1080 / 7, 1920 - barSize + (1920 - 8 - 143), 1080 / 7 - 5, -5);
			GL11.glEnd();
			
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
			
			font.unbind();
		}
		
		if(endTicks > 1.25)
		{
			loginGui.getApplication().sendPacket(new GuiMainPacket(loginGui.getApplication(), loginGui.getApplication().getLogicHandler()));
			close();
		}
	}

	public void close()
	{
		GLFW.glfwSetCharCallback(loginGui.getApplication().getWindow(), null);
	}

}
