package org.vandy.client.main;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.opengl.GL11;
import org.vandy.client.Account;
import org.vandy.client.Bank;
import org.vandy.client.Customer;
import org.vandy.client.Search;

import com.polaris.engine.options.Key;

public class MainTransfers extends MainState
{

	private Key enterKey;
	private Key deleteKey;
	private Key backspaceKey;

	private String currentText = "";
	private String[] potentials;
	private int state = 0;

	private long nextDeleteTime = 0;
	private float shakeTicks = 0f;
	private Account account = null;

	public MainTransfers(MainGui gui, boolean alt)
	{
		super(gui, alt);
	}

	public void init()
	{
		super.init();
		
		deleteKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_DELETE);
		backspaceKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_BACKSPACE);
		enterKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_ENTER);

		GLFW.glfwSetCharCallback(mainGui.getApplication().getWindow(), GLFWCharCallback.create((window, codepoint) -> {
			if(Character.isDigit((char) codepoint) || codepoint == 32)
			{
				if(currentText.length() < 32 && state == 0)
				{
					currentText += (char) codepoint;
					potentials = Search.doSearch(5, currentText);
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
				currentText = currentText.substring(0, currentText.length() - 1);
				potentials = Search.doSearch(5, currentText);
			}
			else
			{
				state = 0;
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
			if(state == 0)
			{
				account = Bank.findAccount(currentText);
				if(account != null)
				{
					state = 1;
				}
				else
				{
					shakeTicks = .25f;
				}
			}
		}
		if(state == 0)
		{
			String s;
			font.bind();
			if(currentText.length() > 0)
			{
				float shake = 10 * (float) (1 - (shakeTicks % (1 / 16d) * 16));
				s = (potentials[0] != null && potentials[0].length() > currentText.length() ? potentials[0].substring(currentText.length()) : "");
				font.draw(currentText, 1920 / 2 - font.getWidth(currentText + s, .4f) / 2 + shake, 1080 / 2, 0, .4f);
				GL11.glColor4f(.6f, .6f, .6f, 1f);
				if(s.length() > 0)
					font.draw(s, 1920 / 2 - font.getWidth(currentText + s, .4f) / 2 + font.getWidth(currentText, .4f) + font.getWidth(s.charAt(0) + "", .4f) / 2 + shake, 1080 / 2, 0, .4f);
			}
			else
			{
				s = "Please Type an Account Number";
				font.draw(s, 1920 * 3 / 4 - font.getWidth(s, .4f), 1080 / 2, 0, .4f);
			}
			font.unbind();
		}
		else if(state == 1)
		{
			
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

	}

}
