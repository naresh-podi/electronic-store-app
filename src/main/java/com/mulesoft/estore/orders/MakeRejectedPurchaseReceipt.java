package com.mulesoft.estore.orders;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class MakeRejectedPurchaseReceipt implements Callable{

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		PurchaseReceipt purchaseReceipt = new PurchaseReceipt();
		
		purchaseReceipt.setStatus(Status.REJECTED);
		purchaseReceipt.setTotalPrice(0);
		
		return purchaseReceipt;
	}
}
