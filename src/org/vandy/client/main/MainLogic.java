package org.vandy.client.main;

import org.vandy.client.login.LoginLogic;

import com.polaris.engine.Gui;
import com.polaris.engine.LogicGui;
import com.polaris.engine.thread.LogicApp;

public class MainLogic extends LogicGui
{
	
	public MainLogic(LogicApp app, LoginLogic parent) 
	{
		super(app, parent);
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
