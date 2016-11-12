package org.vandy.client;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private List<Transaction> transactionList;
	private List<Transfer> transferList;
	
	public Account() 
	{
		transactionList = new ArrayList<Transaction>();
		transferList = new ArrayList<Transfer>();
	}
}
