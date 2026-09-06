package main.java.entities;

import java.util.Objects;

public class Product {
	private int product_id;
	private String description;
	private double price;
	private ProductType product_type;
	
	
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public ProductType getProduct_type() {
		return product_type;
	}
	public void setProduct_type(ProductType product_type) {
		this.product_type = product_type;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(product_id));
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return product_id == other.product_id;
	}
	
	
	
}
