package org.vandy.client.main;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.opengl.GL11;
import org.vandy.client.Account;
import org.vandy.client.Bank;
import org.vandy.client.Search;
import org.vandy.client.Transaction;

import com.polaris.engine.options.Key;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Texture;

public class MainTransfers extends MainState
{
	
	private List<Account> accountList;

	private Key enterKey;
	private Key deleteKey;
	private Key backspaceKey;

	private String currentText = "";
	private String[] potentials;
	private int state = 0;

	private long nextDeleteTime = 0;
	private float shakeTicks = 0f;
	private Account mainAccount = null;
	private Account account = null;

	private DecimalFormat format = new DecimalFormat("#.##");
	private int index = 0;
	
	private Texture savingsTexture;
	private Texture creditTexture;
	private Texture checkTexture;

	public MainTransfers(MainGui gui, boolean alt, List<Account> list)
	{
		super(gui, alt);
		accountList = list;
	}

	public void init()
	{
		super.init();
		
		deleteKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_DELETE);
		backspaceKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_BACKSPACE);
		enterKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_ENTER);
		
		savingsTexture = mainGui.getApplication().getTextureManager().genTexture("Savings", new File("textures/savings.png"));
		creditTexture = mainGui.getApplication().getTextureManager().genTexture("Credit", new File("textures/credit.png"));
		checkTexture = mainGui.getApplication().getTextureManager().genTexture("Check", new File("textures/check.png"));

		GLFW.glfwSetCharCallback(mainGui.getApplication().getWindow(), GLFWCharCallback.create((window, codepoint) -> {
			if(Character.isDigit((char) codepoint) || codepoint == '.')
			{
				if(currentText.length() < 16)
				{
					if(codepoint == '.' && index == 0)
					{
						index = currentText.length() + 1;
						currentText += (char) codepoint;
					}
					else if(index > 0 && index + 1 <= currentText.length())
					{
						currentText += (char) codepoint;
					}
					else if(index == 0)
					{
						currentText += (char) codepoint;
					}
					if(state == 1)
						potentials = Search.doSearchAcctNum(5, currentText);
				}
			}
		}));
	}

	public void render(double delta)
	{
		super.render(delta);
		
		shakeTicks = (float) Math.max(0, shakeTicks - delta);
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
			if(currentText.length() > 0)
			{
				if(index == currentText.length())
					index = 0;
				currentText = currentText.substring(0, currentText.length() - 1);
				potentials = Search.doSearchAcctNum(5, currentText);
			}
			else
			{
				state = Math.min(0, state - 1);
				currentText = "";
			}
		}
		else
		{
			nextDeleteTime = 500000000L;
		}
		if(enterKey.wasQuickPressed() && currentText.length() > 0)
		{
			enterKey.removeQuickPress();
			if(state == 1)
			{
				account = Bank.findAccount(currentText);
				if(account != null)
				{
					state = 2;
					currentText = "";
				}
				else
				{
					shakeTicks = .25f;
				}
			}
			else if(state == 2)
			{
				if(mainAccount.transferTo(currentText, account) == 0)
				{
					shakeTicks = .25f;
				}
			}
		}
		
		float y = 80;
		float x = 1920 / 2 - 200;
		String s;

		if(state == 0)
		{
			GL11.glColor4f(1, 1, 1, 1);
			for(int i = 0; i < accountList.size(); i++)
			{
				GL11.glColor4f(1f, 1f, 1f, 1f);
				if(mouseX >= 1920 / 2 - 200 && mouseX <= 1920 / 2 + 200)
				{
					if(mouseY >= y - 128 * .45f && mouseY <= y + 125 - 64 * .45f)
					{
						GL11.glColor4f(.6f, .6f, .6f, 1);
						if(mainGui.getApplication().getInput().getMouse(0).isPressed())
						{
							mainAccount = accountList.get(i);
							state = 1;
						}
					}
				}
				account = accountList.get(i);
				if(account.getType().equals("Savings"))
				{
					savingsTexture.bind();
				}
				else if(account.getType().equals("Checking"))
				{
					checkTexture.bind();
				}
				else
				{
					creditTexture.bind();
				}
				GL11.glBegin(GL11.GL_QUADS);
				Draw.rectUV(x - 10, y - 60, x + 50, y, 1);
				GL11.glEnd();

				boldFont.bind();
				boldFont.draw(account.getNickname(), x + 52, y, 0, .45f);
				y += 50;
				s = "**** **** **** " + account.getAccountNumber().substring(12);
				boldFont.draw(s, x + 200 - 12 - boldFont.getWidth(s, .33f) / 2, y, 0, .33f);
				y += 50;
				s = "$" + format.format(account.getBalance());
				boldFont.draw(s, x + 200 - 12 - boldFont.getWidth(s, .33f) / 2, y, 0, .33f);
				y += 75;
				boldFont.unbind();
			}
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		else if(state == 1)
		{
			font.bind();
			GL11.glColor4f(1, 1, 1, 1);
			if(currentText.length() > 0)
			{
				y = 1080 / 2;
				float shake = 10 * (float) (1 - (shakeTicks % (1 / 16d) * 16));
				s = (potentials[0] != null && potentials[0].length() > currentText.length() ? potentials[0].substring(currentText.length()) : "");
				font.draw(currentText, 1920 / 2 - font.getWidth(currentText + s, .6f) / 2 + shake, y, 0, .6f);
				GL11.glColor4f(.6f, .6f, .6f, 1f);
				if(s.length() > 0)
					font.draw(s, 1920 / 2 - font.getWidth(currentText + s, .6f) / 2 + font.getWidth(currentText, .6f) + font.getWidth(s.charAt(0) + "", .6f) / 2 + shake, y, 0, .6f);
				for(int i = 0; i < potentials.length; i++)
				{
					y += 128 * .4f + 10;
					font.draw(potentials[i], 1920 / 2 - font.getWidth(potentials[i], .6f) / 2, y, 0, .6f);
				}
			}
			else
			{
				s = "Please Type an Account Number";
				font.draw(s, 1920 / 2 - font.getWidth(s, .6f) / 2, 1080 / 2, 0, .6f);
			}
			font.unbind();
		}
		else if(state == 2)
		{
			font.bind();
			GL11.glColor4f(1, 1, 1, 1);
			if(currentText.length() > 0)
			{
				font.draw(currentText, 1920 / 2 - font.getWidth(currentText, .6f) / 2, 1080 / 2, 0, .6f);
			}
			else
			{
				s = "Please Type the Amount You're Sending";
				font.draw(s, 1920 / 2- font.getWidth(s, .6f) / 2, 1080 / 2, 0, .6f);
			}
			font.unbind();
		}
		GL11.glPopMatrix();
	}

	public void close()
	{
		super.close();
		GLFW.glfwSetCharCallback(mainGui.getApplication().getWindow(), null);
	}
	
	public void destroy()
	{
		super.destroy();
		
		savingsTexture.destroy();
		creditTexture.destroy();
		checkTexture.destroy();
	}

}
