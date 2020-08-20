package com.macisdev.pizzashopwebservice;

import com.macisdev.orders.Order;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

	private boolean storeOrder (String orderString) {
		Session session = null;
		try {
			Order order = ParserXML.parseXmlToOrder(orderString);
			session = HibernateSingleton.getSession();
			Transaction transaction = session.beginTransaction();
			session.save(order);
			transaction.commit();
			session.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}

	@WebMethod(operationName = "getStoredOrder")
	public Order getStoredOrder(String orderId) {
		Session session = null;
		try {
			session = HibernateSingleton.getSession();
			Order order = (Order) session.load(Order.class, orderId);
			return order;
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
	}
}
