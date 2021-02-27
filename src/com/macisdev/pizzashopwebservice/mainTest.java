package com.macisdev.pizzashopwebservice;


import com.macisdev.orders.Order;

import java.util.ArrayList;
import java.util.List;

public class mainTest {
	public static void main(String[] args) {
		try {
			Order order1 = ParserXML.parseXmlToOrder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><order><order_info><order_id>1587922864850</order_id><order_datetime>1613410273659</order_datetime><customer_name>Miguel Angel Macias</customer_name><customer_phone>649425570</customer_phone><delivery_method>Envío a domicilio</delivery_method><customer_address>C/Valerito 36</customer_address><payment_method>Tarjeta</payment_method><total_price>31.20</total_price></order_info><pizza><code>1</code><name>Monster</name><size>Mediana</size><extras>EXTRA: Champiñones, Pepperoni, Atún, Cebolla, Pimiento, 4 Quesos, Aceitunas </extras><price>8.5</price></pizza><pizza><code>4</code><name>Barbacoa</name><size>Familiar</size><extras>EXTRA: Ternera </extras><price>16.2</price></pizza><pizza><code>8</code><name>Hawaiana</name><size>Mediana</size><extras>SIN: Piña </extras><price>6.5</price></pizza></order>", ParserXML.WEBSERVICE);
			order1.setOrderId("123_123");
			System.out.println(order1.toString() + "\n" + order1.getOrderElements().get(0));
			String order1Xml = ParserXML.parseOrderToXml(order1, ParserXML.WEBSERVICE);
			System.out.println(order1Xml);
			List<Order> orderList = new ArrayList<>();
			orderList.add(order1);
			orderList.add(order1);
			orderList.add(order1);

			List<String> stringList = ParserXML.convertOrderListToStringList(orderList, ParserXML.WEBSERVICE);

			System.out.println(stringList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
