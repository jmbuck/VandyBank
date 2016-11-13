package org.vandy.client;

import java.util.*;

public class Banking {
	
	private List<Customer> customerList = new ArrayList<Customer>();
	private List<Account> accountList = new ArrayList<Account>();
	
	public Banking() throws Exception {
		loadCustomers();
		loadAccounts();
		
	}
	
	public void loadCustomers() throws Exception {
		String[] customers = CapitalHttpClient.getAllCustomers();
		for(String s : customers) {
			Customer c;
			String firstName = CapitalHttpClient.getCustomerByID(s, "first_name");
			String lastName = CapitalHttpClient.getCustomerByID(s, "last_name");
			String zip = CapitalHttpClient.getCustomerByID(s, "zip");
			String city = CapitalHttpClient.getCustomerByID(s, "city");
			String streetNum = CapitalHttpClient.getCustomerByID(s, "street_number");
			String state = CapitalHttpClient.getCustomerByID(s, "state");
			String streetName = CapitalHttpClient.getCustomerByID(s, "street_name");
			c = new Customer(s, firstName, lastName, zip, city, streetNum, state, streetName);
			customerList.add(c);
		}
	}
	
	public void loadAccounts() throws Exception {
		String[] accounts = CapitalHttpClient.getAllAccounts("");
		for(String s : accounts) {
			Account a;
			String custID = CapitalHttpClient.getAccountByID(s, "customer_id");
			String type = CapitalHttpClient.getAccountByID(s, "type");
			String nickname = CapitalHttpClient.getAccountByID(s, "nickname");
			int rewards = Integer.parseInt(CapitalHttpClient.getAccountByID(s, "rewards"));
			int balance = Integer.parseInt(CapitalHttpClient.getAccountByID(s, "balance"));
			String accNum = CapitalHttpClient.getAccountByID(s, "account_number");
			
			
		}
	}
}
