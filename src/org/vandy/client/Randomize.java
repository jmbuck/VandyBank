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
			createRandomCustomer(firsts.get(MathHelper.random(12)), lasts.get(MathHelper.random(12)), streetNums.get(MathHelper.random(12)),
					streetNames.get(MathHelper.random(12)), cities.get(MathHelper.random(12)), states.get(MathHelper.random(12)),
						zipCodes.get(MathHelper.random(12)));
		}
	}
	
	public Customer createRandomCustomer(String first, String last, String streetNum, String streetName, String city, String state, String zipCode) throws Exception
	{
		numAccounts = MathHelper.random(1, 4);
		String randID = CapitalHttpClient.postCustomer(first, last, streetNum, streetName, city, state, zipCode);
		Customer randy = new Customer(randID, first, last, streetNum, streetName, city, state, zipCode);
		String randFirstLast = first + last;
		addRandomAccounts(randID, randFirstLast);
		numDeposits = MathHelper.random(15, 20);
		numWithdrawals = MathHelper.random(10, 15);
		numPurchases = MathHelper.random(5, 10);
		numBills = MathHelper.random(3, 6);
	}
	
	public void addRandomAccounts(String randID, String randFirstLast) throws Exception
	{
		List<String> types = new ArrayList<String>();
		types.add("Credit Card"); types.add("Checking"); types.add("Savings");
		for(int i = 0; i < numAccounts; i++)
		{
			String accID = CapitalHttpClient.postAccount(randID, types.get((int)(Math.random()*4)), randFirstLast, 0.00, Math.round((Math.random()*1000 + 1000)*100)/100.00, Integer.toString(i+1));
		}
		
	}

}
