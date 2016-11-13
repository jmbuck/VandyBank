package org.vandy.client;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.polaris.engine.App;
import com.polaris.engine.Gui;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Font;
import com.polaris.engine.util.MathHelper;

public class GuiScreen extends Gui
{
	
	protected float minimizeSize = 0;
	protected float closeSize = 0;
	
	protected Font boldFont;
	protected Font font;

	protected boolean inScreen = true;

	public GuiScreen(App app) 
	{
		super(app);
	}

	@Override
	public void init()
	{
		boldFont = Font.createFont(new File("Hero.ttf"), 128);
		font = Font.createFont(new File("Hero Light.ttf"), 128);
	}
	
	public void render(double delta)
	{
		super.render(delta);
		GL11.glEnable(GL11.GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		double[] xpos = new double[1];
		double[] ypos = new double[1];
		GLFW.glfwGetCursorPos(application.getWindow(), xpos, ypos);
		inScreen = xpos[0] >= 0 && ypos[0] >= 0 && xpos[0] <= 1920 / application.getWindowScaleX() && ypos[0] <= 1080 / application.getWindowScaleY();
		
		drawBackground();
		
		if(application.getInput().getMouse(GLFW.GLFW_MOUSE_BUTTON_LEFT).isPressed() && inScreen)
		{
			if(application.getMouseX() >= 1840 && application.getMouseY() <= 40)
			{
				if(application.getMouseX() < 1880)
				{
					GLFW.glfwIconifyWindow(application.getWindow());
				}
				else
				{
					application.close();
				}
			}
		}
		
		if(application.getInput().getKey(GLFW.GLFW_KEY_ESCAPE).isPressed())
		{
			application.close();
		}
		
		if(inScreen && application.getMouseX() > 1840 && application.getMouseY() < 40)
		{
			if(application.getMouseX() <= 1880)
			{
				minimizeSize = (minimizeSize + (float) delta) % 1f;
				closeSize = 0;
			}
			else
			{
				closeSize = (closeSize + (float) delta) % .75f;
				minimizeSize = 0;
			}
		}
		else
		{
			minimizeSize = closeSize = 0;
		}

		float f = .5f - Math.abs(.5f - closeSize);

		f *= 2;

		GL11.glColor4f(.9f, .9f, .9f, 1);
		GL11.glBegin(GL11.GL_QUADS);
		if(minimizeSize > 0.001 && minimizeSize < .5f)
		{
			GL11.glVertex3f(1880 - 32 + MathHelper.random(1.2f), 32 - MathHelper.random(1.2f), 100);
			GL11.glVertex3f(1880 - 8 - MathHelper.random(1.2f), 32 - MathHelper.random(1.2f), 100);
			GL11.glVertex3f(1880 - 8 - MathHelper.random(1.2f), 8 + (minimizeSize * 48) + MathHelper.random(1.2f) - .6f, 100);
			GL11.glVertex3f(1880 - 32 + MathHelper.random(1.2f), 8 + (minimizeSize * 48) + MathHelper.random(1.2f) - .6f, 100);
		}
		else if(minimizeSize < 0.001)
		{
			Draw.rect(1880 - 32, 8, 1880 - 8, 32, 100);
		}

		GL11.glVertex3f(1900 - 12 - 3 * f, 20 + 12 + 3 * f, 100);
		GL11.glVertex3f(1900 - 12 + 3 / 2f + f / 2f, 20 + 12 + 3 * f, 100);
		GL11.glVertex3f(1900 + 12 + 3 * f, 20 - 12 + 3 / 2f + f / 2f, 100);
		GL11.glVertex3f(1900 + 12 + 3 * f, 20 - 12 - 3 * f, 100);

		GL11.glVertex3f(1900 - 12 - 3 * f, 20 + 12 - 3 / 2f - f / 2f, 100);
		GL11.glVertex3f(1900 - 12 - 3 * f, 20 + 12 + 3 * f, 100);
		GL11.glVertex3f(1900 + 12 + 3 * f, 20 - 12 - 3 * f, 100);
		GL11.glVertex3f(1900 + 12 - 3 / 2f - f / 2f, 20 - 12 - 3 * f, 100);

		GL11.glVertex3f(1900 - 12 - 3 * f, 20 - 12 + 3 / 2f + f / 2f, 100);
		GL11.glVertex3f(1900 - 12 - 3 * f, 20 - 12 - 3 * f, 100);
		GL11.glVertex3f(1900 + 12 + 3 * f, 20 + 12 + 3 * f, 100);
		GL11.glVertex3f(1900 + 12 - 3 / 2f - f / 2f, 20 + 12 + 3 * f, 100);

		GL11.glVertex3f(1900 - 12 - 3 * f, 20 - 12 - 3 * f, 100);
		GL11.glVertex3f(1900 - 12 + 3 / 2f + f / 2f, 20 - 12 - 3 * f, 100);
		GL11.glVertex3f(1900 + 12 + 3 * f, 20 + 12 - 3 / 2f - f / 2f, 100);
		GL11.glVertex3f(1900 + 12 + 3 * f, 20 + 12 + 3 * f, 100);

		GL11.glEnd();
	}
	
	public void drawBackground()
	{
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(VandyApp.fontDarkest.x, VandyApp.fontDarkest.y, VandyApp.fontDarkest.z, 1f);
		Draw.rect(0, 0, 1920, 1080, -100);
		GL11.glEnd();
	}

}
