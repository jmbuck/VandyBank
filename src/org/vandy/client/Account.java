package org.vandy.client;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private List<Transfer> transferList = new ArrayList<Transfer>();
	private List<Bill> billList = new ArrayList<Bill>();
	private List<Transaction> transList = new ArrayList<Transaction>();
	private List<Deposit> depList = new ArrayList<Deposit>();
	private List<Withdrawal> withList = new ArrayList<Withdrawal>();
	private String id, type, nickname, account_number, customer_id;
	private int rewards;
	private double balance;
	
	public Account(String accId, String custID, String accType, String custNickname, int accRewards, double accBalance,
				String accNum) throws Exception 
	{	
		id = accId;
		type = accType;
		nickname = custNickname;
		rewards = accRewards;
		balance = accBalance;
		account_number = accNum;
		customer_id = custID;
	}
	
	public String getID()
	{
		return id;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getNickname()
	{
		return nickname;
	}
	
	public String getAccountNumber()
	{
		return account_number;
	}
	
	public String getCustomerID()
	{
		return customer_id;
	}
	
	public int getRewards()
	{
		return rewards;
	}
	
	public double getBalance()
	{
		return balance;
	}
	
	public void setType(String accType) throws Exception
	{
		type = accType;
		CapitalHttpClient.putAccountChanges(id, "type", accType);
	}
	
	public void addBill(Bill cost)
	{
		billList.add(cost);
	}
	
	public int deposit(Deposit transaction, String depID) throws Exception
	{
		double amount = Double.parseDouble(CapitalHttpClient.getDepositsByID(depID, "amount"));
		if(balance + amount < 0 || amount < 0)	//Insufficient funds or amount
		{
			return 0;				//Check bounces
		}
		else
		{
			balance += amount;
			transList.add(transaction);
			depList.add(transaction);			
			return 1;
		}
	}
	
	public int withdraw(Withdrawal transaction, String withID) throws Exception
	{
		double amount = Double.parseDouble(CapitalHttpClient.getWithdrawalsByID(withID, "amount"));
		if(balance - amount < 0 || amount < 0)	//Insufficient funds or amount
		{
			return 0;				//Check bounces
		}
		else
		{
			balance -= amount;
			transList.add(transaction);
			withList.add(transaction);			
			return 1;
		}
	}
}
