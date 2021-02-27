package com.macisdev.pizzashopwebservice;

import com.macisdev.pizzashopwebservice.model.Order;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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

			Order order = ParserXML.parseXmlToOrder(orderXml, ParserXML.WEBSERVICE);
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
			Order order = ParserXML.parseXmlToOrder(orderString, ParserXML.WEBSERVICE);
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
	public String getStoredOrder(String orderId) {
		Session session;
		try {
			session = HibernateSingleton.getSession();
			Order order = session.get(Order.class, orderId);
			System.out.println(order.toString());

			return ParserXML.parseOrderToXml(order, ParserXML.WEBSERVICE);
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "getStoredOrdersByPhoneNumber")
	public List<String> getStoredOrdersByPhoneNumber(String phoneNumber) {
		Session session;
		List<String> ordersRetrieved = null;
		try {
			session = HibernateSingleton.getSession();
			Query<Order> query = session.createQuery("from Order where customerPhone = :phoneNumber",
					Order.class);
			query.setParameter("phoneNumber", phoneNumber);
			ordersRetrieved = ParserXML.convertOrderListToStringList(query.getResultList(), ParserXML.WEBSERVICE);
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return ordersRetrieved;
	}

	@WebMethod(operationName = "getAllStoredOrders")
	public List<String> getAllStoredOrders() {
		Session session;
		List<String> ordersRetrieved = null;
		try {
			session = HibernateSingleton.getSession();
			Query<Order> query = session.createQuery("from Order", Order.class);
			ordersRetrieved = ParserXML.convertOrderListToStringList(query.getResultList(), ParserXML.WEBSERVICE);
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return ordersRetrieved;

	}
}
