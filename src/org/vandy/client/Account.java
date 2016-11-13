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
	private double rewards, balance;
	
	public Account(String accId, String custID, String accType, String custNickname, double accRewards, double accBalance,
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
	
	public List<Deposit> getDepList()
	{
		return depList;
	}
	
	public List<Withdrawal> getWithList()
	{
		return withList;
	}
	
	public List<Transaction> getTransList()
	{
		return transList;
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
	
	public void setType(String accType)
	{
		if(!type.equals(accType))
		{
			type = accType;
			try {
				CapitalHttpClient.putAccountChanges(id, "type", accType);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setNickname(String accNickname)
	{
		if(!nickname.equals(accNickname))
		{
			nickname = accNickname;
			try {
				CapitalHttpClient.putAccountChanges(id, "nickname", accNickname);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addBill(Bill cost)
	{
		billList.add(cost);
	}
	
	public int deposit(Deposit transaction, String depID) throws Exception
	{
		double amount = Double.parseDouble(CapitalHttpClient.getDepositsByID(depID, "amount"));
		if(amount < 0)	//Insufficient amount
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
	
	public int transferTo(Transfer transaction, String transID, Account receiver)
	{
		try
		{
			double amount = Double.parseDouble(CapitalHttpClient.getTransferByID(transID, "amount"));
			if(balance - amount < 0 || amount < 0)	//Insufficient funds or amount
			{
				return 0;				//Check for bounces
			}
			else
			{
				balance -= amount;
				Withdrawal withdraw = new Withdrawal(transaction.getID(), type, transaction.getTransDate(), "pending", id, "balance", transaction.getAmount(), transaction.getDesc());
				Deposit deposit = new Deposit(transaction.getID(), receiver.getType(), transaction.getTransDate(), "pending", receiver.getID(), "balance", transaction.getAmount(), transaction.getDesc());
				receiver.deposit(deposit, transID);
				CapitalHttpClient.putAccountChanges(id, "balance", Double.toString(balance));
				CapitalHttpClient.putAccountChanges(id, "rewards", Double.toString(rewards));
				transList.add(withdraw);
				withList.add(withdraw);
				receiver.getDepList().add(deposit);
				receiver.getTransList().add(deposit);
				return 1;
			}		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int transferFrom(Transfer transaction, String transID, Account source)
	{
		try
		{
			double amount = Double.parseDouble(CapitalHttpClient.getTransferByID(transID, "amount"));
			if(source.getBalance() - amount < 0 || amount < 0)	//Insufficient funds or amount
			{
				return 0;				//Check for bounces
			}
			else
			{
				balance += amount;
				Deposit deposit = new Deposit(transaction.getID(), type, transaction.getTransDate(), "pending", id, "balance", transaction.getAmount(), transaction.getDesc());
				Withdrawal withdraw = new Withdrawal(transaction.getID(), source.getType(), transaction.getTransDate(), "pending", source.getID(), "balance", transaction.getAmount(), transaction.getDesc());
				source.withdraw(withdraw, transID);
				CapitalHttpClient.putAccountChanges(id, "balance", Double.toString(balance));
				CapitalHttpClient.putAccountChanges(id, "rewards", Double.toString(rewards));
				transList.add(deposit);
				depList.add(deposit);
				source.getWithList().add(withdraw);
				source.getTransList().add(withdraw);
				return 1;
			}		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public void merge(Account account)
	{
		double amount = account.getBalance();
		if(amount == 0)
			return;
		balance += amount;
//		CapitalHttpClient.deleteAccount();
		
	}
}
