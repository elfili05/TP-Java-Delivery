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
	
	public LinkedList<Restaurant> getAvailable() throws SQLException {
		return rr.getAll();
	}
	
	
	public Restaurant getRestaurant(Restaurant restaurantToFind) throws SQLException {
		return rr.getOne(restaurantToFind);
	}
	
	
	public Boolean addRestaurant(Restaurant restaurantToAdd) throws SQLException {
		return rr.addRestaurant(restaurantToAdd);
	}

	// usados por el panel admin para editar/eliminar restaurantes.
	public Boolean updateRestaurant(Restaurant restaurantToUpdate) throws SQLException {
		return rr.updateRestaurant(restaurantToUpdate);
	}

	public Boolean deleteRestaurant(int restaurantId) throws SQLException {
		return rr.deleteRestaurant(restaurantId);
	}
}
