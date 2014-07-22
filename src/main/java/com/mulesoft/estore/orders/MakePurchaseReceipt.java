package com.mulesoft.estore.orders;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transport.PropertyScope;

public class MakePurchaseReceipt implements Callable{

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		
		Float totalPrice = Float.valueOf(eventContext.getMessage().getProperty("totalPrice", PropertyScope.INVOCATION).toString());
		PurchaseReceipt purchaseReceipt = new PurchaseReceipt();
		
		purchaseReceipt.setStatus(Status.ACCEPTED);
		purchaseReceipt.setTotalPrice(totalPrice);
		
		return purchaseReceipt;
	}
}
