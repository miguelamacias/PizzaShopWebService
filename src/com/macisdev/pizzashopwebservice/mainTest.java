package com.macisdev.pizzashopwebservice;


import com.macisdev.orders.Order;

public class mainTest {
	public static void main(String[] args) {
		try {
			Order order = ParserXML.parseXmlToOrder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><order><order_info><order_datetime>1616518900531</order_datetime><customer_name>Miguel Ángel Macías</customer_name><customer_phone>649425570</customer_phone><delivery_method>Recoger en local</delivery_method><customer_address>Recoger en local</customer_address><payment_method>Efectivo</payment_method><order_status>0</order_status><total_price>91.90</total_price></order_info><pizza><code>1</code><name>Margarita</name><size>Mediana</size><extras>EXTRA: Jamón York </extras><price>6.0</price></pizza><pizza><code>6</code><name>Carbonara</name><size>Familiar</size><extras>EXTRA: Champiñones, Cebolla </extras><price>16.8</price></pizza><pizza><code>10</code><name>Mixta</name><size>Mediana</size><extras/><price>7.5</price></pizza><pizza><code>9</code><name>Hawaiana</name><size>Mediana</size><extras>\n" +
					"SIN: Piña</extras><price>6.5</price></pizza><pizza><code>103</code><name>Patatas con cheddar y bacon</name><size>Normal</size><extras/><price>4.5</price></pizza><pizza><code>105</code><name>Nuggets de pollo</name><size>Ración: 10 unidades</size><extras/><price>4.0</price></pizza><pizza><code>108</code><name>Salsas</name><size>Churrasco</size><extras/><price>1.1</price></pizza><pizza><code>201</code><name>Pasta carbonara</name><size>Tagliatelle</size><extras/><price>5.1</price></pizza><pizza><code>207</code><name>Serranito</name><size>Cerdo</size><extras/><price>4.0</price></pizza><pizza><code>205</code><name>Hamburguesa casera completa</name><size>Sin patatas</size><extras>\n" +
					"SIN: Tomate</extras><price>4.5</price></pizza><pizza><code>301</code><name>Coca-Cola</name><size>Zero Botella 2L</size><extras/><price>2.5</price></pizza><pizza><code>306</code><name>Cruzcampo</name><size>Litrona 1L</size><extras/><price>1.8</price></pizza><pizza><code>11</code><name>4 Estaciones</name><size>Mediana</size><extras>EXTRA: Gambas \n" +
					"SIN: Alcachofas</extras><price>7.8</price></pizza><pizza><code>8</code><name>Di Marco</name><size>Mediana</size><extras/><price>6.8</price></pizza><pizza><code>13</code><name>Vegetal</name><size>Mediana</size><extras/><price>6.5</price></pizza><pizza><code>14</code><name>Andaluza</name><size>Mediana</size><extras/><price>6.5</price></pizza></order>", ParserXML.WEBSERVICE);
			order.setOrderId("98754-158");
			InvoiceGenerator gen = new InvoiceGenerator(order);
			gen.generateInvoice("23/03/2021", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
