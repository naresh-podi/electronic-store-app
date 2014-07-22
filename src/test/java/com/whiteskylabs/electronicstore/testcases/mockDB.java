package com.whiteskylabs.electronicstore.testcases;

import java.io.File;
import java.util.*;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class mockDB extends FunctionalMunitSuite{
	
	
	@Test
	public void testforDBMock() throws Exception
	{
	   //Mocking jdbc-endpoint with address
	       whenEndpointWithAddress("jdbc://Select").thenReturn( muleMessageWithPayload( getJDBCPayload() ) );
	   //invoke the flow
	       MuleEvent resultEvent = runFlow("mock-database",testEvent("Payload to the flow"));
	}
	
	public List getJDBCPayload()
	{
	   HashMap<String,String> mapObj1= new HashMap<String, String>();
	   HashMap<String,String> mapObj2= new HashMap<String, String>();

	   mapObj1.put("1", "indira");
	   mapObj1.put("2", "indira2");
	   mapObj2.put("3", "indira3");
	   mapObj2.put("4", "indira4");

	   List listObj = new ArrayList<String>();

	   listObj.add(mapObj1);
	   listObj.add(mapObj2);
	   return listObj;
	}
	@Override
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "mock-database.xml";
	}

}
