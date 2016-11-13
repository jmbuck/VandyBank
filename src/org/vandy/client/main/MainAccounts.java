package org.vandy.client.main;

import java.util.List;

import org.vandy.client.Account;

public class MainAccounts extends MainState
{
	
	private List<Account> accountList;

	public MainAccounts(MainGui gui, List<Account> list)
	{
		super(gui);
		accountList = list;
	}
	
	public void render(double delta)
	{
		super.render(delta);
	}

}
