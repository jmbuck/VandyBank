package org.vandy.client.packet;

import org.vandy.client.login.LoginLogic;

import com.polaris.engine.App;
import com.polaris.engine.thread.AppPacket;
import com.polaris.engine.thread.LogicApp;

public class ExecuteSearchPacket extends AppPacket
{

	public ExecuteSearchPacket(App app, LogicApp logic) 
	{
		super(app, logic);
	}

	public void handle()
	{
		if(logicThread.getLogicHandler() instanceof LoginLogic)
		{
			((LoginLogic) logicThread.getLogicHandler()).doSearch();
		}
	}

}
