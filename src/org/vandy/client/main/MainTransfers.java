package org.vandy.client.main;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;

import com.polaris.engine.options.Key;

public class MainTransfers extends MainState
{
	
	private Key enterKey;
	private Key deleteKey;
	private Key backspaceKey;
	
	private String currentText = "";
	private int state = 0;
	
	private long nextDeleteTime = 0;

	public MainTransfers(MainGui gui)
	{
		super(gui);
	}
	
	public void init()
	{
		deleteKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_DELETE);
		backspaceKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_BACKSPACE);
		enterKey = mainGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_ENTER);

		GLFW.glfwSetCharCallback(mainGui.getApplication().getWindow(), GLFWCharCallback.create((window, codepoint) -> {
			if(Character.isAlphabetic((char) codepoint) || codepoint == 32 || codepoint == ',')
			{
				if(currentText.length() < 32)
				{
					currentText += (char) codepoint;
				}
			}
		}));
	}
	
	public void render(double delta)
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
			if(currentText.length() > 0)
				currentText = currentText.substring(0, currentText.length() - 1);
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
	}

}
