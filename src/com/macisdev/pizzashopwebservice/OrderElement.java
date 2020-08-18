package com.macisdev.pizzashopwebservice;

import java.util.Objects;


public class OrderElement {
	private String code;
	private String name;
	private String size;
	private String extras;
	private String price;
	private int primaryKey;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}


	public String getExtras() {
		return extras;
	}

	public void setExtras(String extras) {
		this.extras = extras;
	}


	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderElement that = (OrderElement) o;
		return Objects.equals(code, that.code) &&
				Objects.equals(name, that.name) &&
				Objects.equals(size, that.size) &&
				Objects.equals(extras, that.extras) &&
				Objects.equals(price, that.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, name, size, extras, price);
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}
}
