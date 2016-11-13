package org.vandy.client;

public class Deposit extends Transaction {

	String id, acctId, medium, transDate, desc, status, type;
	double amount;
	
	public Deposit(String dId, String dType, String dTransDate, String dStatus, String aId, String med, double dAmt, String descr) {
		id = dId;
		acctId = aId;
		medium = med;
		transDate = dTransDate;
		desc = descr;
		amount = dAmt;
		status = dStatus;
		type = dType;
	}
}