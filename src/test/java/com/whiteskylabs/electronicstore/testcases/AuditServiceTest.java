package com.whiteskylabs.electronicstore.testcases;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.munit.runner.functional.FunctionalMunitSuite;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class AuditServiceTest extends FunctionalMunitSuite {
	
	@Override
	protected String getConfigResources() {
		return "audit-service.xml,electronics-store.xml";
	}
	
	@Test
	public void testAuditService() throws Exception {

		//String payload = "orderId:4";
		MuleEvent testEvent = testEvent("") ;
		testEvent.getMessage().setPayload("orderId:4");
		testEvent.getMessage().setSessionProperty("totalValue", "50001");
		
		
		MuleEvent responseEvent = runFlow("audit-service", testEvent);
		System.out.println("asdasdasdasdasd::::::::::::"+responseEvent.getMessage().getPayloadAsString());
		
		//assertEquals("1", responseEvent.getMessage().getPayloadAsString());
		assertNotNull(responseEvent.getMessage());
	}

//	public HashMap getJDBCPayload() {
//		HashMap<String, String> mapObj1 = new HashMap<String, String>();
//		mapObj1.put("orderId", "2");
//		return mapObj1;
//	}
//
//	
//
//	@Override
//	protected Properties getStartUpProperties() {
//		Properties properties = new Properties(super.getStartUpProperties());
//		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, new File(
//				"mappings").getAbsolutePath());
//		return properties;
//	}

}
