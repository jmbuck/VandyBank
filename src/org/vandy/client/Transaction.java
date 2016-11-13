package org.vandy.client;

public class Transaction {
	
	String id, acctId, medium, transDate, desc, status, type;
	double amount;
	int multiplier;
	public Transaction(String Id, String dType, String dTransDate, String dStatus, String aId, String med, double dAmt, String descr, int mult)
	{
		id = Id;
		acctId = aId;
		medium = med;
		transDate = dTransDate;
		desc = descr;
		amount = dAmt;
		status = dStatus;
		type = dType;
		multiplier = mult;
	}
}
