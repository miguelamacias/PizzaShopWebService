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
import java.io.StringReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class ParserXML {
	public static Order parseXmlToOrder(String xml) throws Exception {
		Order order = new Order();
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
		document.getDocumentElement().normalize();


		//Get order header
		NodeList orderInfo = document.getElementsByTagName("order_info");
		
		Node nodeOrderInfo = orderInfo.item(0);

		Element elementOrderInfo = (Element) nodeOrderInfo;
		String orderId = elementOrderInfo.getElementsByTagName("order_id").item(0).getTextContent();
		order.setOrderId(orderId);
		order.setOrderDateTime(parseDateTime(Long.parseLong(orderId)));
		order.setCustomerName(elementOrderInfo.getElementsByTagName("customer_name").item(0).getTextContent());
		order.setCustomerPhone(elementOrderInfo.getElementsByTagName("customer_phone").item(0).getTextContent());
		order.setDeliveryMethod(elementOrderInfo.getElementsByTagName("delivery_method").item(0).getTextContent());
		order.setCustomerAddress(elementOrderInfo.getElementsByTagName("customer_address").item(0).getTextContent());
		order.setPaymentMethod(elementOrderInfo.getElementsByTagName("payment_method").item(0).getTextContent());
		double totalPrice = Double.parseDouble(elementOrderInfo.getElementsByTagName("total_price").
				item(0).getTextContent());
		order.setTotalPrice(totalPrice);
		
		//Parse order elements		
		NodeList orderElementsList = document.getElementsByTagName("pizza");
		
		for (int i = 0; i < orderElementsList.getLength(); i++) {
			Node currentNode = orderElementsList.item(i);
			
			if(currentNode.getNodeType() == Node.ELEMENT_NODE) {
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

	public static String parseDateTime(long milliSeconds) {
		ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(milliSeconds), ZoneId.systemDefault());

		return String.format("%02d/%02d/%d - %02d:%02d:%02d",
				dateTime.getDayOfMonth(), dateTime.getMonth().getValue(), dateTime.getYear(),
				dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
	}
}
