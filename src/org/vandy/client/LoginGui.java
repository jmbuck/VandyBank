package org.vandy.client;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.polaris.engine.App;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Font;

public class LoginGui extends GuiScreen
{

	private Font playRegular;
	private float nextScreenAlpha = 1;
	
	public LoginGui(App app) 
	{
		super(app);
	}
	
	@Override
	public void init()
	{
		playRegular = Font.createFont(new File("Play-Regular.ttf"), 64);
	}
	
	public void close()
	{
		playRegular.destroy();
	}
	
	@Override
	public void render(double delta)
	{
		super.render(delta);
		GL11.glEnable(GL11.GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(.6f, .6f, 1, 1f);
		Draw.rect(0, 0, 1920, 1080, -100);
		GL11.glEnd();
		
		drawLoader();
		
		GL11.glColor4f(.3f, .3f, .3f, (float) Math.abs(ticksExisted % 12 - 6) / 3f);
		playRegular.bind();
		playRegular.draw("LOADING", 1920 / 2 - playRegular.getWidth("LOADING") / 2, 1280 / 2 + 100, 1, 1);
		playRegular.unbind();
		
		if(application.getInput().getKey(GLFW.GLFW_KEY_ESCAPE).isPressed())
		{
			application.close();
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
		GL11.glColor4f(.5f, .5f, .5f, 1f * nextScreenAlpha);
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

}
