package org.vandy.client.login;

import com.polaris.engine.Gui;
import com.polaris.engine.LogicGui;
import com.polaris.engine.thread.LogicApp;

public class LoginLogic extends LogicGui
{
	public LoginLogic(LogicApp app)
	{
		super(app);
	}

	@Override
	public Gui getGui() 
	{
		return new LoginGui(this.logic.getApplication());
	}
	
	
	public void update()
	{
		
	}

	public void doSearch()
	{
		
	}
	

}
