package com.whiteskylabs.estore;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Logger;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.module.db.internal.domain.database.GenericDbConfig;

public class DBSetup implements Callable {

	public Connection connectionObj = null;
	public PreparedStatement psObj = null;
	private final static Logger log = Logger.getLogger(DBSetup.class.getName());
	LinkedHashMap<String, String> createQueries = null;

	public GenericDbConfig derbyConnector;

	public GenericDbConfig getDerbyConnector() {
		return derbyConnector;
	}

	public void setDerbyConnector(GenericDbConfig derbyConnector) {
		this.derbyConnector = derbyConnector;
	}

	/**
	 * @param query
	 *            - this method executes valid database query
	 * @throws Exception
	 */
	public void executeQueries(String query) throws Exception {
		psObj = connectionObj.prepareStatement(query);
		psObj.execute();
	}

	/**
	 * inserts following products details into 'PRODUCTS table
	 * 
	 * 1 SAMSUNG S-1 12345 2 SAMSUNG S-2 2100 3 SAMSUNG S-3 2200 4 SAMSUNG S-4
	 * 4500 5 SAMSUNG S-5 40987 6 PHILIPS P-1 12345 7 BAJAJ B1 9870 8 BAJAJ B-2
	 * 12345 9 APPLE A-1 12345 10 APPLE A-2 12345
	 * 
	 * @throws Exception
	 */
	public void updateProducts() throws Exception {
		String insertQueries[] = { "INSERT INTO PRODUCTS	(MANUFACTURER, PRODUCT_NAME, PRICE) VALUES 	('Samsung', 'S-1',12345 ),('Samsung', 'S-2',2100 ), ('Samsung', 'S-3',2200 ), ('Samsung', 'S-4',4500), ('Samsung', 'S-5',40987 ), ('Philips', 'P-1',12345 ), ('Bajaj', 'B1',9870), ('Bajaj', 'B-2',12345 ), ('Apple', 'A-1',12345 ), ('Apple', 'A-2',12345 )" };

		for (String key : insertQueries) {
			executeQueries(key.toUpperCase());
		}

	}

	/**
	 * Creates following tables
	 *  	1. PRODUCTS
	 * 		2. ORDERS
	 * 		3. ORDERS_AUDITS
	 * and 
	 * insert to following tables
	 * 		1. PRODUCTS
	 */
	public void dbSetup() {
		createQueries = new LinkedHashMap<String, String>();

		createQueries.put("PRODUCTS", "CREATE TABLE PRODUCTS" + "("
				+ "PRODUCT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,"
				+ "MANUFACTURER VARCHAR(255)," + "PRODUCT_NAME VARCHAR(255),"
				+ "PRICE INTEGER NOT NULL," + "PRIMARY KEY (PRODUCT_ID)" + ")");

		createQueries.put("ORDERS", "CREATE TABLE ORDERS" + "("
				+ "ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,"
				+ "ORDER_ID VARCHAR(255) NOT NULL,"
				+ "PRODUCT_ID INTEGER NOT NULL," + "QUANTITY INTEGER NOT NULL,"
				+ "STATUS VARCHAR(255) DEFAULT 'ACCEPTED', "
				+ "PRIMARY KEY (ID),"
				+ "FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(PRODUCT_ID)"
				+ ")");

		createQueries.put("ORDERS_AUDITS", "CREATE TABLE ORDERS_AUDITS" + "("
				+ "ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,"
				+ "ORDER_ID VARCHAR(255) NOT NULL,"
				+ "TOTAL_AMOUNT FLOAT NOT NULL," + "PRIMARY KEY (ORDER_ID)"
				+ ")");
		try {
			log.info("--------------DB Setup started--------------");

			connectionObj = derbyConnector.getDataSource().getConnection();
			DatabaseMetaData dbmd = connectionObj.getMetaData();

			Set<String> createQuerieskeys = createQueries.keySet();
			for (String tableName : createQuerieskeys) {
				ResultSet rs = dbmd.getTables(null, null, tableName, null);
				if (!rs.next()) {
					executeQueries(createQueries.get(tableName).toUpperCase());
					if (tableName == "PRODUCTS") {
						updateProducts();
					}
					log.info(tableName + " table is created");
				}
			}
			
			log.info("--------------DB Setup completed successfully--------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mule.api.lifecycle.Callable#onCall(org.mule.api.MuleEventContext)
	 */
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		dbSetup();
		eventContext.getMessage().setPayload("DB Setup completed successfully");
		return eventContext.getMessage();
	}
}
