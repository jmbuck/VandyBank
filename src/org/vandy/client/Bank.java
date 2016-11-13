package org.vandy.client;

import java.util.*;

public class Bank {

	private static List<Customer> customerList = new ArrayList<Customer>();
	private static List<Account> accountList = new ArrayList<Account>();
	private static List<Bill> billList = new ArrayList<Bill>();
	private static List<Deposit> depList = new ArrayList<Deposit>();
	private static List<Withdrawal> withList = new ArrayList<Withdrawal>();
	private static List<Transfer> transferList = new ArrayList<Transfer>();
	private static List<Merchant> merchList = new ArrayList<Merchant>();
	private static List<Purchase> purchList = new ArrayList<Purchase>();

	public static void load() {
		try {
			System.out.println("Loading Customers...");
			loadCustomers();
			System.out.println("Loading Accounts...");
			loadAccounts();
			System.out.println("Loading Bills...");
			loadBills();
			System.out.println("Loading Deposits...");
			loadDeposits();
			System.out.println("Loading Withdrawals...");
			loadWithdrawals();
			System.out.println("Loading Transfers...");
			loadTransfers();
			System.out.println("Loading Merchants...");
			loadMerchants();
			System.out.println("Loading Purchases...");
			loadPurchases();
			System.out.println("Done loading.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void loadPurchases() {
		// TODO Auto-generated method stub
		
	}

	private static void loadMerchants() {
		// TODO Auto-generated method stub

	}

	private static void loadTransfers() {
		// TODO Auto-generated method stub

	}

	private static void loadWithdrawals() {
		// TODO Auto-generated method stub
		try {
			for(Account a : accountList) {
				String[] withdrawals = CapitalHttpClient.getDeposits(a.getID());
				for(String s : withdrawals) {
					String type = CapitalHttpClient.getWithdrawalsByID(s, "type");
					//String transDate = CapitalHttpClient.getWithdrawalsByID(s, "transaction_date");
					String transDate = "";
					try {
						 transDate = CapitalHttpClient.getWithdrawalsByID(s, "transaction_date");
					}catch (Exception e) {
						transDate = "Unknown Date";
					}
					String status = CapitalHttpClient.getWithdrawalsByID(s, "status");
					String medium = CapitalHttpClient.getWithdrawalsByID(s, "medium");
					String description = CapitalHttpClient.getWithdrawalsByID(s, "description");
					double amt = Double.parseDouble(CapitalHttpClient.getWithdrawalsByID(s, "amount"));
					Withdrawal w = new Withdrawal(s, type, transDate, status, a.getID(), medium, amt, description);
					a.addWithdraw(w);
					for(Customer c : customerList) {
						if(a.getCustomerID().equals(c.getID())) {
							c.addWithdraw(w);
						}
					}
					withList.add(w);
				}
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void loadDeposits() {
		// TODO Auto-generated method stub
		try {
			for(Account a : accountList) {
				String[] deposits;
				deposits = CapitalHttpClient.getDeposits(a.getID());
				for(String s : deposits) {
					String type = CapitalHttpClient.getDepositsByID(s, "type");
					String transDate = "";
					try {
						 transDate = CapitalHttpClient.getDepositsByID(s, "transaction_date");
					}catch (Exception e) {
						transDate = "Unknown Date";
					}
					String status = CapitalHttpClient.getDepositsByID(s, "status");
					String medium = CapitalHttpClient.getDepositsByID(s, "medium");
					String description = CapitalHttpClient.getDepositsByID(s, "description");
					double amt = Double.parseDouble(CapitalHttpClient.getDepositsByID(s, "amount"));
					Deposit d = new Deposit(s, type, transDate, status, a.getID(), medium, amt, description);
					a.addDep(d);
					for(Customer c : customerList) {
						if(a.getCustomerID().equals(c.getID())) {
							c.addDep(d);
						}
					}
					depList.add(d);
				}
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadCustomers() {
		try{
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadAccounts() {
		try {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadBills() {
		try {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String[] getCustomers() {
		String[] customers = new String[customerList.size()];
		for (int i = 0; i<customerList.size(); i++) {
			customers[i] = customerList.get(i).getID();
		}
		return customers;
	}
	
	public static String lookUpCustomer(String id, String parameter) {
		Customer c = customerList.get(0);
		int i = 0;
		while (c.getID() != id) c = customerList.get(i);
		if (parameter.equals("first_name")) {
			return c.getFirstName();
		}
		if (parameter.equals("last_name")) {
			return c.getLastName();
		}
		if (parameter.equals("zip")) {
			return c.getZip();
		}
		if (parameter.equals("city")) {
			return c.getCity();
		}
		if (parameter.equals("street_number")) {
			return c.getStreetNum();
		}
		if (parameter.equals("state")) {
			return c.getState();
		}
		if (parameter.equals("street_name")) {
			return c.getStreetName();
		}
		return "";
	}
	
	public static void createCustomer(Customer c)
	{

	}

	public static void setCurrentCustomer(Customer c)
	{


	}

	public static Customer findCustomer(String fullname) {
		String[] parts = fullname.split(" ");
		for(Customer c : customerList) {
			if(parts[0].equals(c.getFirstName()) && parts[1].equals(c.getLastName())) {
				return c;
			}
		}
		return null;
	}
}