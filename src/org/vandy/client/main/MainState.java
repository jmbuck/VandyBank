package org.vandy.client.main;

public class MainState 
{
	
	protected final MainGui mainGui;
	protected double ticks = 0;
	private boolean closing = false;
	
	public MainState(MainGui gui)
	{
		mainGui = gui;
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
