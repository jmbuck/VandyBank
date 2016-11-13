package org.vandy.client;

import java.util.*;

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
		numAccounts = (int)(Math.random()*4) + 1;
		addRandomAccounts();
		numDeposits = (int)(Math.random()*15) + 6;
		numWithdrawals = (int)(Math.random()*5) + 6;
		numPurchases = (int)(Math.random()*5) + 6;
		numBills = (int)(Math.random()*3) + 4;
	}
	
	public void addRandomAccounts() throws Exception
	{
		List<String> types = new ArrayList<String>();
		types.add("Credit Card"); types.add("Checking"); types.add("Savings");
		for(int i = 0; i < numAccounts; i++)
		{
			randy.addAccount(new Account(Integer.toString(1001+i), "1337", types.get((int)(Math.random()*4)), "rando", 0.00, Math.round(Math.random()*1000 + 1000), Integer.toString(7523+i)));
		}
		
	}

}
