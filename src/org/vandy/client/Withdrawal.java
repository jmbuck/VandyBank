package org.vandy.client;

public class Withdrawal extends Transaction{

	String id, acctId, medium, transDate, desc, status, type;
	double amount;
	
	public Withdrawal(String wId, String wType, String wTransDate, String wStatus, String aId, String med, double wAmt, String descr) {
		id = wId;
		acctId = aId;
		medium = med;
		transDate = wTransDate;
		desc = descr;
		amount = wAmt;
		status = wStatus;
		type = wType;
	}
}
