package org.vandy.client.main;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.opengl.GL11;
import org.vandy.client.Account;
import org.vandy.client.Bank;
import org.vandy.client.Transaction;
import org.vandy.client.VandyApp;

import com.polaris.engine.options.Key;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Texture;
import com.polaris.engine.util.MathHelper;

public class MainAccounts extends MainState
{
	private String[] accTypes = {"", "Savings", "Checking", "Credit Card"};
	private String[] transTypes = {"Deposit", "Withdrawl", "Purchase"};

	private Key enterKey;
	private Key deleteKey;
	private Key backspaceKey;
	private float yOffset = 0;

	private List<Account> accountList;
	private Account currentAccount = null;

	private DecimalFormat format = new DecimalFormat("#.##");

	private String currentText = "";

	private int state = 0;
	private int extraStuff = -1;

	private Texture editTexture;
	private Texture newTexture;
	private Texture mergeTexture;
	private Texture savingsTexture;
	private Texture creditTexture;
	private Texture checkTexture;

	private long nextDeleteTime = 0;

	public MainAccounts(MainGui gui, List<Account> list, boolean alt)
	{
		super(gui, alt);
		accountList = list;
	}

	public void init()
	{
		deleteKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_DELETE);
		backspaceKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_BACKSPACE);
		enterKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_ENTER);

		editTexture = mainGui.getApplication().getTextureManager().genTexture("Edit", new File("textures/edit.png"));
		newTexture = mainGui.getApplication().getTextureManager().genTexture("New", new File("textures/new.png"));
		mergeTexture = mainGui.getApplication().getTextureManager().genTexture("Merge", new File("textures/merge.png"));
		savingsTexture = mainGui.getApplication().getTextureManager().genTexture("Savings", new File("textures/savings.png"));
		creditTexture = mainGui.getApplication().getTextureManager().genTexture("Credit", new File("textures/credit.png"));
		checkTexture = mainGui.getApplication().getTextureManager().genTexture("Check", new File("textures/check.png"));

		GLFW.glfwSetCharCallback(mainGui.getApplication().getWindow(), GLFWCharCallback.create((window, codepoint) -> {
			if(Character.isAlphabetic((char) codepoint) || codepoint == 32 || codepoint == ',')
			{
				if(currentText.length() < 16 && (state == 1 || (state == 3 && currentAccount != null)))
				{
					currentText += (char) codepoint;
				}
			}
		}));

	}

	public void render(double delta)
	{
		super.render(delta);

		int hover = checkSpot(0);

		renderIcons(newTexture, mergeTexture, editTexture, 0, hover, 0);

		if(mainGui.getApplication().getInput().getMouse(0).isPressed() && hover > 0)
		{
			state = hover;
			currentAccount = null;
		}

		float x = 1920 / 32;
		float y = 80 + 100 + (state == 1 || (state == 3 && currentAccount != null) ? 100 : 0);

		GL11.glColor4f(.8f, .8f, .8f, 1f);

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
				currentText = currentText.substring(0, currentText.length() - 1);
			else
			{
				state = 0;
				currentText = "";
				extraStuff = -1;
			}
		}
		else
		{
			nextDeleteTime = 500000000L;
		}

		//type, name
		if(state == 1)
		{
			hover = checkSpot(80);

			if(mainGui.getApplication().getInput().getMouse(0).isPressed() && hover > 0)
			{
				extraStuff = hover;
			}

			if(enterKey.wasQuickPressed() && currentText.length() > 0)
			{
				enterKey.removeQuickPress();
				Bank.addAccount(accTypes[extraStuff], currentText);
				currentText = "";
				state = 0;
				extraStuff = -1;
				currentAccount = null;
				y -= 100;
			}

			renderIcons(savingsTexture, checkTexture, creditTexture, 85, hover, extraStuff);

			GL11.glColor4f(1f, 1f, 1f, 1f);
			boldFont.bind();
			if(currentText.length() == 0)
			{
				boldFont.draw("Please Type the Name", 1920 / 3 - 7 - boldFont.getWidth("Please Type the Name", .3f), 210, 0, .3f);
			}
			else
			{
				boldFont.draw(currentText, 1920 / 3 - 7 - boldFont.getWidth(currentText, .3f), 210, 0, .3f);
			}
			boldFont.unbind();
		}
		else if(state == 3 && currentAccount != null)
		{
			if(enterKey.wasQuickPressed() && currentText.length() > 0)
			{
				enterKey.removeQuickPress();
				currentAccount.setNickname(currentText);

				currentText = "";
				state = 0;
				extraStuff = -1;
				currentAccount = null;
				y -= 100;
			}

			GL11.glColor4f(1f, 1f, 1f, 1f);
			boldFont.bind();
			if(currentText.length() == 0)
			{
				boldFont.draw("Please Type the New Name", 1920 / 3 - 7 - boldFont.getWidth("Please Type the New Name", .3f), 130, 0, .3f);
			}
			else
			{
				boldFont.draw(currentText, 1920 / 3 - 7 - boldFont.getWidth(currentText, .3f), 130, 0, .3f);
			}
			boldFont.unbind();
		}
		Account account;
		String s;

		GL11.glColor4f(1, 1, 1, 1);
		for(int i = 0; i < accountList.size(); i++)
		{
			GL11.glColor4f(1f, 1f, 1f, 1f);
			if(i == extraStuff && state > 1)
				GL11.glColor4f(.6f, .6f, .6f, 1);
			if(mouseX >= x - 10 && mouseX <= 1920 / 3 - 2)
			{
				if(mouseY >= y - 128 * .45f && mouseY <= y + 125 - 64 * .45f)
				{
					GL11.glColor4f(.6f, .6f, .6f, 1);
					if(mainGui.getApplication().getInput().getMouse(0).isPressed())
					{
						if(state == 2)
						{
							if(currentAccount == null)
								currentAccount = accountList.get(i);
							else if(currentAccount != accountList.get(i))
							{
								accountList.get(i).merge(currentAccount);
								accountList.remove(currentAccount);
								state = 0;
								extraStuff = -1;
								currentAccount = null;
							}
						}
						else if(state == 3 || state == 0)
						{
							currentAccount = accountList.get(i);
						}
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
			boldFont.draw(s, 1920 / 3 - 12 - boldFont.getWidth(s, .33f), y, 0, .33f);
			y += 50;
			s = "$" + format.format(account.getBalance());
			boldFont.draw(s, 1920 / 3 - 12 - boldFont.getWidth(s, .33f), y, 0, .33f);
			y += 75;
			boldFont.unbind();
		}
		
		x = 1920 / 3 + 12;
		y -= 175 * accountList.size();
		
		y -= yOffset;
		
		if(currentAccount != null)
		{
			yOffset += mainGui.getApplication().getScrollDeltaY() * 4;
			boldFont.bind();
			List<Transaction> list = currentAccount.getTransList();
			yOffset = MathHelper.clamp(0, (5 * 128 * .45f + 40) * list.size(), yOffset);
			Transaction t;
			for(int i = 0; i < list.size(); i++)
			{
				/*if(y < -128 || y > 1080)
				{
					y += (5 * 128 * .45f + 40);
					continue;
				}*/
				t = list.get(i);
				boldFont.draw(transTypes[t.getType()], x, y, 0, .45f);
				y += 128 * .45f + 10;
				s = "$" + format.format(t.getAmount());
				//s = "**** **** **** " + currentAccount.getAccountNumber().substring(12);
				boldFont.draw(s, 1920 * 2 / 3 - 12 - boldFont.getWidth(s, .33f), y, 0, .33f);
				y += 128 * .45f + 10;
				s = t.getTransDate();
				boldFont.draw(s, 1920 * 2/ 3 - 12 - boldFont.getWidth(s, .33f), y, 0, .33f);
				y += 128 * .45f + 10;
				s = t.getStatus();
				boldFont.draw(s, 1920 * 2/ 3 - 12 - boldFont.getWidth(s, .33f), y, 0, .33f);
				y += 128 * .45f + 10;
				s = t.getID();
				boldFont.draw(s, 1920 * 3 / 3 - 12 - boldFont.getWidth(s, .33f), y, 0, .33f);
				y += 128 * .45f + 20;
			}
			boldFont.unbind();
		}
		else
		{
			yOffset = 0;
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1f);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rect(1920 / 3 - 2, 0, 1920 / 3 + 2, 1080, -10);
		if(currentAccount != null)
			Draw.rect(1920 * 2/ 3 - 2, 0, 1920 * 2 / 3 + 2, 1080, -10);
		GL11.glEnd();
		
		if(currentAccount != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(1920 * 5 / 6, 1080 / 2, 0);
			GL11.glRotatef(90, 0, 0, 1);
			boldFont.bind();
			boldFont.draw("UNDER CONSTRUCTION!", 1920 / 2 - boldFont.getWidth("UNDER CONSTRUCTION!", .4f) / 2, 1080 / 2, 0, .4f);
			boldFont.unbind();
			GL11.glPopMatrix();
		}

		GL11.glPopMatrix();
	}

	private int checkSpot(float y)
	{
		if(closingTicks < .01f && ticks > .95f && mouseX >= 1920 / 3 - 2 - 35 - 240 && mouseY >= y)
		{
			if(mouseY <= 80 + y)
			{
				if(mouseX <= 1920 / 3 - 2 - 30 - 160)
				{
					return 3;
				}
				else if(mouseX <= 1920 / 3 - 2 - 115)
				{
					return 2;
				}
				else if(mouseX <= 1920 / 3 - 2 - 5)
				{
					return 1;
				}
			}
		}
		return 0;
	}

	private void renderIcons(Texture tex1, Texture tex2, Texture tex3, float y, int hover, int second)
	{
		if(hover == 1 || second == 1)
			GL11.glColor4f(.6f, .6f, .6f, 1f);
		else
			GL11.glColor4f(.8f, .8f, .8f, 1f);
		tex1.bind();
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(1920 / 3 - 2 - 10 - 80, y, 1920 / 3 - 2 - 10, y + 80, 0);
		GL11.glEnd();

		if(hover == 2 || second == 2)
			GL11.glColor4f(.6f, .6f, .6f, 1f);
		else
			GL11.glColor4f(.8f, .8f, .8f, 1f);
		tex2.bind();
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(1920 / 3 - 2 - 20 - 160, y, 1920 / 3 - 2 - 20 - 80, y + 80, 0);
		GL11.glEnd();

		if(hover == 3 || second == 3)
			GL11.glColor4f(.6f, .6f, .6f, 1f);
		else
			GL11.glColor4f(.8f, .8f, .8f, 1f);
		tex3.bind();
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rectUV(1920 / 3 - 2 - 30 - 240, y, 1920 / 3 - 2 - 30 - 160, y + 80, 0);
		GL11.glEnd();
	}

	public void close()
	{
		super.close();
		GLFW.glfwSetCharCallback(mainGui.getApplication().getWindow(), null);
	}

	public void destroy()
	{		
		editTexture.destroy();
		newTexture.destroy();
		mergeTexture.destroy();
		savingsTexture.destroy();
		creditTexture.destroy();
		checkTexture.destroy();
	}

}
