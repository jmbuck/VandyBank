package org.vandy.client;

public class Withdrawal extends Transaction{
	
	public Withdrawal(String wId, String wType, String wTransDate, String wStatus, String aId, String med, double dAmt, String descr) {
		super(wId, wType, wTransDate, wStatus, aId, med, dAmt, descr, -1);
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