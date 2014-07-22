package com.whiteskylabs.electronicstore.testcases;

import java.io.File;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class OrderServiceTest extends FunctionalMunitSuite{
	
	@Test
	public void testFlightInformation() throws Exception {

		MuleEvent responseEvent = runFlow("orderService", testEvent(getRequestPayload()));
		System.out.println(responseEvent.getMessage().getPayloadAsString());
		//assertEquals(getExpectedResponsePayload(),responseEvent.getMessage().getPayloadAsString());
		Assert.assertNotNull(responseEvent.getMessage().getPayload());
	}

	public String getRequestPayload() {

		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ord=\"http://orders.se.mulesoft.com/\">"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ "<ord:processOrder>"
				+ "<order>"
				+ "<orderId>12</orderId>"
				+"<customer>"
				  + "<address>Hyderabad</address>"
				  + "<firstName>Denni</firstName>"
				  + "<lastName>Podi</lastName>"
				+"</customer>"
				+ "<orderItems>"
				+ "<item>"
				  + "<manufacturer>Samsung</manufacturer>"
				  + "<name>s-1</name>"
				  + "<productId>33333</productId>"
				  + "<quantity>40</quantity>"
				+ "</item>"
				+ "<item>"
				  + "<manufacturer>Philips</manufacturer>"
				  + "<name>i-2</name>"
				  + "<productId>1234</productId>"
				  + "<quantity>4</quantity>"
				+ "</item>"
				+ "<item>"
				  + "<manufacturer>Philips</manufacturer>"
				  + "<name>s-3</name>"
				  + "<productId>3</productId>"
				  + "<quantity>4</quantity>"
				+ "</item>"
				+ "<item>"
				  + "<manufacturer>Philips</manufacturer>"
				  + "<name>i-4</name>"
				  + "<productId>4</productId>"
				  + "<quantity>4</quantity>"
				+ "</item>"
				+ "</orderItems>"
				+ "</order>" + "</ord:processOrder>" + "</soapenv:Body>" + "</soapenv:Envelope>";
		return request;
	}

	/*public String getExpectedResponsePayload() {

		String request = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<ns2:purchaseResponse xmlns:ns2=\"http://samsung.se.mulesoft.com/\">"
				+ "<orderResponse>"
				+ "<id>1</id>"
				+ "<result>ACCEPTED</result>"
				+ "<price>30600</price>"
				+ "</orderResponse>" + "</ns2:purchaseResponse>"
				+ "</soap:Body>" + "</soap:Envelope>";
		return request;
	}*/

	@Override
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "electronics-store.xml";
	}

	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());
		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, new File(
				"mappings").getAbsolutePath());
		return properties;
	}

}
