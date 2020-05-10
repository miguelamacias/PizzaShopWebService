package com.macisdev.pizzashopwebservice;

import java.io.IOException;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;


@WebService(serviceName = "PizzaShopWebService")
public class PizzaShopService {

	private static ArrayList<String> orderList = new ArrayList<>();
	private int waitingTime = 30;

	@WebMethod(operationName = "sendOrder")
	public int sendOrder(String order) throws IOException {

		//Adds the order to the list of pending orders
		orderList.add(order);
		System.out.println("Android processing ended\n");
		return waitingTime;
	}

	@WebMethod(operationName = "getOrders")
	public ArrayList<String> getOrders(int waitingTime) {
		ArrayList<String> copyToSend = new ArrayList<>(orderList);
		orderList.clear();

		this.waitingTime = waitingTime;
		return copyToSend;
	}
}
