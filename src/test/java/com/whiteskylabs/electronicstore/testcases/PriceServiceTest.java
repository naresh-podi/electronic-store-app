package com.whiteskylabs.electronicstore.testcases;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class PriceServiceTest extends FunctionalMunitSuite {

	@Override
	protected boolean haveToDisableInboundEndpoints() {
		return false;
	}

	@Override
	protected List<String> getFlowsExcludedOfInboundDisabling() {
		String flowName = "price-service";
		ArrayList<String> mylist = new ArrayList<String>();
		mylist.add(flowName);
		return mylist;
	}

	@Test
	public void testRestMocking() throws MuleException, Exception {

		MuleClient client = muleContext.getClient();
		MuleMessage response = client.send(
				"http://localhost:8088/api/prices/1", createHTTPPayload());
		Assert.assertEquals("12345", response.getPayloadAsString());
	}

	public MuleMessage createHTTPPayload() {

		MuleMessage muleMessage = new DefaultMuleMessage(null, muleContext);
		muleMessage.setProperty("http.method", "GET", PropertyScope.OUTBOUND);
		muleMessage.setProperty("Content-Type", "text/plain",
				PropertyScope.INBOUND);
		return muleMessage;
	}

}
