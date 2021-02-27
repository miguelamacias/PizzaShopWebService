package com.macisdev.pizzashopwebservice.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderElement {
	private int primaryKey;
	private SimpleStringProperty code;
    private SimpleStringProperty name;
    private SimpleStringProperty size;
    private SimpleStringProperty extras;
    private SimpleDoubleProperty price;

	public OrderElement(String code, String name, String size, String extras, double price) {
		this.code = new SimpleStringProperty(code);
		this.name = new SimpleStringProperty(name);
		this.size = new SimpleStringProperty(size);
		this.extras = new SimpleStringProperty(extras);
		this.price = new SimpleDoubleProperty(price);
	}
	
	public OrderElement() {
		
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setCode(String code) {
		this.code = new SimpleStringProperty(code);
	}

	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}

	public void setSize(String size) {
		this.size = new SimpleStringProperty(size);
	}

	public void setExtras(String extras) {
		this.extras = new SimpleStringProperty(extras);
	}

	public void setPrice(double price) {
		this.price = new SimpleDoubleProperty(price);
	}
	
	

	public String getCode() {
		return code.get();
	}

	public String getName() {
		return name.get();
	}

	public String getSize() {
		return size.get();
	}

	public String getExtras() {
		return extras.get();
	}

	public double getPrice() {
		return price.get();
	}
	
    @Override
    public String toString() {
        return String.format("%s  -  %s  -  %s€\n%s",getName(), getSize(), getPrice(), getExtras());
    }

}
