/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.macisdev.pizzashopwebservice;

import com.macisdev.orders.Order;
import com.macisdev.orders.OrderElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


public class ParserXML {
	public static final int WEBSERVICE = 1;
	public static final int RESTAURANT = 2;
	public static Order parseXmlToOrder(String xml, int client) {
		Order order = new Order();
		Document document;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
					new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


		//Get order header
		NodeList orderInfo = document.getElementsByTagName("order_info");

		Node nodeOrderInfo = orderInfo.item(0);

		Element elementOrderInfo = (Element) nodeOrderInfo;

		if (client == RESTAURANT) {
			order.setOrderId(elementOrderInfo.getElementsByTagName("order_id").item(0).getTextContent());
			order.setOrderDateTime(elementOrderInfo.getElementsByTagName("order_datetime").item(0).getTextContent());
		}

		if (client == WEBSERVICE) {
			order.setOrderDateTime(parseDateTime(Long.parseLong(
							elementOrderInfo.getElementsByTagName("order_datetime").item(0).getTextContent())));
		}
		order.setCustomerName(elementOrderInfo.getElementsByTagName("customer_name").item(0).getTextContent());
		order.setCustomerPhone(elementOrderInfo.getElementsByTagName("customer_phone").item(0).getTextContent());
		order.setDeliveryMethod(elementOrderInfo.getElementsByTagName("delivery_method").item(0).getTextContent());
		order.setCustomerAddress(elementOrderInfo.getElementsByTagName("customer_address").item(0).getTextContent());
		order.setPaymentMethod(elementOrderInfo.getElementsByTagName("payment_method").item(0).getTextContent());
		order.setOrderStatus(Integer.parseInt(
				elementOrderInfo.getElementsByTagName("order_status").item(0).getTextContent()));
		double totalPrice = Double.parseDouble(elementOrderInfo.getElementsByTagName("total_price").
				item(0).getTextContent());
		order.setTotalPrice(totalPrice);

		//Parse order elements		
		NodeList orderElementsList = document.getElementsByTagName("pizza");

		for (int i = 0; i < orderElementsList.getLength(); i++) {
			Node currentNode = orderElementsList.item(i);

			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				//Creates the orderElement object to add its details after being parsed from the xml
				OrderElement orderElement = new OrderElement();

				Element currentElement = (Element) currentNode;
				orderElement.setCode(currentElement.getElementsByTagName("code").item(0).getTextContent());
				orderElement.setName(currentElement.getElementsByTagName("name").item(0).getTextContent());
				orderElement.setSize(currentElement.getElementsByTagName("size").item(0).getTextContent());
				orderElement.setExtras(currentElement.getElementsByTagName("extras").item(0).getTextContent());
				double price = Double.parseDouble(currentElement.getElementsByTagName("price").
						item(0).getTextContent());
				orderElement.setPrice(price);

				//Adds the element parsed to the order
				order.addElementToOrder(orderElement);
			}
		}
		return order;
	}

	public static String parseOrderToXml(Order order, int client) {
		//Creates the document
		Document document;

		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}

		//Creates the root element
		Element root = document.createElement("order");
		document.appendChild(root);

		//Creates the order_info node
		Element orderInfo = document.createElement("order_info");
		root.appendChild(orderInfo);

		//Create the elements for the actual information about the order

		if (client == WEBSERVICE) {
			Element orderId = document.createElement("order_id");
			orderId.appendChild(document.createTextNode(order.getOrderId()));
			orderInfo.appendChild(orderId);
		}

		Element orderDateTime = document.createElement("order_datetime");
		orderDateTime.appendChild(document.createTextNode(order.getOrderDateTime()));
		orderInfo.appendChild(orderDateTime);

		Element name = document.createElement("customer_name");
		name.appendChild(document.createTextNode(order.getCustomerName()));
		orderInfo.appendChild(name);

		Element phone = document.createElement("customer_phone");
		phone.appendChild(document.createTextNode(order.getCustomerPhone()));
		orderInfo.appendChild(phone);

		Element delivery = document.createElement("delivery_method");
		delivery.appendChild(document.createTextNode(order.getDeliveryMethod()));
		orderInfo.appendChild(delivery);

		Element address = document.createElement("customer_address");
		address.appendChild(document.createTextNode(order.getCustomerAddress()));
		orderInfo.appendChild(address);

		Element payment = document.createElement("payment_method");
		payment.appendChild(document.createTextNode(order.getPaymentMethod()));
		orderInfo.appendChild(payment);

		Element status = document.createElement("order_status");
		status.appendChild(document.createTextNode(String.valueOf(order.getOrderStatus())));
		orderInfo.appendChild(status);

		//Adds the total price to the xml file
		double totalPrice = 0;
		for (OrderElement element : order.getOrderElements()) {
			totalPrice += element.getPrice();
		}

		Element price = document.createElement("total_price");
		price.appendChild(document.createTextNode(String.format("%.2f", totalPrice)));
		orderInfo.appendChild(price);

		//Add the ordered stuffs to the xml (suppose everything is a pizza for simplicity)
		for (OrderElement currentElement : order.getOrderElements()) {
			Element pizza = document.createElement("pizza");
			root.appendChild(pizza);

			Element pizzaCode = document.createElement("code");
			pizzaCode.appendChild(document.createTextNode(String.valueOf(currentElement.getCode())));
			pizza.appendChild(pizzaCode);

			Element pizzaName = document.createElement("name");
			pizzaName.appendChild(document.createTextNode(currentElement.getName()));
			pizza.appendChild(pizzaName);

			Element pizzaSize = document.createElement("size");
			pizzaSize.appendChild(document.createTextNode(currentElement.getSize()));
			pizza.appendChild(pizzaSize);

			Element pizzaExtras = document.createElement("extras");
			pizzaExtras.appendChild(document.createTextNode(currentElement.getExtras()));
			pizza.appendChild(pizzaExtras);

			Element pizzaPrice = document.createElement("price");
			pizzaPrice.appendChild(document.createTextNode(String.valueOf(currentElement.getPrice())));
			pizza.appendChild(pizzaPrice);
		}

		return transformDOMDocumentToString(document);

	}

	public static String transformDOMDocumentToString(Document document) {
		try {
			DOMSource domSource = new DOMSource(document);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Order> convertStringToOrderList(List<String> stringList, int client) {
		List<Order> orderList = new ArrayList<>();

		for (String order : stringList) {
			orderList.add(ParserXML.parseXmlToOrder(order, client));
		}

		return orderList;
	}

	public static List<String> convertOrderListToStringList(List<Order> orderList, int client) {
		List<String> stringList = new ArrayList<>();

		for (Order order : orderList) {
			stringList.add(parseOrderToXml(order, client));
		}

		return stringList;
	}

	public static String parseDateTime(long milliSeconds) {
		ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(milliSeconds), ZoneId.systemDefault());

		return String.format("%02d/%02d/%d - %02d:%02d:%02d",
				dateTime.getDayOfMonth(), dateTime.getMonth().getValue(), dateTime.getYear(),
				dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
	}
}
