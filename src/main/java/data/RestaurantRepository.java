package main.java.data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import main.java.entities.Restaurant;
import main.java.entities.Schedule;

public class RestaurantRepository {
	
	
	/* 
	 * ""
					+ "SELECT DISTINCT res.restaurant_id, res.name, res.address, res.image_url\r\n"
					+ "FROM restaurant res\r\n"
					+ "INNER JOIN schedule sch\r\n"
					+ "	ON sch.restaurant_id = res.restaurant_id\r\n"
					+ "WHERE sch.day_of_week = LOWER(DAYNAME(CURDATE()))\r\n"
					+ "	AND time(now()) BETWEEN sch.start_time AND sch.end_time;"
	 * 
	 * */

	public LinkedList<Restaurant> getAll() throws SQLException {
		LinkedList<Restaurant> restaurants = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(""
					+ "SELECT res.restaurant_id, res.name, res.address, res.image_url\r\n"
					+ "FROM restaurant res\r\n"
					);
			rs = stmt.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					Restaurant r = new Restaurant();
					r.setRestaurant_id(rs.getInt("res.restaurant_id"));
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
	
	public Restaurant getOne(Restaurant restaurantToFind) throws SQLException {
		PreparedStatement stmt = null;
		Restaurant res = null;
		ResultSet rs = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "SELECT restaurant_id, name, address, image_url "
					  + " FROM restaurant"
					  + " WHERE restaurant_id = ?"
					);

			
			if (stmt == null) {}
			stmt.setInt(1, restaurantToFind.getRestaurant_id());
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				res = new Restaurant();
				res.setRestaurant_id(rs.getInt("restaurant_id"));
				res.setName(rs.getString("name"));
				res.setAddress(rs.getString("address"));
				res.setImage_url(rs.getString("image_url"));
			
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
		return res;
		
	}
	
	public Boolean isAvailable(Restaurant restaurantToCheck) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;
		ResultSet rs = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "SELECT res.restaurant_id, res.name\r\n"
					  + "FROM restaurant res\r\n"
					  + "INNER JOIN schedule sch\r\n"
					  + "	ON sch.restaurant_id = res.restaurant_id\r\n"
					  + "WHERE sch.day_of_week = LOWER(DAYNAME(CURDATE()))\r\n"
					  + "	AND time(now()) BETWEEN sch.start_time AND sch.end_time\r\n"
					  + "    AND res.restaurant_id = ?;"
					);
			stmt.setInt(1, restaurantToCheck.getRestaurant_id());
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				result = true;
			} else {
				result = false;
			}
			
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
	
	public Boolean addRestaurant(Restaurant restaurant) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "INSERT INTO restaurant (name, address, image_url) "
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

	// trae los horarios cargados de un restaurante, ordenados para mostrarlos en el panel admin.
	public LinkedList<Schedule> getSchedules(int restaurantId) throws SQLException {
		LinkedList<Schedule> schedules = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "SELECT schedule_number, restaurant_id, day_of_week, start_time, end_time "
					+ "FROM schedule "
					+ "WHERE restaurant_id = ? "
					+ "ORDER BY schedule_number"
					);
			stmt.setInt(1, restaurantId);
			rs = stmt.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					Schedule s = new Schedule();
					s.setSchedule_number(rs.getInt("schedule_number"));
					s.setRestaurant_id(rs.getInt("restaurant_id"));
					s.setDay_of_week(rs.getString("day_of_week"));
					s.setStart_time(rs.getTime("start_time"));
					s.setEnd_time(rs.getTime("end_time"));
					schedules.add(s);
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

		return schedules;
	}

	public Boolean updateSchedule(Schedule schedule) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;

		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "UPDATE schedule "
					+ "SET day_of_week = ?, start_time = ?, end_time = ? "
					+ "WHERE restaurant_id = ? AND schedule_number = ?"
					);
			stmt.setString(1, schedule.getDay_of_week());
			stmt.setTime(2, schedule.getStart_time());
			stmt.setTime(3, schedule.getEnd_time());
			stmt.setInt(4, schedule.getRestaurant_id());
			stmt.setInt(5, schedule.getSchedule_number());
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

	public Boolean deleteSchedule(int restaurantId, int scheduleNumber) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;

		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"DELETE FROM schedule WHERE restaurant_id = ? AND schedule_number = ?"
					);
			stmt.setInt(1, restaurantId);
			stmt.setInt(2, scheduleNumber);
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
