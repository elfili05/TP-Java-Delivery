package main.java.entities;

public class OrderDetail {
	
	private int order_id;
	private int detail_number;
	private Product product;
	private int quantity;
	
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public int getDetail_number() {
		return detail_number;
	}
	public void setDetail_number(int detail_number) {
		this.detail_number = detail_number;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public double getSubtotal() {
		return product.getPrice() * quantity;	
	}
	
	
}
