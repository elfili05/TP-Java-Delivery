package java.entities;

public class Product {
	private int product_id;
	private String description;
	private double price;
	private ProductType product_type;
	
	int getProduct_id() {
		return product_id;
	}
	void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	String getDescription() {
		return description;
	}
	void setDescription(String description) {
		this.description = description;
	}
	double getPrice() {
		return price;
	}
	void setPrice(double price) {
		this.price = price;
	}
	ProductType getProduct_type() {
		return product_type;
	}
	void setProduct_type(ProductType product_type) {
		this.product_type = product_type;
	}
	
	
	
}
