package com.mulesoft.estore.samsung;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

public class OrderItemToOrderRequest extends AbstractTransformer {

	@Override
	protected Object doTransform(Object src, String enc)
			throws TransformerException {
		OrderRequest req = new OrderRequest();
		req.setName("eStore");
		return req;
	}

}
