package com.macisdev.pizzaswebservice;

import java.io.IOException;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;


@WebService(serviceName = "PizzaShopWebService")
public class PizzaShopService {

	private static ArrayList<String> orderList = new ArrayList<>();

	@WebMethod(operationName = "sendOrder")
	public boolean sendOrder(String order) throws IOException {

		//Adds the order to the list of pending orders
		orderList.add(order);
		System.out.println("Android processing ended\n");
		return true;
	}

	@WebMethod(operationName = "getOrders")
	public ArrayList<String> getOrders() {
		ArrayList<String> copyToSend = (ArrayList <String>)orderList.clone();
		orderList.clear();
		System.out.println("Orders retrieved by restaurant: " + copyToSend.size());
		return copyToSend;
	}
}
