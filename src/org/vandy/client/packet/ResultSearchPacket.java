package org.vandy.client.packet;

import org.vandy.client.login.LoginGui;

import com.polaris.engine.App;
import com.polaris.engine.Gui;
import com.polaris.engine.thread.AppPacket;
import com.polaris.engine.thread.LogicApp;

public class ResultSearchPacket extends AppPacket
{
	
	private String[] potentials;

	public ResultSearchPacket(App app, LogicApp logic, String[] array) 
	{
		super(app, logic);
		potentials = array;
	}
	
	public void handle()
	{
		Gui gui = application.getCurrentScreen();
		if(gui instanceof LoginGui)
		{
			((LoginGui) application.getCurrentScreen()).setPotentials(potentials);
		}
	}

}
