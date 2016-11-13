package org.vandy.client;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.opengl.GL11;

import com.polaris.engine.App;
import com.polaris.engine.options.Key;
import com.polaris.engine.render.Draw;

public class LoginGui extends GuiScreen
{

	private Key deleteKey;
	private Key backspaceKey;
	private long nextTimeToDelete = 0;

	private String currentName = "";

	private boolean login = true;
	private boolean createAccount = true;
	private int stage = 0;
	private Customer customer;

	public LoginGui(App app) 
	{
		super(app);
	}

	@Override
	public void init()
	{
		super.init();

		deleteKey = application.getInput().getKey(GLFW.GLFW_KEY_DELETE);
		backspaceKey = application.getInput().getKey(GLFW.GLFW_KEY_BACKSPACE);

		GLFW.glfwSetCharCallback(application.getWindow(), GLFWCharCallback.create((window, codepoint) -> {
			if(Character.isAlphabetic((char) codepoint) || codepoint == 32 || codepoint == ',')
			{
				if(currentName.length() < 48)
					currentName += (char) codepoint;
			}
		}));
	}

	@Override
	public void render(double delta)
	{
		super.render(delta);

		if(login)
		{
			GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1);
			GL11.glBegin(GL11.GL_QUADS);
			Draw.rect(1920 / 2 - 400, 1080 / 2 + 210 * .3f, 1920 / 2 + 400, 1080 / 2 + 220 * .3f, 2);
			GL11.glEnd();
			
			GL11.glColor4f(1, 1, 1, 1);
			boldFont.bind();
			if(currentName.length() == 0)
			{
				String s = "";
				for(int i = 1; i < (ticksExisted * 3 % 4); i++)
				{
					s += " .";
				}
				String name = "Please Type Your Name";
				boldFont.draw(name + s, 1920 / 2 - boldFont.getWidth(name, .3f) / 2, 1080 / 2 + 128 * .3f, 0, .3f);

				if(application.getMouseX() >= 1920 / 2 - 400 && application.getMouseX() <= 1920 / 2 + 400)
				{
					if(application.getMouseY() >= 1080 / 2 + 220 * .3f && application.getMouseY() <= 1080 / 2 + 438 * .3f)
					{
						if(application.getInput().getMouse(0).isPressed())
						{
							login = false;
							stage = 0;
						}
						GL11.glColor4f(.6f, .6f, .6f, 1f);
					}
				}
				boldFont.draw("Create an Account", 1920 / 2 - boldFont.getWidth("Create an Account", .3f) / 2, 1080 / 2 + 388 * .3f, 0, .3f);
			}
			else
			{
				boolean flag = false;
				if((deleteKey.isPressed() && deleteKey.getPressedTime() < 10000000L)
						|| (backspaceKey.isPressed() && backspaceKey.getPressedTime() < 10000000L))
				{
					flag = true;
				}
				else if((deleteKey.isPressed() && deleteKey.getPressedTime() > nextTimeToDelete) 
						|| (backspaceKey.isPressed() && backspaceKey.getPressedTime() > nextTimeToDelete))
				{
					flag = true;
					nextTimeToDelete += 500000000L;
				}
				if(flag)
				{
					currentName = currentName.substring(0, currentName.length() - 1);
				}
				else
				{
					nextTimeToDelete = 500000000L;
				}
				boldFont.draw(currentName, 1920 / 2 - boldFont.getWidth(currentName, .3f) / 2, 1080 / 2 + 128 * .3f, 0, .3f);
			}
			boldFont.unbind();
		}
		else if(createAccount)
		{
			GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1);
			GL11.glBegin(GL11.GL_QUADS);
			Draw.rect(1920 / 2 - 400, 1080 / 2 + 210 * .3f, 1920 / 2 + 400, 1080 / 2 + 220 * .3f, 2);
			GL11.glEnd();
			
			GL11.glColor4f(1, 1, 1, 1);
			
			boldFont.bind();
			String defaultText = "Type or Press Backspace";
			String s = "";
			for(int i = 1; i < (ticksExisted * 3 % 4); i++)
			{
				s += " .";
			}
			boldFont.draw(defaultText + s, 1920 / 2 - boldFont.getWidth(defaultText, .3f) / 2, 1080 / 2 + 128 * .3f, 0, .3f);
			
			float x = 1920 / 2 - 400 + boldFont.getWidth("Cancel", .3f) + 100;
			
			if(application.getMouseX() >= 1920 / 2 - 450 && application.getMouseX() <= x - 50)
			{
				if(application.getMouseY() >= 1080 / 2 + 210 * .3f && application.getMouseY() <= 1080 / 2 + 438 * .3f)
				{
					if(application.getInput().getMouse(0).isPressed())
					{
						login = true;
						stage = 0;
						currentName = "";
					}
					else
					{
						GL11.glColor4f(.6f, .6f, .6f, 1f);
					}
				}
			}
			boldFont.draw("Cancel", 1920 / 2 - 400, 1080 / 2 + 388 * .3f, 0, .3f);
			
			GL11.glColor4f(1f, 1f, 1f, 1f);
			
			if(application.getInput().getKey(GLFW.GLFW_KEY_ENTER).isPressed() && currentName.length() > 0)
			{
				if(stage == 0)
				{
					customer = new Customer();
					customer.setFirst(currentName);
				}
				else if(stage == 1)
				{
					customer.setLast(currentName);
				}
				else if(stage == 2)
				{
					customer.setAddress(currentName);
				}
				else if(stage == 3)
				{
					customer.setCity(currentName);
				}
				else if(stage == 4)
				{
					customer.setState(currentName);
				}
				else
				{
					customer.setZip(currentName);
					Bank.createCustomer(customer);
					Bank.setCurrentCustomer(customer);
				}
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
				boldFont.draw(text, (1920 / 2 + 400 + x - boldFont.getWidth(text, .3f)) / 2, 1080 / 2 + 388 * .3f, 0, .3f);
			
			boldFont.unbind();
		}
		else
		{
			drawLoader();

			GL11.glColor4f(.3f, .3f, .3f, (float) Math.abs(ticksExisted % 12 - 6) / 3f);
			boldFont.bind();
			boldFont.draw("LOADING", 1920 / 2 - boldFont.getWidth("LOADING") / 2, 1280 / 2 + 100, 1, 1);
			boldFont.unbind();	
		}
	}

	private void drawLoader()
	{
		float verticalTimer = (float) Math.abs(ticksExisted % 2 - 1);
		float horizontalTimer = 1 - verticalTimer;
		float multiplier = 5/2;

		verticalTimer *= 20;
		horizontalTimer *= 20;

		GL11.glPushMatrix();
		GL11.glTranslatef(1920 / 2, 1280 / 2, 2);
		GL11.glRotated(ticksExisted * 36, 0, 0, 1);
		GL11.glColor4f(.5f, .5f, .5f, 1f);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(verticalTimer / multiplier, - multiplier * verticalTimer, 0);
		GL11.glVertex3f(-verticalTimer / multiplier, - multiplier * verticalTimer, 0);

		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(verticalTimer / multiplier, multiplier * verticalTimer, 0);
		GL11.glVertex3f(-verticalTimer / multiplier, multiplier * verticalTimer, 0);

		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(horizontalTimer * multiplier, horizontalTimer / multiplier, 0);
		GL11.glVertex3f(horizontalTimer * multiplier, - horizontalTimer / multiplier, 0);

		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(-horizontalTimer * multiplier, horizontalTimer / multiplier, 0);
		GL11.glVertex3f(-horizontalTimer * multiplier, - horizontalTimer / multiplier, 0);
		GL11.glEnd();

		GL11.glPopMatrix();
	}

	public void close()
	{
		font.destroy();
		boldFont.destroy();
		GLFW.glfwSetCharCallback(application.getWindow(), null);
	}

}
