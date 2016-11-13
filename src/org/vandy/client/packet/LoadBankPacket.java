package org.vandy.client.packet;

import org.vandy.client.Bank;

import com.polaris.engine.App;
import com.polaris.engine.thread.AppPacket;
import com.polaris.engine.thread.LogicApp;

public class LoadBankPacket extends AppPacket
{

	public LoadBankPacket(App app, LogicApp logic) 
	{
		super(app, logic);
	}
	
	public void handle()
	{
		Bank.load();
	}

}
