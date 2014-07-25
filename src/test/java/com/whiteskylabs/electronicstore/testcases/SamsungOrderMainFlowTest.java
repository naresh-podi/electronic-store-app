package com.whiteskylabs.electronicstore.testcases;

import junit.framework.Assert;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import com.mulesoft.estore.orders.PurchaseReceipt;
import com.mulesoft.estore.orders.Status;

public class SamsungOrderMainFlowTest extends FunctionalMunitSuite {

	
	@Override
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "process-samsung-items.xml,process-non-samsung-items.xml,get-samsung-price-service.xml,get-non-samsung-price-service.xml,electronics-store.xml,database-initialisation.xml,audit-service.xml";
	}
	
	@Test
	public void testSamsungFLow() throws MuleException, Exception{
		
		whenEndpointWithAddress("vm://samsungOrder").thenReturn(muleMessageWithPayload(mockPurchaseReceipt()));
		
		MuleEvent response = runFlow("process-order", testEvent(getSamsungRequestPayload()));
		
		System.out.println(":::::RESPONSE:::::"+response.getMessage().getPayloadAsString());
		
		Assert.assertEquals(getSamsungResponsePayload(), response.getMessage().getPayloadAsString());
	}
	
	public String getSamsungRequestPayload(){
		
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
						                  +"<manufacturer>Samsung</manufacturer>"
						                  +"<name>Note 3</name>"
						                  +"<productId>2</productId>"
						                  +"<quantity>3</quantity>"
						               +"</item>"
						            +"</orderItems>"
						         +"</order>"
						      +"</ord:processOrder>"
						   +"</soapenv:Body>"
						+"</soapenv:Envelope>";
		
		return request;
	}
	
	public String getSamsungResponsePayload(){
		
		String samsungResponse = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
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
								                  +"<manufacturer>Samsung</manufacturer>"
								                  +"<name>Note 3</name>"
								                  +"<productId>2</productId>"
								                  +"<purchaseReceipt>"
								                     +"<id>1</id>"
								                     +"<status>ACCEPTED</status>"
								                     +"<totalPrice>2100.0</totalPrice>"
								                  +"</purchaseReceipt>"
								                  +"<quantity>3</quantity>"
								               +"</item>"
								            +"</orderItems>"
								         +"</summary>"
								      +"</ns2:processOrderResponse>"
								   +"</soap:Body>"
								+"</soap:Envelope>";
		
		return samsungResponse;
		
	}
	
public PurchaseReceipt mockPurchaseReceipt(){
		
		PurchaseReceipt purchaseReceipt = new PurchaseReceipt(); 

		purchaseReceipt.setId("1");
		purchaseReceipt.setStatus(Status.ACCEPTED);
		purchaseReceipt.setTotalPrice(2100);
		
		return purchaseReceipt;
		
	}
}
