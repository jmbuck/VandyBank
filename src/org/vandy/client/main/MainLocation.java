package org.vandy.client.main;

import org.lwjgl.opengl.GL11;

public class MainLocation extends MainState
{

	public MainLocation(MainGui gui, boolean alt) 
	{
		super(gui, alt);
	}
	
	public void render(double delta)
	{
		super.render(delta);
		
		boldFont.bind();
		boldFont.draw("UNDER CONSTRUCTION!", 1920 / 2 - boldFont.getWidth("UNDER CONSTRUCTION!", .5f) / 2, 1080 / 2, 0, .5f);
		boldFont.unbind();
		
		GL11.glPopMatrix();
	}

}
