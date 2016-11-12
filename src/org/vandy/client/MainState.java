package org.vandy.client;

public class MainState 
{
	
	protected final MainGui mainGui;
	protected double ticks = 0;
	
	public MainState(MainGui gui)
	{
		mainGui = gui;
	}
	
	public void render(double delta)
	{
		ticks += delta;
	}

}
