package org.vandy.client;

import java.util.*;

public class Bank {
	
	private List<Customer> customerList = new ArrayList<Customer>();
	private List<Account> accountList = new ArrayList<Account>();
	private List<Bill> billList = new ArrayList<Bill>();
	private List<Deposit> depList = new ArrayList<Deposit>();
	private List<Withdrawal> withList = new ArrayList<Withdrawal>();
	private List<Transfer> transferList = new ArrayList<Transfer>();
	private List<Merchant> merchList = new ArrayList<Merchant>();
	private List<Purchase> purchList = new ArrayList<Purchase>();
	
	public Bank() throws Exception {
		
	}
	
	public void load() throws Exception {
		loadCustomers();
		loadAccounts();
		loadBills();
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
			a = new Account(s, custID, type, nickname, rewards, balance, accNum);
			//add accounts to customers
			for(Customer c : customerList) {
				if(s.equals(c.getID())) {
					c.addAccount(a);
				}
			}
			accountList.add(a);
			
		}
	}
	
	public void loadBills() throws Exception {
		for(Customer c : customerList) {
			for(Account a : c.getAccounts()) {
				String[] accBills = CapitalHttpClient.getAccountBills(a.getID());
				for(String b : accBills) {
					String status = CapitalHttpClient.getBillByID(b, "status");
					String nickname = CapitalHttpClient.getBillByID(b, "nickname");
					String creationDate = CapitalHttpClient.getBillByID(b, "creation_date");
					String paymentDate = CapitalHttpClient.getBillByID(b, "payment_date");
					int recurr = Integer.parseInt(CapitalHttpClient.getBillByID(b, "recurring_date"));
					String upcoming = CapitalHttpClient.getBillByID(b, "upcoming_payment_date");
					Bill bill = new Bill(b, status, c, nickname, creationDate, paymentDate, recurr, upcoming, a);
					c.addBill(bill);
					a.addBill(bill);
					billList.add(bill);
				}
			}
		}
	}
}