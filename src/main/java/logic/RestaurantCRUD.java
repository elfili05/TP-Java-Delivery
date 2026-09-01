package main.java.logic;

import java.sql.SQLException;
import java.util.LinkedList;

import main.java.data.RestaurantRepository;
import main.java.entities.Restaurant;

public class RestaurantCRUD {
	
	private RestaurantRepository rr;
	
	public RestaurantCRUD() {
		rr = new RestaurantRepository();
	}
	
	public LinkedList<Restaurant> getAvailable( ) throws SQLException {
		return rr.getAll();
	}
	
	public Boolean addRestaurant(Restaurant restaurantToAdd) throws SQLException {
		return rr.addRestaurant(restaurantToAdd);
	}
}
