package com.mulesoft.estore.orders;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Interface for order processing.
 * 
 */
@WebService
public interface IProcessOrder {
	/**
	 * Process an order.
	 * 
	 * @param order
	 * @return
	 */
	@WebResult(name = "summary")
	Order processOrder(@WebParam(name = "order") Order order);
}
