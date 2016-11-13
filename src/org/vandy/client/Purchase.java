package org.vandy.client;

public class Purchase extends Withdrawal {
	
	String merch;
	
	public Purchase(String pId, String pType, String purchDate, String pStatus, String pPayer, String med,
			double wAmt, String descr, String pMerch) {
		super(pId, pType, purchDate, pStatus, pPayer, med, wAmt, descr);
		merch = pMerch;
		// TODO Auto-generated constructor stub
	}
	
	public int getType() {
		return 2; //purchase
	}

}