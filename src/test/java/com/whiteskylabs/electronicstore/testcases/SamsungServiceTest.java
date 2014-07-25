package com.whiteskylabs.electronicstore.testcases;

import java.io.File;
import java.util.Properties;

import junit.framework.Assert;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class SamsungServiceTest extends FunctionalMunitSuite {

	@Test
	public void testSamsungService() throws Exception {

		MuleEvent responseEvent = runFlow("get-samsung-product-price-service", testEvent(getRequestPayload()));
		System.out.println(responseEvent.getMessage().getPayloadAsString());
		Assert.assertEquals(getExpectedResponsePayload(),responseEvent.getMessage().getPayloadAsString());
	}

	public String getRequestPayload() {

		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sam=\"http://samsung.estore.mulesoft.com/\">"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ "<sam:purchase>"
				+ "<orderRequest>"
				+ "<name>Galaxy</name>"
				+ "<quantity>12</quantity>"
				+ "</orderRequest>"
				+ "</sam:purchase>" + "</soapenv:Body>" + "</soapenv:Envelope>";
		return request;
	}

	public String getExpectedResponsePayload() {

		String request = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<ns2:purchaseResponse xmlns:ns2=\"http://samsung.estore.mulesoft.com/\">"
				+ "<orderResponse>"
				+ "<id>1</id>"
				+ "<result>ACCEPTED</result>"
				+ "<price>27000</price>"
				+ "</orderResponse>" + "</ns2:purchaseResponse>"
				+ "</soap:Body>" + "</soap:Envelope>";
		return request;
	}

	@Override
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "electronics-store.xml, get-samsung-price-service.xml";
	}

	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());
		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, new File(
				"mappings").getAbsolutePath());
		return properties;
	}

}
