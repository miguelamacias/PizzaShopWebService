
package com.macisdev.pizzashopwebservice.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class Order {
	private SimpleStringProperty orderId;
	private SimpleStringProperty customerName;
	private SimpleStringProperty customerPhone;
	private SimpleStringProperty customerAddress;
	private SimpleStringProperty orderDateTime;
	private SimpleStringProperty deliveryMethod;
	private SimpleStringProperty paymentMethod;
	private SimpleDoubleProperty totalPrice;
	
	private List<OrderElement> orderElements = new ArrayList<>();

	public Order(String orderId, String customerName, String customerPhone, String customerAddress,
				 String deliveryMethod, String paymentMethod, double totalPrice, String orderDateTime) {
		this.orderId = new SimpleStringProperty(orderId);
		this.customerName = new SimpleStringProperty(customerName);
		this.customerPhone = new SimpleStringProperty(customerPhone);
		this.customerAddress = new SimpleStringProperty(customerAddress);
		this.deliveryMethod = new SimpleStringProperty(deliveryMethod);
		this.totalPrice = new SimpleDoubleProperty(totalPrice);
		this.paymentMethod = new SimpleStringProperty(paymentMethod);
		this.orderDateTime = new SimpleStringProperty(orderDateTime);
	}
	
	public Order() {
		
	}

	public String getOrderId() {
		return orderId.get();
	}

	public void setOrderId(String orderId) {
		this.orderId = new SimpleStringProperty(orderId);
	}

	public String getOrderDateTime() {
		return orderDateTime.get();
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = new SimpleStringProperty(orderDateTime);
	}

	public String getCustomerName() {
		return customerName.get();
	}

	public void setCustomerName(String customerName) {
		this.customerName = new SimpleStringProperty(customerName);
	}

	public String getCustomerPhone() {
		return customerPhone.get();
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = new SimpleStringProperty(customerPhone);
	}

	public String getCustomerAddress() {
		return customerAddress.get();
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = new SimpleStringProperty(customerAddress);
	}

	public String getDeliveryMethod() {
		return deliveryMethod.get();
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = new SimpleStringProperty(deliveryMethod);
	}

	public String getPaymentMethod() {
		return paymentMethod.get();
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = new SimpleStringProperty(paymentMethod);
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = new SimpleDoubleProperty(totalPrice);
	}

	public double getTotalPrice() {
		return totalPrice.get();
	}

	public List<OrderElement> getOrderElements() {
		return orderElements;
	}

	public void setOrderElements(List<OrderElement> orderElements) {
		this.orderElements = orderElements;
	}
	
	
	public void addElementToOrder(OrderElement element) {
		orderElements.add(element);
	}

	@Override
	public String toString() {
		return String.format(" ID: %s%n Name: %s%n Phone: %s%n Delivery: %s%n Address: %s%n Payment: %s%n Price: %f%n",
				getOrderId(), getCustomerName(), getCustomerPhone(), getDeliveryMethod(), getCustomerAddress(), getPaymentMethod(), getTotalPrice());
	}	
}
