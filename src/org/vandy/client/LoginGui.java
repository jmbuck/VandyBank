package org.vandy.client;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.opengl.GL11;

import com.polaris.engine.App;
import com.polaris.engine.options.Key;
import com.polaris.engine.render.Draw;

public class LoginGui extends GuiScreen
{

	private LoginState state;
	
	private Key deleteKey;
	private Key backspaceKey;
	private Key enterKey;
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
		
		state = new LoginLogin(this, boldFont);
	}

	@Override
	public void render(double delta)
	{
		super.render(delta);

		state.render(delta);
		
		if(login)
		{

		}
		else if(createAccount)
		{
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

	public void setState(LoginState newState) 
	{
		state.close();
		
		state = newState;
	}

}
