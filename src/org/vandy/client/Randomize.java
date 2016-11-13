package org.vandy.client;

import java.util.*;

import com.polaris.engine.util.MathHelper;

public class Randomize {
	private int numAccounts;
	private int numDeposits;
	private int numWithdrawals;
	private int numPurchases;
	private int numBills;
	private List<Account> accounts = new ArrayList<Account>();
	private List<Transaction> transList = new ArrayList<Transaction>();
	private List<Deposit> depList = new ArrayList<Deposit>();
	private List<Withdrawal> withList = new ArrayList<Withdrawal>();
	Customer randy;
	Account account;
	
	public Randomize() throws Exception
	{
		numAccounts = MathHelper.random(1, 4);
		addRandomAccounts();
		numDeposits = MathHelper.random(15, 20);
		numWithdrawals = MathHelper.random(10, 15);
		numPurchases = MathHelper.random(5, 10);
		numBills = MathHelper.random(3, 6);
	}
	
	public void addRandomAccounts() throws Exception
	{
		List<String> types = new ArrayList<String>();
		types.add("Credit Card"); types.add("Checking"); types.add("Savings");
		for(int i = 0; i < numAccounts; i++)
		{
			randy.addAccount(new Account(Integer.toString(1001+i), "1337", types.get((int)(Math.random()*4)), "rando", 0.00, Math.round((Math.random()*1000 + 1000)*100)/100, Integer.toString(7523+i)));
		}
		
	}

}
