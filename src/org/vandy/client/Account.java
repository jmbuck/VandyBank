package org.vandy.client;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private List<Transfer> transferList;
	private List<Bill> billList;
	private List<Deposit> depList;
	private List<Withdrawal> withList;
	private String id;
	
	public Account(String custID, String type, String nickname, int rewards, int balance,
					String acctNum) throws Exception 
	{	
		String[] accounts = CapitalHttpClient.getAccountsByCustomer(custID);
		if(accounts.length != 0) {
			for(String s : accounts) {
				
			}
		}
		id = CapitalHttpClient.postAccount(custID, type, nickname, rewards, balance, acctNum);
		
		transferList = new ArrayList<Transfer>();
		billList = new ArrayList<Bill>();
		depList = new ArrayList<Deposit>();
		withList = new ArrayList<Withdrawal>();
	}
}
