package java.entities;

public class OrderDetail {
	
	private int order_id;
	private int detail_number;
	private Product product;
	private int quantity;
	
	int getOrder_id() {
		return order_id;
	}
	void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	int getDetail_number() {
		return detail_number;
	}
	void setDetail_number(int detail_number) {
		this.detail_number = detail_number;
	}

	int getQuantity() {
		return quantity;
	}
	void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	Product getProduct() {
		return product;
	}
	void setProduct(Product product) {
		this.product = product;
	}
	
	double getSubtotal() {
		return product.getPrice() * quantity;	
	}
	
	
}
