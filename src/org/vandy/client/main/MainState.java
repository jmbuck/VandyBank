package org.vandy.client.main;

import org.lwjgl.opengl.GL11;

import com.polaris.engine.render.Font;
import com.polaris.engine.util.MathHelper;

public class MainState 
{
	
	protected Font font;
	protected Font boldFont;
	
	protected final MainGui mainGui;
	protected float ticksExisted = 0;
	protected float ticks = 0;
	private boolean closing = false;
	protected float closingTicks = 0;
	
	protected float mouseX = 0;
	protected float mouseY = 0;
	
	public MainState(MainGui gui)
	{
		mainGui = gui;
		
		font = mainGui.getLight();
		boldFont = mainGui.getBold();
	}
	
	public void init()
	{
		
	}
	
	public void render(double delta)
	{
		if(closing)
		{
			closingTicks = MathHelper.getExpValue(closingTicks, 1, .25f, (float) delta);
			closingTicks = Math.min(closingTicks + .02f, 1f);
		}
		else
		{
			ticks = MathHelper.getExpValue(ticks, 1, .33f, (float) delta);
			ticks = Math.min(ticks + .02f, 1f);
		}
		
		ticksExisted += (float) delta;
		
		mouseX = (float) mainGui.getApplication().getMouseX();
		mouseX -= mainGui.getApplication().getWindowScaleX() * (mainGui.getApplication().getWindowX() / 16 + 16);
		
		mouseY = (float) mainGui.getApplication().getMouseY();
		mouseY -= mainGui.getApplication().getWindowScaleY() * (mainGui.getApplication().getWindowY() / 7 + 16);
		
		mouseX *= 1.08379327647;
		mouseY *= 1.2017167382;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0, (1 - (float) ticks) * -1080f + closingTicks * 1080f, 0);
	}

	public void close() 
	{
		ticks = 1;
		closing = true;
	}

	public void destroy()
	{
		
	}
	
}
