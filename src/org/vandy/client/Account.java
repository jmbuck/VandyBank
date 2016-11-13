package org.vandy.client;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private List<Bill> billList = new ArrayList<Bill>();
	private List<Transaction> transList = new ArrayList<Transaction>();
	private List<Deposit> depList = new ArrayList<Deposit>();
	private List<Withdrawal> withList = new ArrayList<Withdrawal>();
	private List<Transfer> payerTransList = new ArrayList<Transfer>();
	private List<Transfer> payeeTransList = new ArrayList<Transfer>();
	private String id, type, nickname, account_number, customer_id;
	private int rewards, balance;
	
	public Account(String accId, String custID, String accType, String custNickname, int accRewards, int accBalance,
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
	
	public double getRewards()
	{
		return rewards;
	}
	
	public double getBalance()
	{
		return balance;
	}
	
	public void addDep(Deposit d) {
		depList.add(d);
	}
	
	public void addWithdraw(Withdrawal w) {
		withList.add(w);
	}
	
	public void addPayerTrans(Transfer t) {
		payerTransList.add(t);
	}
	
	public void addPayeeTrans(Transfer t) {
		payeeTransList.add(t);
	}
	
	public void setType(String accType) throws Exception
	{
		if(!type.equals(accType))
		{
			type = accType;
			CapitalHttpClient.putAccountChanges(id, "type", accType);
		}
	}
	
	public void setNickname(String accNickname) throws Exception
	{
		if(!nickname.equals(accNickname))
		{
			nickname = accNickname;
			CapitalHttpClient.putAccountChanges(id, "nickname", accNickname);
		}
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
			CapitalHttpClient.putAccountChanges(id, "balance", Double.toString(balance));
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
			CapitalHttpClient.putAccountChanges(id, "balance", Double.toString(balance));
			transList.add(transaction);
			withList.add(transaction);			
			return 1;
		}
	}
	
	public int purchase(Purchase transaction, String purchID) throws Exception
	{
		double amount = Double.parseDouble(CapitalHttpClient.getWithdrawalsByID(purchID, "amount"));
		if(balance - amount < 0 || amount < 0)	//Insufficient funds or amount
		{
			return 0;				//Check for bounces
		}
		else
		{
			balance -= amount;
			rewards += amount*0.015;
			CapitalHttpClient.putAccountChanges(id, "balance", Double.toString(balance));
			CapitalHttpClient.putAccountChanges(id, "rewards", Double.toString(rewards));
			transList.add(transaction);
			withList.add(transaction);	
			return 1;
		}		
	}
}
