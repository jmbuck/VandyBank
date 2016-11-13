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
	
	public int getMultiplier() {
		return multiplier;
	}
	
	public int getType() {
		if(multiplier == 1) {
			return 0; //deposit
		}
		if(multiplier == -1) {
			return 1; //withdraw
		}
		return -1;
	}
	
	public String getID() {
		return id;
	}
	
	public String getAcctId() {
		return acctId;
	}
	
	public String getMedium() {
		return medium;
	}
	
	public String getTransDate() {
		return transDate;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public String getStatus() {
		return status;
	}
	
	
	
}
