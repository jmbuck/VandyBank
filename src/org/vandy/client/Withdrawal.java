package org.vandy.client;

public class Withdrawal extends Transaction{

	String id, medium, transDate, desc, status, type;
	Account acct;
	double amount;
	
	public Withdrawal(String wId, String wType, String wTransDate, String wStatus, Account wAcct, String med, double wAmt, String descr) {
		id = wId;
		acct = wAcct;
		medium = med;
		transDate = wTransDate;
		desc = descr;
		amount = wAmt;
		status = wStatus;
		type = wType;
	}
}