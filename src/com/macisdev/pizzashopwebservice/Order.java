package com.macisdev.pizzashopwebservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
	private String orderId;
	private String customerName;
	private String customerPhone;
	private String customerAddress;
	private String orderDatetime;
	private String deliveryMethod;
	private String paymentMethod;
	private String totalPrice;

	private List<OrderElement> elements = new ArrayList<>();

	public List<OrderElement> getElements() {
		return elements;
	}

	public void setElements(List<OrderElement> elements) {
		this.elements = elements;
	}

	public void addOrderElement(OrderElement element) {
		elements.add(element);
	}


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String cutomerPhone) {
		this.customerPhone = cutomerPhone;
	}


	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}


	public String getOrderDatetime() {
		return orderDatetime;
	}

	public void setOrderDatetime(String orderDatetime) {
		this.orderDatetime = orderDatetime;
	}


	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}


	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return Objects.equals(orderId, order.orderId) &&
				Objects.equals(customerName, order.customerName) &&
				Objects.equals(customerPhone, order.customerPhone) &&
				Objects.equals(customerAddress, order.customerAddress) &&
				Objects.equals(orderDatetime, order.orderDatetime) &&
				Objects.equals(deliveryMethod, order.deliveryMethod) &&
				Objects.equals(paymentMethod, order.paymentMethod) &&
				Objects.equals(totalPrice, order.totalPrice);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, customerName, customerPhone, customerAddress, orderDatetime, deliveryMethod, paymentMethod, totalPrice);
	}
}
