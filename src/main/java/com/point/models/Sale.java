package com.point.models;

import java.util.ArrayList;
import java.util.Date;

public class Sale {

	Long id;
	Date date;
	int total;
	ArrayList<?> productList;

	public Sale() {
	}

	/**
	 * @param id
	 * @param date
	 * @param total
	 * @param productList
	 */
	public Sale(Long id, Date date, int total, ArrayList<?> productList) {
		this.id = id;
		this.date = date;
		this.total = total;
		this.productList = productList;
	}

	@Override
	public String toString() {
		return "Sale [id=" + id + ", date=" + date + ", total=" + total + ", productList=" + productList + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ArrayList<?> getProductList() {
		return productList;
	}

	public void setProductList(ArrayList<?> productList) {
		this.productList = productList;
	}

	
}
