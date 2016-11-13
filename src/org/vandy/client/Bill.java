package org.vandy.client;

public class Bill {
	
	String id, status, nickname, creationDate, paymentDate, upcomingPaymentDate;
	Customer payee;
	Account acc;
	int recurring;
	
	public Bill(String bId, String bStatus, Customer bPayee, String bNickname, String bCreationDate,
				String bPaymentDate, int bRecurr, String bUpcoming, Account bAcc) {
		id = bId;
		status = bStatus;
		payee = bPayee;
		nickname = bNickname;
		creationDate = bCreationDate;
		paymentDate = bPaymentDate;
		recurring = bRecurr;
		upcomingPaymentDate = bUpcoming;
		acc = bAcc;
	}
}
