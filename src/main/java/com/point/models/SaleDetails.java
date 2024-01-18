package com.point.models;

public class SaleDetails {

	Long id;
	Long id_sale;
	Long id_product;
	int quantity;
	double unit_price;
	double subtotal;
	
	
	/**
	 * @param id
	 * @param id_sale
	 * @param id_product
	 * @param quantity
	 * @param unit_price
	 * @param subtotal
	 */
	public SaleDetails(Long id, Long id_sale, Long id_product, int quantity, double unit_price, double subtotal) {
		this.id = id;
		this.id_sale = id_sale;
		this.id_product = id_product;
		this.quantity = quantity;
		this.unit_price = unit_price;
		this.subtotal = subtotal;
	}
	
	
	@Override
	public String toString() {
		return "SaleDetails [id=" + id + ", id_sale=" + id_sale + ", id_product=" + id_product + ", quantity="
				+ quantity + ", unit_price=" + unit_price + ", subtotal=" + subtotal + "]";
	}
	
	
	Long getId() {
		return id;
	}
	void setId(Long id) {
		this.id = id;
	}
	Long getId_sale() {
		return id_sale;
	}
	void setId_sale(Long id_sale) {
		this.id_sale = id_sale;
	}
	Long getId_product() {
		return id_product;
	}
	void setId_product(Long id_product) {
		this.id_product = id_product;
	}
	int getQuantity() {
		return quantity;
	}
	void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	double getUnit_price() {
		return unit_price;
	}
	void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}
	double getSubtotal() {
		return subtotal;
	}
	void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	
	
}
