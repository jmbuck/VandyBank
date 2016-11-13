package org.vandy.client.main;

import com.polaris.engine.render.Font;

public class MainState 
{
	
	protected Font font;
	protected Font boldFont;
	
	protected final MainGui mainGui;
	protected double ticks = 0;
	private boolean closing = false;
	
	public MainState(MainGui gui)
	{
		mainGui = gui;
		
		font = mainGui.getLight();
		boldFont = mainGui.getBold();
	}
	
	public void render(double delta)
	{
		if(closing)
			ticks -= delta;
		else
			ticks += delta;
	}

	public void close() 
	{
		ticks = 1;
		closing = true;
	}

}
