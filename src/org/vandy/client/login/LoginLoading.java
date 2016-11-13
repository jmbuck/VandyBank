package org.vandy.client.login;

import java.io.File;

import org.lwjgl.opengl.GL11;

import com.polaris.engine.render.Font;
import com.polaris.engine.render.Texture;

public class LoginLoading extends LoginState
{
	
	private Texture accountsTexture;
	private Texture transfersTexture;
	private Texture billsTexture;
	private Texture locationTexture;
	private Texture settingsTexture;
	private Texture exitTexture;

	public LoginLoading(LoginGui gui, Font font)
	{
		super(gui, font);
	}
	
	public void init()
	{
		accountsTexture = loginGui.getApplication().getTextureManager().genTexture("Accounts", new File("textures/bank.png"));
		transfersTexture = loginGui.getApplication().getTextureManager().genTexture("Transfers", new File("textures/transfer.png"));
		billsTexture = loginGui.getApplication().getTextureManager().genTexture("Bills", new File("textures/bill.png"));
		locationTexture = loginGui.getApplication().getTextureManager().genTexture("Location", new File("textures/location.png"));
		settingsTexture = loginGui.getApplication().getTextureManager().genTexture("Settings", new File("textures/settings.png"));
		exitTexture = loginGui.getApplication().getTextureManager().genTexture("Exit", new File("textures/exit.png"));
	}
	
	public void render(double delta)
	{
		super.render(delta);
		drawLoader();

		GL11.glColor4f(.8f, .8f, .8f, (float) Math.abs(ticksExisted % 12 - 6) / 3f);
		font.bind();
		font.draw("LOADING", 1920 / 2 - font.getWidth("LOADING", .5f) / 2, 1080 / 2 + 200, 1, .5f);
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
		GL11.glTranslatef(1920 / 2, 1080 / 2 - 100, 2);
		GL11.glRotated(ticksExisted * 36, 0, 0, 1);
		GL11.glColor4f(.8f, .8f, .8f, 1f);
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
		
	}
}
