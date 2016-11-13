package org.vandy.client.packet;

import org.vandy.client.login.LoginGui;
import org.vandy.client.login.LoginLoading;

import com.polaris.engine.App;
import com.polaris.engine.thread.AppPacket;
import com.polaris.engine.thread.LogicApp;

public class FinishLoadBankPacket extends AppPacket
{

	public FinishLoadBankPacket(App app, LogicApp logic)
	{
		super(app, logic);
	}

	@Override
	public void handle()
	{
		((LoginLoading)((LoginGui) application.getCurrentScreen()).getState()).startClose();
	}

}
