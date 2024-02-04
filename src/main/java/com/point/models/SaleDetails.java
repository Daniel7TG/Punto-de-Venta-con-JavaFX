package com.point.models;

public class SaleDetails {

	Long id;
	Long id_sale;
	Long id_product;
	int quantity;
	String product_name;
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
	public SaleDetails(Long id, Long id_sale, Long id_product, int quantity, String product_name, double unit_price, double subtotal) {
		this.id = id;
		this.id_sale = id_sale;
		this.id_product = id_product;
		this.quantity = quantity;
		this.product_name = product_name;
		this.unit_price = unit_price;
		this.subtotal = subtotal;
	}
	
	
	@Override
	public String toString() {
		return "SaleDetails [id=" + id + ", id_sale=" + id_sale + ", id_product=" + id_product + ", quantity="
				+ quantity + ", unit_price=" + unit_price + ", subtotal=" + subtotal + "]";
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId_sale() {
		return id_sale;
	}
	public void setId_sale(Long id_sale) {
		this.id_sale = id_sale;
	}
	public Long getId_product() {
		return id_product;
	}
	public void setId_product(Long id_product) {
		this.id_product = id_product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	
}
