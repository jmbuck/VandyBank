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

public class LoginLogin extends LoginState
{

	private Key enterKey;
	private Key deleteKey;
	private Key backspaceKey;

	private String currentText = "";
	private String[] potentials;

	private long nextDeleteTime = 0;

	private double shakeTicks = 0;

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
					currentText += (char) codepoint;
			}
		}));
	}

	public void render(double delta)
	{
		super.render(delta);

		shakeTicks = Math.max(0, shakeTicks - delta);

		GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1);
		GL11.glBegin(GL11.GL_QUADS);
		Draw.rect(1920 / 2 - 400, 1080 / 2 + 210 * .3f, 1920 / 2 + 400, 1080 / 2 + 220 * .3f, 2);
		GL11.glEnd();

		GL11.glColor4f(1, 1, 1, 1);
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
			}
			else
			{
				nextDeleteTime = 500000000L;
			}
			if(enterKey.wasQuickPressed() && currentText.length() > 0)
			{
				enterKey.removeQuickPress();
				Customer customer = Bank.findCustomer(currentText);
				if(customer == null)
				{
					//Bank.setCurrentCustomer(customer);
					loginGui.setState(new LoginLoading(loginGui, font));
				}
				else
				{
					shakeTicks = .25;
				}
			}
			float shake = 0;
			shake = 10 * (float) (1 - (shakeTicks % (1 / 16d) * 16));
			font.draw(currentText, 1920 / 2 - font.getWidth(currentText, .3f) / 2 + shake, 1080 / 2 + 128 * .3f, 0, .3f);
		}
		font.unbind();
	}

	public void close()
	{
		GLFW.glfwSetCharCallback(loginGui.getApplication().getWindow(), null);
	}

}
