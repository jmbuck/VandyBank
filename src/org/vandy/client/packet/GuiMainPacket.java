package org.vandy.client.packet;

import org.vandy.client.Customer;
import org.vandy.client.main.MainLogic;

import com.polaris.engine.App;
import com.polaris.engine.thread.AppPacket;
import com.polaris.engine.thread.LogicApp;

public class GuiMainPacket extends AppPacket
{
	
	public GuiMainPacket(App app, LogicApp logic) 
	{
		super(app, logic);
	}

	@Override
	public void handle() 
	{
		logicThread.setLogicHandler(new MainLogic(logicThread));
	}

}
