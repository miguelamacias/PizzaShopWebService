package com.macisdev.pizzashopwebservice;

import com.macisdev.orders.Order;

public class mainTest {
	public static void main(String[] args) {
		PizzaShopService pizzaShopService = new PizzaShopService();
		//boolean order1 = pizzaShopService.storeOrder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><order><order_info><order_id>1597541859378</order_id><customer_name>Tgggt</customer_name><customer_phone>666666666</customer_phone><delivery_method>Recoger en local</delivery_method><customer_address>Recoger en local</customer_address><payment_method>Efectivo</payment_method><total_price>8.00</total_price></order_info><pizza><code>1</code><name>Monster</name><size>Mediana</size><extras/><price>8.0</price></pizza></order>");
		//boolean order2 = pizzaShopService.storeOrder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><order><order_info><order_id>1597541777351</order_id><customer_name>Gggg</customer_name><customer_phone>663665662</customer_phone><delivery_method>Recoger en local</delivery_method><customer_address>Recoger en local</customer_address><payment_method>Efectivo</payment_method><total_price>8.00</total_price></order_info><pizza><code>1</code><name>Monster</name><size>Mediana</size><extras/><price>8.0</price></pizza></order>");
		//System.out.println(order1 + " " + order2);
		Order order = pizzaShopService.getStoredOrder("1597541777351");
		System.out.println(order);
	}
}
