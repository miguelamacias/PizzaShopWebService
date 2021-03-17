package com.macisdev.pizzashopwebservice;

import com.macisdev.orders.Order;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebService(serviceName = "PizzaShopWebService")
public class PizzaShopService {

	private int waitingTime = 30;
	private final Logger log = Logger.getAnonymousLogger();

	@WebMethod(operationName = "sendOrder")
	public String sendOrder(String order) {
		if (validateXML(order)) {
			//Generates an ID for the order and attaches it to the received order
			String generatedId = generateOrderId();

			//Adds the order to the list of pending orders
			storeOrder(order, generatedId);

			log.log(Level.INFO, "Android processing ended --> {0}\n", new Date());

			return waitingTime + "_" + generatedId;
		} else {
			return null;
		}

	}

	@WebMethod(operationName = "getOrders")
	public List<String> getNewOrders(int waitingTime) {
		this.waitingTime = waitingTime;
		Session session;
		List<Order> ordersRetrieved;
		try {
			session = HibernateSingleton.getSession();
			Query<Order> query = session.createQuery("from Order where orderStatus = :status",
					Order.class);
			query.setParameter("status", Order.STATUS_RECEIVED_BY_SERVER);
			ordersRetrieved = query.getResultList();

			//Change the status order once they are retrieved by the desktop app
			Transaction transaction = session.beginTransaction();
			for (Order order : ordersRetrieved) {
				order.setOrderStatus(Order.STATUS_SENDED_TO_MANAGER);
				session.update(order);
			}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}

		return ParserXML.convertOrderListToStringList(ordersRetrieved, ParserXML.WEBSERVICE);
	}

	@WebMethod(operationName = "getUnfinishedOrders")
	public List<String> getUnfinishedOrders() {
		Session session;
		List<String> ordersRetrieved = null;
		try {
			session = HibernateSingleton.getSession();
			Query<Order> query = session.createQuery("from Order where orderStatus = :orderStatus",
					Order.class);
			query.setParameter("orderStatus", Order.STATUS_SENDED_TO_MANAGER);
			ordersRetrieved = ParserXML.convertOrderListToStringList(query.getResultList(), ParserXML.WEBSERVICE);
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return ordersRetrieved;
	}

	@WebMethod(operationName = "finalizeOrder")
	public void finalizeOrder(String orderId) {
		Session session;
		session = HibernateSingleton.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			Order order = session.get(Order.class, orderId);
			order.setOrderStatus(Order.STATUS_FINISHED);
			session.update(order);
			transaction.commit();

		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}

	private boolean validateXML(String xml) {
		Source schemaFile = new StreamSource(getClass().getClassLoader().
				getResourceAsStream("com/macisdev/pizzashopwebservice/resources/schema.xsd"));

		Source xmlFile = new StreamSource(new StringReader(xml));
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	private String generateOrderId() {
		StringBuilder id = new StringBuilder();
		try {
			Session session = HibernateSingleton.getSession();
			Query<Long> query = session.createQuery("SELECT count(*) from Order", Long.class);
			id.append(String.format("%05d", query.getSingleResult()));
			id.append("-");
			SecureRandom random = new SecureRandom();
			id.append(random.nextInt(899) + 100);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id.toString();
	}

	private void storeOrder (String orderString, String orderId) {
		Session session;
		Transaction transaction = null;
		try {
			Order order = ParserXML.parseXmlToOrder(orderString, ParserXML.WEBSERVICE);
			if (order != null) {
				order.setOrderId(orderId);
				order.setOrderStatus(Order.STATUS_RECEIVED_BY_SERVER);
				session = HibernateSingleton.getSession();
				transaction = session.beginTransaction();
				session.save(order);
				transaction.commit();
			}
		} catch (Exception e) {
			assert transaction != null;
			transaction.rollback();
			e.printStackTrace();
		}
	}

	@WebMethod(operationName = "getStoredOrder")
	public String getStoredOrder(String orderId) {
		Session session;
		try {
			session = HibernateSingleton.getSession();
			Order order = session.get(Order.class, orderId);

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
