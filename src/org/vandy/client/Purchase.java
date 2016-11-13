package org.vandy.client;

public class Purchase extends Withdrawal {
	
	String id, payer, merch, type, status, medium, desc, purchaseDate;
	double amt;
	
	public Purchase(String pId, String pType, String purchDate, String pStatus, String pPayer, String med,
			double wAmt, String descr, String pMerch) {
		super(pId, pType, purchDate, pStatus, pPayer, med, wAmt, descr);
		merch = pMerch;
		// TODO Auto-generated constructor stub
	}

}