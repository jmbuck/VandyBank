package org.vandy.client.main;

import org.lwjgl.opengl.GL11;

public class MainBills extends MainState
{

	public MainBills(MainGui gui, boolean alt) 
	{
		super(gui, alt);
	}

	public void init()
	{
		super.init();
	}
	
	public void render(double delta)
	{
		super.render(delta);
		
		boldFont.bind();
		boldFont.draw("UNDER CONSTRUCTION!", 1920 / 2 - boldFont.getWidth("UNDER CONSTRUCTION!", .5f) / 2, 1080 / 2, 0, .5f);
		boldFont.unbind();
		
		GL11.glPopMatrix();
	}
	
	public void close()
	{
		super.close();
	}
	
	public void destroy()
	{
		super.destroy();
	}
	
}
