package org.vandy.client;

public class Purchase extends Withdrawal {
	
	String id, type, status, medium, desc, purchaseDate;
	double amt;
	Merchant merch;
	Account payer;
	
	public Purchase(String pId, String pType, Merchant pMerch, Account payer, String purchDate, double wAmt, String pStatus,
			String med, String descr) {
		super(pId, pType, purchDate, pStatus, payer, med, wAmt, descr);
		merch = pMerch;
		// TODO Auto-generated constructor stub
	}

}