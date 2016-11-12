package org.vandy.client;

import com.polaris.engine.App;
import com.polaris.engine.LogicGui;

public class VandyApp extends App
{

	public static void main(String[] args)
	{
		VandyApp app = new VandyApp(true);
		app.run();
	}
	
	public VandyApp(boolean debug) 
	{
		super(debug);
	}

	@Override
	public long createWindow()
	{
		return this.createWindow(1280, 720, "Vandy", 0);
	}

	@Override
	protected LogicGui getStartGui() 
	{
		return new LoginLogic(this.getLogicHandler());
	}

}
