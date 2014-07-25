package com.whiteskylabs.electronicstore.testcases;

import junit.framework.Assert;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import com.mulesoft.estore.orders.PurchaseReceipt;
import com.mulesoft.estore.orders.Status;

public class NonExistingProductTest extends FunctionalMunitSuite {

	
	@Override
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "process-samsung-items.xml,process-non-samsung-items.xml,get-samsung-price-service.xml,get-non-samsung-price-service.xml,electronics-store.xml,database-initialisation.xml,audit-service.xml";
	}
	
	@Test
	public void testNonSamsungFLow() throws MuleException, Exception{
		
		whenEndpointWithAddress("vm://nonSamsungOrder").thenReturn(muleMessageWithPayload(mockPurchaseReceipt()));
		
		MuleEvent response = runFlow("process-order", testEvent(getNonSamsungRequestPayload()));
		
		Assert.assertEquals(getNonSamsungResponsePayload(), response.getMessage().getPayloadAsString());
	}
	
	public String getNonSamsungRequestPayload(){
		
		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ord=\"http://orders.estore.mulesoft.com/\">"
						   +"<soapenv:Header/>"
						   +"<soapenv:Body>"
						   	+"<ord:processOrder>"
						   		+"<order>"
						   			+"<orderId>1</orderId>"
						   			+"<customer>"
						   				+"<address>Hyd</address>"
						   				+"<firstName>Naresh</firstName>"
						               +"<lastName>Podi</lastName>"
						            +"</customer>"
						            +"<orderItems>"
						               +"<item>"
						                  +"<manufacturer>Sony</manufacturer>"
						                  +"<name>LED</name>"
						                  +"<productId>32</productId>"
						                  +"<quantity>1</quantity>"
						               +"</item>"
						            +"</orderItems>"
						         +"</order>"
						      +"</ord:processOrder>"
						   +"</soapenv:Body>"
						+"</soapenv:Envelope>";
		
		return request;
	}
	
	public String getNonSamsungResponsePayload(){
		
		String nonSamsungResponse = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
								   +"<soap:Body>"
								   +"<ns2:processOrderResponse xmlns:ns2=\"http://orders.estore.mulesoft.com/\">"
									   +"<summary>"
								            +"<orderId>1</orderId>"
								            +"<customer>"
								               +"<address>Hyd</address>"
								               +"<firstName>Naresh</firstName>"
								               +"<lastName>Podi</lastName>"
								            +"</customer>"
								            +"<orderItems>"
								               +"<item>"
								                  +"<manufacturer>Sony</manufacturer>"
								                  +"<name>LED</name>"
								                  +"<productId>32</productId>"
								                  +"<purchaseReceipt>"
								                     +"<status>REJECTED</status>"
								                     +"<totalPrice>0.0</totalPrice>"
								                  +"</purchaseReceipt>"
								                  +"<quantity>1</quantity>"
								               +"</item>"
								            +"</orderItems>"
								         +"</summary>"
								      +"</ns2:processOrderResponse>"
								   +"</soap:Body>"
								+"</soap:Envelope>";
		
		return nonSamsungResponse;
	}
	
public PurchaseReceipt mockPurchaseReceipt(){
		
		PurchaseReceipt purchaseReceipt = new PurchaseReceipt(); 

		purchaseReceipt.setStatus(Status.REJECTED);
		purchaseReceipt.setTotalPrice(0);
		
		return purchaseReceipt;
		
	}
}
