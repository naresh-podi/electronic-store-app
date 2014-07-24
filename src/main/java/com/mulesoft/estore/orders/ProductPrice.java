package com.mulesoft.estore.orders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/prices")
public class ProductPrice {

	@GET
	@Path("/{productId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getProductPrice(@PathParam("productId") String productId)
			throws Exception {

		return getPrice(Integer.parseInt(productId));
	}

	
	/** Get Product price
	 * @param productId Id of the Product
	 * @return Product price
	 * @throws Exception
	 */
	public String getPrice(int productId) throws Exception {

		String getPriceQry = "SELECT PRICE FROM PRODUCTS WHERE PRODUCT_ID ="
				+ productId;
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		String dbURL = "jdbc:derby:muleEmbeddedDB;create=true";

		System.out.println("--------------Getting Non-Samsung product price started--------------");

		
		// Derby DB connection
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(dbURL);
		Statement stmt = connection.createStatement();
		
		// Get records
		ResultSet rs = stmt.executeQuery(getPriceQry);

		// Iterate and get the product price.
		if (rs.next()) {
			String price = rs.getString("PRICE");
			System.out.println("PRICE: " + price);
			System.out
					.println("--------------Retreived Non-Samsung product price successfully--------------");

			return price;
		} else {
			throw new Exception("Product Id: " +productId+" doesnt exist in PRODUCTS table");
		}
	}
}
