package org.vandy.client.login;

import org.lwjgl.opengl.GL11;
import org.vandy.client.VandyApp;

import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Font;

public class LoginLoading extends LoginState
{

	private double endTicks = 0;
	private boolean end = false;

	public LoginLoading(LoginGui gui, Font font)
	{
		super(gui, font);
	}

	public void render(double delta)
	{
		super.render(delta);
		if(end)
		{
			endTicks += delta;
		}
		drawLoader();

		GL11.glColor4f(.8f, .8f, .8f, (float) Math.abs(ticksExisted % 12 - 6) / 3f * (float) Math.max((1 - endTicks / 1.5), 0));
		font.bind();
		font.draw("LOADING", 1920 / 2 - font.getWidth("LOADING", .5f) / 2, 1080 / 2 + 200, 1, .5f);
		font.unbind();
		if(endTicks > .75f)
		{
			GL11.glColor4f(VandyApp.darkest.x, VandyApp.darkest.y, VandyApp.darkest.z, 1f);
			GL11.glBegin(GL11.GL_QUADS);
			Draw.rect(1920 / 2 - 400 * (float) (endTicks - .75f), 1080 / 2 + 210 * .3f, 1920 / 2 + 400 * (float) (endTicks  - .75f), 1080 / 2 + 220 * .3f, 2);
			GL11.glEnd();
		}
		if(endTicks > 1.25f)
		{
			GL11.glColor4f(1, 1, 1, (float) (endTicks - 1.25f) * 2f);
			font.bind();
			String name = "Please Type Your Name";
			font.draw(name, 1920 / 2 - font.getWidth(name, .3f) / 2, 1080 / 2 + 128 * .3f, 0, .3f);
			font.draw("Create an Account", 1920 / 2 - font.getWidth("Create an Account", .3f) / 2, 1080 / 2 + 388 * .3f, 0, .3f);
			font.unbind();
		}
		if(endTicks > 1.75f)
		{
			loginGui.setState(new LoginLogin(loginGui, font));
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
		GL11.glTranslatef(1920 / 2, 1080 / 2 - 100, 2);
		GL11.glRotated(ticksExisted * 36 * Math.pow(2, 2 * (endTicks)), 0, 0, 1);
		GL11.glColor4f(.8f, .8f, .8f, 1f * (float) Math.max((1 - endTicks / 1.75f), 0));
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

	public void startClose() 
	{
		end = true;
	}
}
