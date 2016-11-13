package org.vandy.client;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private List<Transfer> transferList = new ArrayList<Transfer>();
	private List<Bill> billList = new ArrayList<Bill>();
	private List<Deposit> depList = new ArrayList<Deposit>();
	private List<Withdrawal> withList = new ArrayList<Withdrawal>();
	private String id, type, nickname, account_number, customer_id;
	private int rewards;
	private double balance;
	
	public Account(String custID, String accType, String custNickname, int accRewards, double accBalance,
					String acctNum) throws Exception 
	{	
		id = custID;
		type = accType;
		nickname = custNickname;
		rewards = accRewards;
		balance = accBalance;
		account_number = acctNum;
	}
	
	public int withdraw(Withdrawal transaction)
	{
		amount = 
		if(balance - amount < 0 || amount < 0)	//Insufficient funds or amount
		{
			return 0;				//Check bounces
		}
		else
		{
			balance -= amount;
			withList.add(amount)
			return 1;
		}
	}
}
