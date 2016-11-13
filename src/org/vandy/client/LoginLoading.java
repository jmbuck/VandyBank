package org.vandy.client;

import org.lwjgl.opengl.GL11;

import com.polaris.engine.render.Font;

public class LoginLoading extends LoginState
{

	public LoginLoading(LoginGui gui, Font font)
	{
		super(gui, font);
	}
	
	public void render(double delta)
	{
		drawLoader();

		GL11.glColor4f(.3f, .3f, .3f, (float) Math.abs(ticksExisted % 12 - 6) / 3f);
		font.bind();
		font.draw("LOADING", 1920 / 2 - font.getWidth("LOADING") / 2, 1280 / 2 + 100, 1, 1);
		font.unbind();	
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
}
