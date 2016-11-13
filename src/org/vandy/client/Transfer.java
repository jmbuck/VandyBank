package org.vandy.client;

public class Transfer {
	
	String id, type, transDate, status, medium, desc;
	double amount;
	String payee, payer;
	
	public Transfer(String tId, String tType, String transactionDate,
					String tStatus, String tPayee, String med, double amt,
					String descr, String tPayer) {
		id = tId;
		type = tType;
		transDate = transactionDate;
		status = tStatus;
		medium = med;
		desc = descr;
		amount = amt;
		payee = tPayee;
		payer = tPayer;
	}
}
