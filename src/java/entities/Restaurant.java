package java.entities;

public class Restaurant {
	private int restaurant_id;
	private String name;
	private String address;
	
	int getRestaurant_id() {
		return restaurant_id;
	}
	void setRestaurant_id(int restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
	String getName() {
		return name;
	}
	void setName(String name) {
		this.name = name;
	}
	String getAddress() {
		return address;
	}
	void setAddress(String address) {
		this.address = address;
	}
	
	
}
