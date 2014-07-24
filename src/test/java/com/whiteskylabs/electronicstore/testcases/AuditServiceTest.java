package com.whiteskylabs.electronicstore.testcases;

import java.io.File;
import java.util.Properties;
import junit.framework.Assert;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class AuditServiceTest extends FunctionalMunitSuite {
	@Test
	public void testAuditService() throws Exception {

		MuleEvent testEvent = testEvent( "" );
		testEvent.setFlowVariable("orderId", "2");
		testEvent.setSessionVariable("totalValue", "12001");
		MuleEvent responseEvent = runFlow("audit-service", testEvent);
		System.out.println(responseEvent.getMessage().getPayloadAsString());
		Assert.assertEquals("1", responseEvent.getMessage()
				.getPayloadAsString());
	}

	@Override
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "audit-service.xml, electronics-store.xml";
	}

	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());
		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, new File(
				"mappings").getAbsolutePath());
		return properties;
	}

}
