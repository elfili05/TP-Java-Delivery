package java.data;
import java.sql.*;
import java.util.LinkedList;
import java.entities.Restaurant;
import java.entities.Schedule;

public class RestaurantRepository {

	public LinkedList<Restaurant> getAll() {
		LinkedList<Restaurant> restaurants = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(""
					+ "SELECT DISTINCT res.name, res.address"
					+ "FROM restaurant res"
					+ "INNER JOIN schedule sch"
					+ "		ON sch.restaurant_id = res.restaurant_id"
					+ "WHERE sch.day_of_week = LOWER(DAYNAME(CURDATE()))"
					+ "		AND time(now()) BETWEEN sch.start_time AND sch.end_time;"
					);
			rs = stmt.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					Restaurant r = new Restaurant();
					r.setName(rs.getString("res.name"));
					r.setAddress(rs.getString("res.address"));
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
	
	public void addRestaurant(Restaurant restaurant) throws SQLException {
		PreparedStatement stmt = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "INSERT INTO restaurant (name, address) "
					+ "VALUES (?, ?)"
					);
			stmt.setString(1, restaurant.getName());
			stmt.setString(2, restaurant.getAddress());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addSchedule(Restaurant restaurant, LinkedList<Schedule> schedules) throws SQLException {
		PreparedStatement stmt = null;
		
		try {
			for (Schedule schedule : schedules) {
				stmt = DbConnector.getInstance().getConn().prepareStatement(
						  "INSERT INTO schedule (schedule_number, restaurant_id, day_of_week, start_time, end_time) "
						+ "VALUES (?,?, ?, ?, ?)"
						);
				stmt.setInt(1, (schedules.indexOf(schedule) + 1 ) );
				stmt.setInt(2, restaurant.getRestaurant_id());
				stmt.setString(3, schedule.getDay_of_week());
				stmt.setTime(4, schedule.getStart_time());
				stmt.setTime(5, schedule.getEnd_time());
				stmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteRestaurant(int restaurantId) throws SQLException {
		PreparedStatement stmt = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "DELETE FROM restaurant WHERE restaurant_id = ?"
					);
			stmt.setInt(1, restaurantId);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateRestaurant(Restaurant restaurant) throws SQLException {
		PreparedStatement stmt = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "UPDATE restaurant "
					+ "SET name = ?, address = ? "
					+ "WHERE restaurant_id = ?"
					);
			stmt.setString(1, restaurant.getName());
			stmt.setString(2, restaurant.getAddress());
			stmt.setInt(3, restaurant.getRestaurant_id());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
