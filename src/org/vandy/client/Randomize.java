package org.vandy.client;

import java.util.*;

import com.polaris.engine.util.MathHelper;

public class Randomize {
	private int numAccounts;
	private int numDeposits;
	private int numWithdrawals;
	private int numPurchases;
	private int numBills;
	private List<Customer> custList = new ArrayList<Customer>();
	private int numCustomers = 20;
	
	public Randomize() throws Exception
	{
		for(int i = 0; i < numCustomers; i++)
		{
			List<String> firsts = new ArrayList<String>();
			firsts.add("Bob");firsts.add("Norma");firsts.add("John");firsts.add("Steve");
			firsts.add("Jane");firsts.add("Frank");firsts.add("Shelby");firsts.add("Mehul");
			firsts.add("Eve");firsts.add("Adam");firsts.add("Randy");firsts.add("Micheal");
			List<String> lasts = new ArrayList<String>();
			firsts.add("Ross");firsts.add("Jean");firsts.add("Johnson");firsts.add("Joe");
			firsts.add("Doe");firsts.add("Hanks");firsts.add("Robinson");firsts.add("Patel");
			firsts.add("Simpson");firsts.add("Sin");firsts.add("McRando");firsts.add("Jordan");
			List<String> streetNums = new ArrayList<String>();
			firsts.add("100");firsts.add("256");firsts.add("300");firsts.add("404");
			firsts.add("512");firsts.add("600");firsts.add("777");firsts.add("800");
			firsts.add("900");firsts.add("999");firsts.add("1028");firsts.add("1337");
			List<String> streetNames = new ArrayList<String>();
			firsts.add("Generic Street");firsts.add("Time Square");firsts.add("Drivers Drive");firsts.add("Avenue Boulevard");
			firsts.add("Boulevard Avenue");firsts.add("Hicks Street");firsts.add("Circle Court");firsts.add("Skeet Street");
			firsts.add("Home Drive");firsts.add("City Square");firsts.add("Boonesborough Road");firsts.add("Rocky Road");
			List<String> cities = new ArrayList<String>();
			firsts.add("Springfield");firsts.add("New York");firsts.add("West Lafayette");firsts.add("Atlanta");
			firsts.add("San Francisco");firsts.add("Indianapolis");firsts.add("Nashville");firsts.add("Louisville");
			firsts.add("Lexington");firsts.add("Cincinatti");firsts.add("Miami");firsts.add("Toddsville");
			List<String> states = new ArrayList<String>();
			firsts.add("Indiana");firsts.add("Illinois");firsts.add("Tennessee");firsts.add("Kentucky");
			firsts.add("Virginia");firsts.add("New York");firsts.add("Ohio");firsts.add("Texas");
			firsts.add("Florida");firsts.add("California");firsts.add("Pennsylvania");firsts.add("Georgia");
			List<String> zipCodes = new ArrayList<String>();
			firsts.add("12345");firsts.add("67891");firsts.add("23456");firsts.add("78912");
			firsts.add("34567");firsts.add("89123");firsts.add("45678");firsts.add("91234");
			firsts.add("56789");firsts.add("19283");firsts.add("74651");firsts.add("13579");
			Customer rand = createRandomCustomer(firsts.get(MathHelper.random(12)), lasts.get(MathHelper.random(12)), streetNums.get(MathHelper.random(12)),
					streetNames.get(MathHelper.random(12)), cities.get(MathHelper.random(12)), states.get(MathHelper.random(12)),
						zipCodes.get(MathHelper.random(12)));
			custList.add(rand);
		}
	}
	
	public Customer createRandomCustomer(String first, String last, String streetNum, String streetName, String city, String state, String zipCode) throws Exception
	{
		numAccounts = MathHelper.random(1, 4);
		String randID = CapitalHttpClient.postCustomer(first, last, streetNum, streetName, city, state, zipCode);
		Customer randy = new Customer(randID, first, last, streetNum, streetName, city, state, zipCode);
		String randFirstLast = first + last;
		addRandomAccounts(randID, randFirstLast);
		return randy;
	}
	
	public void addRandomAccounts(String randID, String randFirstLast) throws Exception
	{
		List<String> types = new ArrayList<String>();
		types.add("Credit Card"); types.add("Checking"); types.add("Savings");
		for(int i = 0; i < numAccounts; i++)
		{
			int balance = (int)Math.round((Math.random()*1000 + 1000)*100);
			String type = types.get((int)(Math.random()*4));
			String accID = CapitalHttpClient.postAccount(randID, type, randFirstLast, 0, balance, "10000000" + Integer.toString(i+10000001));
			Account randAccount = new Account(accID, randID, type, randFirstLast, 0.00, balance/100.00, "10000000" + Integer.toString(i+10000001));
			numDeposits = MathHelper.random(15, 20);
			if(!type.equals("Credit Card"))
				makeRandomDeposits(randAccount, accID);
			numWithdrawals = MathHelper.random(10, 15);
			makeRandomWithdrawals(randAccount, accID);
			numPurchases = MathHelper.random(5, 10);
			if(!type.equals("Savings"))
				makeRandomPurchases(randAccount, accID);
//			numBills = MathHelper.random(3, 6);
//			makeRandomBills(randAccount, accID);
			custList.get(custList.size()-1).addAccount(randAccount);
		}
		
	}
	
	public void makeRandomDeposits(Account randAccount, String accID) throws Exception
	{
		for(int i = 0; i < numDeposits; i++)
		{
			double amount = MathHelper.random(100, 300);
			String depID = CapitalHttpClient.postDeposit(accID, "balance", "11-13-2016", amount, "random");
			Deposit randDep = new Deposit(accID, "balance", "11-13-2016", "pending", depID, "deposit", amount, "random");
			randAccount.deposit(randDep, depID);
		}
		
	}
	
	public void makeRandomWithdrawals(Account randAccount, String accID) throws Exception
	{
		for(int i = 0; i < numWithdrawals; i++)
		{
			double amount = MathHelper.random(100, 200);
			String withID = CapitalHttpClient.postWithdrawal(accID, "balance", "11-13-2016", amount, "random");
			Withdrawal randWith = new Withdrawal(accID, "balance", "11-13-2016", "pending", withID, "withdraw", amount, "random");
			randAccount.withdraw(randWith, withID);
		}
		
	}
	
	public void makeRandomPurchases(Account randAccount, String accID) throws Exception
	{
		for(int i = 0; i < numPurchases; i++)
		{
			double amount = MathHelper.random(50, 150);
			String purchID = CapitalHttpClient.postPurchase(accID, "1337", "balance", "11-13-2016", amount, "random");
			Purchase randPurch = new Purchase(accID, "balance", "11-13-2016", "pending", purchID, "deposit", amount, "random", "1337");
			randAccount.purchase(randPurch, purchID);
		}
		
	}
	
	/*public void makeRandomBills(Account randAccount, String accID) throws Exception
	{
		for(int i = 0; i < numWithdrawals; i++)
		{
			double amount = MathHelper.random(100, 200);
			String withID = CapitalHttpClient.postWithdrawal(accID, "balance", "11-13-2016", amount, "random");
			Withdrawal randWith = new Withdrawal(accID, "balance", "11-13-2016", "pending", withID, "withdraw", amount, "random");
			randAccount.withdraw(randWith, withID);
		}
		
	}*/

}
