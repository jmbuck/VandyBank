package org.vandy.client;

public class Withdrawal extends Transaction{

	String id, medium, transDate, desc, status, type, acctId;
	double amount;
	
	public Withdrawal(String wId, String wType, String wTransDate, String wStatus, String aID, String med, double wAmt, String descr) {
		id = wId;
		acctId = aID;
		medium = med;
		transDate = wTransDate;
		desc = descr;
		amount = wAmt;
		status = wStatus;
		type = wType;
	}
}