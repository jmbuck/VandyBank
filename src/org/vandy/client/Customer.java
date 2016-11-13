package org.vandy.client;

import java.util.ArrayList;
import java.util.List;

public class Customer 
{
	
	private ArrayList<Account> accountList;
	private String id;
	
	public Customer(String id, String first, String last, String streetNum, 
			String streetName, String city, String state, String zip) throws Exception
	{
		id = CapitalHttpClient.postCustomer(first, last, streetNum, streetName, city, state, zip);
		accountList = new ArrayList<Account>();
	}
	
	public void addAccount(Account newAcc) {
		accountList.add(newAcc);
	}
	
	public ArrayList<Account> getAccounts() {
		return accountList;
	}
	
	public void deleteAccount(Account acc) {
		accountList.remove(acc);
	}
	
	
}
