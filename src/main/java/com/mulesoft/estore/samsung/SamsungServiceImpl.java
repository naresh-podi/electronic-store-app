package com.mulesoft.estore.samsung;

import javax.jws.WebService;

@WebService(endpointInterface="com.mulesoft.estore.samsung.SamsungService", name="SamsungService")
public class SamsungServiceImpl implements SamsungService {

	private String samsungPrice;
	
	@Override
	public OrderResponse purchase(OrderRequest orderRequest) {
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setId("1");
		orderResponse.setResult("ACCEPTED");
		// get Samsung product price from properties file
		Integer price = Integer.parseInt(getSamsungPrice()) * orderRequest.getQuantity();
		orderResponse.setPrice(price.toString());
		return orderResponse;
	}

	public String getSamsungPrice() {
		return samsungPrice;
	}

	public void setSamsungPrice(String samsungPrice) {
		this.samsungPrice = samsungPrice;
	}

	
	
}
