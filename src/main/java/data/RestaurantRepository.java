package main.java.data;
import java.sql.*;
import java.util.LinkedList;
import main.java.entities.Restaurant;
import main.java.entities.Schedule;

public class RestaurantRepository {

	public LinkedList<Restaurant> getAll() throws SQLException {
		LinkedList<Restaurant> restaurants = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement("SELECT DISTINCT res.name, res.address, res.image_url\r\n"
					+ "FROM restaurant res\r\n;"
					);
			rs = stmt.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					Restaurant r = new Restaurant();
					r.setName(rs.getString("res.name"));
					r.setAddress(rs.getString("res.address"));
					r.setImage_url(rs.getString("res.image_url"));
					restaurants.add(r);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (rs != null) { rs.close(); }
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return restaurants;
	}
	
	public Boolean addRestaurant(Restaurant restaurant) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "INSERT INTO restaurant (name, address) "
					+ "VALUES (?, ?, ?)"
					);
			stmt.setString(1, restaurant.getName());
			stmt.setString(2, restaurant.getAddress());
			stmt.setString(3, restaurant.getImage_url());
			stmt.executeUpdate();
			
			result = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
			
		} finally {
			try {
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}
	
	public Boolean addSchedule(Restaurant restaurant, Schedule schedule) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;
		
		try {
			
				stmt = DbConnector.getInstance().getConn().prepareStatement(
						  "INSERT INTO schedule (schedule_number, restaurant_id, day_of_week, start_time, end_time) "
						+ "VALUES (?, ?, ?, ?, ?)"
						);
				stmt.setInt(1, (schedule.getSchedule_number()) );
				stmt.setInt(2, restaurant.getRestaurant_id());
				stmt.setString(3, schedule.getDay_of_week());
				stmt.setTime(4, schedule.getStart_time());
				stmt.setTime(5, schedule.getEnd_time());
				stmt.executeUpdate();
				
				result = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
			
		} finally {
			try {
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}
	
	public Boolean deleteRestaurant(int restaurantId) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "DELETE FROM restaurant WHERE restaurant_id = ?"
					);
			stmt.setInt(1, restaurantId);
			stmt.executeUpdate();
			result = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
			
		} finally {
			try {
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}
	
	public Boolean updateRestaurant(Restaurant restaurant) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "UPDATE restaurant "
					+ "SET name = ?, address = ?, image_url = ? "
					+ "WHERE restaurant_id = ?"
					);
			stmt.setString(1, restaurant.getName());
			stmt.setString(2, restaurant.getAddress());
			stmt.setString(3, restaurant.getImage_url());
			stmt.setInt(4, restaurant.getRestaurant_id());
			stmt.executeUpdate();
			result = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
			
		} finally {
			try {
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}
}
