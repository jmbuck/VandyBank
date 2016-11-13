package org.vandy.client.login;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.opengl.GL11;
import org.vandy.client.Bank;
import org.vandy.client.Customer;
import org.vandy.client.VandyApp;

import com.polaris.engine.options.Key;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Font;

public class LoginCreateAccount extends LoginState
{
	
	private Key enterKey;
	private Key deleteKey;
	private Key backspaceKey;
	
	private Customer customer;
	private String currentText = "";
	
	private int stage = 0;
	
	private long nextDeleteTime = 0;

	public LoginCreateAccount(LoginGui gui, Font font)
	{
		super(gui, font);
		
		deleteKey = loginGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_DELETE);
		backspaceKey = loginGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_BACKSPACE);
		enterKey = loginGui.getApplication().getInput().getKey(GLFW.GLFW_KEY_ENTER);
	}
	
	public void init()
	{
		GLFW.glfwSetCharCallback(loginGui.getApplication().getWindow(), GLFWCharCallback.create((window, codepoint) -> {
			if(Character.isAlphabetic((char) codepoint) || codepoint == 32 || codepoint == ',' || stage == 2 || stage == 5)
			{
				if(currentText.length() < 32)
					currentText += (char) codepoint;
			}
		}));
	}
	
	public void render(double delta)
	{
		super.render(delta);
		GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rect(1920 / 2 - 400, 1080 / 2 + 210 * .3f, 1920 / 2 + 400, 1080 / 2 + 220 * .3f, 2);
		GL11.glEnd();

		GL11.glColor4f(1, 1, 1, 1);

		font.bind();
		
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
			if(currentText.length() == 0)
				stage--;
			else
				currentText = currentText.substring(0, currentText.length() - 1);
			if(stage == -1)
			{
				loginGui.setState(new LoginLogin(loginGui, font));
			}
		}
		else
		{
			nextDeleteTime = 500000000L;
		}
		
		if(currentText.length() == 0)
		{
			String defaultText = "Type or Press Backspace";
			String s = "";
			for(int i = 1; i < (ticksExisted * 3 % 4); i++)
			{
				s += " .";
			}
			font.draw(defaultText + s, 1920 / 2 - font.getWidth(defaultText, .3f) / 2, 1080 / 2 + 128 * .3f, 0, .3f);
		}
		else
		{
			font.draw(currentText, 1920 / 2 - font.getWidth(currentText, .3f) / 2, 1080 / 2 + 128 * .3f, 0, .3f);
		}

		float x = 1920 / 2 - 400 + font.getWidth("Cancel", .3f) + 100;

		if(loginGui.getApplication().getMouseX() >= 1920 / 2 - 450 && loginGui.getApplication().getMouseX() <= x - 50)
		{
			if(loginGui.getApplication().getMouseY() >= 1080 / 2 + 210 * .3f && loginGui.getApplication().getMouseY() <= 1080 / 2 + 438 * .3f)
			{
				if(loginGui.getApplication().getInput().getMouse(0).isPressed())
				{
					loginGui.setState(new LoginLogin(loginGui, font));
				}
				else
				{
					GL11.glColor4f(.6f, .6f, .6f, 1f);
				}
			}
		}
		font.draw("Cancel", 1920 / 2 - 400, 1080 / 2 + 388 * .3f, 0, .3f);

		GL11.glColor4f(1f, 1f, 1f, 1f);

		if(enterKey.wasQuickPressed() && currentText.length() > 0)
		{
			enterKey.removeQuickPress();
			if(stage == 0)
			{
				customer = new Customer();
				customer.setFirst(currentText);
			}
			else if(stage == 1)
			{
				customer.setLast(currentText);
			}
			else if(stage == 2)
			{
				customer.setAddress(currentText);
			}
			else if(stage == 3)
			{
				customer.setCity(currentText);
			}
			else if(stage == 4)
			{
				customer.setState(currentText);
			}
			else
			{
				customer.setZip(currentText);
				//Bank.createCustomer(customer);
				Bank.setCurrentCustomer(customer);
				loginGui.setState(new LoginLogin(loginGui, font));
			}
			currentText = "";
			stage++;
		}

		String text = null;
		if(stage == 0)
		{
			text = "First Name";
		}
		else if(stage == 1)
		{
			text = "Last Name";
		}
		else if(stage == 2)
		{
			text = "Address";
		}
		else if(stage == 3)
		{
			text = "City";
		}
		else if(stage == 4)
		{
			text = "State";
		}
		else if(stage == 5)
		{
			text = "Zip Code";
		}

		if(text != null)
			font.draw(text, (1920 / 2 + 400 + x - font.getWidth(text, .3f)) / 2, 1080 / 2 + 388 * .3f, 0, .3f);

		font.unbind();
	}

}
