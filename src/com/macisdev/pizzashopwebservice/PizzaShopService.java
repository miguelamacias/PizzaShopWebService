package com.macisdev.pizzashopwebservice;

import com.macisdev.orders.Order;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@WebService(serviceName = "PizzaShopWebService")
public class PizzaShopService {

	private static final ArrayList<String> orderList = new ArrayList<>();
	private int waitingTime = 30;

	@WebMethod(operationName = "sendOrder")
	public String sendOrder(String order) {
		//Generates an ID for the order and attaches it to the received order
		String generatedId = generateOrderId(order);
		String orderWithId = attachIdToOrder(order, generatedId);

		//Adds the order to the list of pending orders
		orderList.add(orderWithId);
		storeOrder(order, generatedId);
		System.out.println("Android processing ended\n");
		return waitingTime + "_" + generatedId;
	}

	@WebMethod(operationName = "getOrders")
	public ArrayList<String> getOrders(int waitingTime) {
		ArrayList<String> copyToSend = new ArrayList<>(orderList);
		orderList.clear();

		this.waitingTime = waitingTime;
		return copyToSend;
	}

	private String generateOrderId(String orderXml) {
		StringBuilder id = new StringBuilder();
		try {
			Session session = HibernateSingleton.getSession();
			Query<Long> query = session.createQuery("SELECT count(*) from Order", Long.class);
			id.append(String.format("%05d", query.getSingleResult()));
			id.append("-");

			Order order = ParserXML.parseXmlToOrder(orderXml);
			id.append(order.getCustomerPhone(), 0, 3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id.toString();
	}

	private String attachIdToOrder(String order, String id) {
		try {
			//Creates a xml document using the xml string
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new StringReader(order)));
			Document document = parser.getDocument();

			//Adds a generated id to the xml
			Node orderInfo = document.getElementsByTagName("order_info").item(0);
			Element orderId = document.createElement("order_id");
			orderId.appendChild(document.createTextNode(id));
			orderInfo.appendChild(orderId);

			return ParserXML.transformDOMDocumentToString(document);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private void storeOrder (String orderString, String orderId) {
		Session session;
		try {
			Order order = ParserXML.parseXmlToOrder(orderString);
			order.setOrderId(orderId);
			session = HibernateSingleton.getSession();
			Transaction transaction = session.beginTransaction();
			session.save(order);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@WebMethod(operationName = "getStoredOrder")
	public Order getStoredOrder(String orderId) {
		Session session;
		try {
			session = HibernateSingleton.getSession();
			return session.get(Order.class, orderId);
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "getStoredOrdersByPhoneNumber")
	public List<Order> getStoredOrdersByPhoneNumber(String phoneNumber) {
		Session session;
		List<Order> ordersRetrieved = null;
		try {
			session = HibernateSingleton.getSession();
			Query<Order> query = session.createQuery("from Order where customerPhone = :phoneNumber",
					Order.class);
			query.setParameter("phoneNumber", phoneNumber);
			ordersRetrieved = query.getResultList();
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return ordersRetrieved;
	}

	@WebMethod(operationName = "getAllStoredOrders")
	public List<Order> getAllStoredOrders() {
		Session session;
		List<Order> ordersRetrieved = null;
		try {
			session = HibernateSingleton.getSession();
			Query<Order> query = session.createQuery("from Order", Order.class);
			ordersRetrieved = query.getResultList();
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return ordersRetrieved;

	}
}
