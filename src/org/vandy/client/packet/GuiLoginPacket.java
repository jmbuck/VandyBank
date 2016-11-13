package org.vandy.client.packet;

import org.vandy.client.login.LoginLogic;

import com.polaris.engine.App;
import com.polaris.engine.thread.AppPacket;
import com.polaris.engine.thread.LogicApp;

public class GuiLoginPacket extends AppPacket
{
	private LoginLogic loginLogic;

	public GuiLoginPacket(App app, LogicApp logic, LoginLogic login) 
	{
		super(app, logic);
		loginLogic = login;
	}

	@Override
	public void handle() 
	{
		logicThread.setLogicHandler(loginLogic);
	}

}
