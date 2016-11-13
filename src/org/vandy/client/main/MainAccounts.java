package org.vandy.client.main;

import java.util.List;

import org.vandy.client.Account;
import org.vandy.client.Transaction;

public class MainAccounts extends MainState
{
	
	private List<Account> accountList;
	private Account currentAccount;
	private List<Transaction> transactionList;

	public MainAccounts(MainGui gui, List<Account> list)
	{
		super(gui);
		accountList = list;
	}
	
	public void render(double delta)
	{
		super.render(delta);
		
		Account account;
		double x = 1920 / 32;
		double y = 1080 / 16;
		for(int i = 0; i < accountList.size(); i++)
		{
			account = accountList.get(i);
			font.draw(account.getNickname(), x, y, 0, .5f);
		}
	}

}
