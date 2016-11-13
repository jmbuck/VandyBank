package org.vandy.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.polaris.engine.util.MathHelper;

public class Bank {

	private static List<Customer> customerList = new ArrayList<Customer>();
	private static List<Account> accountList = new ArrayList<Account>();
	private static List<Bill> billList = new ArrayList<Bill>();
	private static List<Deposit> depList = new ArrayList<Deposit>();
	private static List<Withdrawal> withList = new ArrayList<Withdrawal>();
	private static List<Transfer> transferList = new ArrayList<Transfer>();
	private static List<Merchant> merchList = new ArrayList<Merchant>();
	private static List<Purchase> purchList = new ArrayList<Purchase>();
	private static Customer curr;

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
		try {
			String[] merchants = CapitalHttpClient.getMerchants();
			for(String s: merchants) {
				String name = CapitalHttpClient.getMerchantByID(s, "name");
				String category = CapitalHttpClient.getMerchantByID(s, "category");
				String[] parts = category.split(",");
				String zip = CapitalHttpClient.getMerchantByID(s, "zip");
				String city = CapitalHttpClient.getMerchantByID(s, "city");
				String streetNumber = CapitalHttpClient.getMerchantByID(s, "street_number");
				String streetName = CapitalHttpClient.getMerchantByID(s, "street_name");
				String state = CapitalHttpClient.getMerchantByID(s, "state");
				Merchant m = new Merchant (s, name, parts, zip, city, streetNumber,
											state, streetName);
				merchList.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadTransfers() {
		// TODO Auto-generated method stub
		try {
			for(Account a : accountList) {
				String[] transfers = CapitalHttpClient.getTransfers(a.getID());
				for(String s : transfers) {
					String type = CapitalHttpClient.getTransferByID(s, "type");
					String transDate = CapitalHttpClient.getTransferByID(s, "transaction_date");
					String status = CapitalHttpClient.getTransferByID(s, "status");
					String medium = CapitalHttpClient.getTransferByID(s, "medium");
					double amount = Double.parseDouble(CapitalHttpClient.getTransferByID(s, "amount"));
					String payer = CapitalHttpClient.getTransferByID(s, "payer_id");
					String payee = CapitalHttpClient.getTransferByID(s, "payee_id");
					String desc = "No description.";
					try {
						desc = CapitalHttpClient.getTransferByID(s, "description");
					} catch (Exception e) {
						desc = "No description.";
					}


					Transfer trans = new Transfer(s, type, transDate, status, payee, medium,
							amount, desc, payer);
					if(a.getID().equals(payee))
						a.addPayeeTrans(trans);
					if(a.getID().equals(payer))
						a.addPayerTrans(trans);

					transferList.add(trans);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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
				String accNum = "";
				try {
					accNum = CapitalHttpClient.getAccountByID(s, "account_number");
				} catch (Exception e) {
					accNum = "UnknownAccount##";
				}

				a = new Account(s, custID, type, nickname, rewards, balance, accNum);
				//add accounts to customers
				for(Customer c : customerList) {
					if(custID.equals(c.getID())) {
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
		while (i < customerList.size() && c.getID() != id) {c = customerList.get(i); i++;}
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
	
	public static String getDate()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		return df.format(today);
	}
	
	public static Customer addCustomer(String first, String last, String streetNum, String streetName, String city, String state, String zip)
	{
		try
		{
			String custID = CapitalHttpClient.postCustomer(first, last, streetNum, streetName, city, state, zip);
			Customer curr = new Customer(custID, first, last, streetNum, streetName, city, state, zip);
			customerList.add(curr);
			return curr;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Account addAccount(String accType, String nickname) {
		try {
			//generate 16 digit acc number
			if(curr.getAccounts().size() < 4) {
				String accNum = "";
				for(int i = 0; i < 16; i++) {
					int num = MathHelper.random(9);
					accNum += Integer.toString(num);
				}
				System.out.println(accNum);
				String custId = curr.getID();
				String accId = CapitalHttpClient.postAccount(custId, accType, nickname, 0, 0, accNum);
				Account acc = new Account(accId, custId, accType, nickname, 0, 0, accNum);
				curr.addAccount(acc);
				accountList.add(acc);
				return acc;	
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Deposit addDesposit(Account acc, double amount, String desc)
	{
		try
		{
			String date = getDate();
			String depID = CapitalHttpClient.postDeposit(acc.getID(), "balance", date, amount, desc);
			Deposit transaction = new Deposit(depID, "deposit", date, "pending", acc.getID(), "balance", amount, desc);
			acc.deposit(transaction, depID);
			depList.add(transaction);
			return transaction;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static Withdrawal addWithdrawal(Account acc, double amount, String desc)
	{
		try
		{
			String date = getDate();
			String withID = CapitalHttpClient.postWithdrawal(acc.getID(), "balance", date, amount, desc);
			Withdrawal transaction = new Withdrawal(depID, "withdrawal", date, "pending", acc.getID(), "balance", amount, desc);
			acc.withdraw(transaction, withID);
			withList.add(transaction);
			return transaction;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static Purchase addPurchase(Account acc, double amount, String merchID, String desc)
	{
		try
		{
			String date = getDate();
			String purchID = CapitalHttpClient.postPurchase(acc.getID(), merchID, "balance", date, amount, desc);
			Purchase transaction = new Purchase(purchID, "merchant", date, "pending", acc.getID(), "balance", amount, desc, merchID);
			acc.purchase(transaction, purchID);
			purchList.add(transaction);
			return transaction;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void createCustomers()
	{
		try
		{
			List<Customer> customers = new Randomize().getCustomers();
			for(Customer c : customers)
				customerList.add(c);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void setCurrentCustomer(Customer c)
	{
		curr = c;
	}

	public static Customer getCurrentCustomer() {
		return curr;
	}

	public static Customer findCustomer(String fullname) {
		String[] parts = fullname.split(" ");

		if(parts.length != 2)
			return null;

		for(Customer c : customerList) {
			if(parts[0].equals(c.getFirstName()) && parts[1].equals(c.getLastName())) {
				return c;
			}
		}
		return null;
	}
}