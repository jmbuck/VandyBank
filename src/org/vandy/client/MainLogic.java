package org.vandy.client;

import com.polaris.engine.Gui;
import com.polaris.engine.LogicGui;
import com.polaris.engine.thread.LogicApp;

public class MainLogic extends LogicGui
{

	public MainLogic(LogicApp app) 
	{
		super(app);
	}

	@Override
	public Gui getGui() 
	{
		return new MainGui(this.logic.getApplication());
	}
	
	public void update()
	{
		
	}

}
