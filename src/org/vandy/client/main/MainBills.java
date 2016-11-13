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
