package org.vandy.client;

public class Deposit extends Transaction {
	
	public Deposit(String dId, String dType, String dTransDate, String dStatus, String aId, String med, double dAmt, String descr) {
		super(dId, dType, dTransDate, dStatus, aId, med, dAmt, descr, 1);
	}
	
	public void setStatus(String state) throws Exception
	{
		if(!status.equals(state))
		{
			status = state;
			CapitalHttpClient.putDepositChanges(id, "status", state);
		}		
	}
}