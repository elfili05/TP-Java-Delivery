package main.java.data;
import java.sql.*;
import java.util.LinkedList;

import main.java.entities.Discount;
import main.java.entities.OrderDetail;
import main.java.entities.Restaurant;
import main.java.entities.User;
import java.sql.Types;

public class OrderRepository {

	public void addOrder(LinkedList<OrderDetail> orderDetails, User user, Restaurant restaurant, double totalAmount) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DiscountRepository discountRepo = new DiscountRepository(); //cuando se llegue a los controladores, CAMBIAR esto
		int orderId = 0;
		
		Discount discount = discountRepo.getOne(totalAmount);
		
		try {
			// Insert the order
			System.out.println(restaurant.getRestaurant_id());
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "INSERT INTO user_order (user_id, restaurant_id, date, discount_id, total_amount) "
					+ "VALUES (?, ?, CURDATE(),?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			System.out.println("user id: " + user.getUser_id());
			stmt.setInt(1, user.getUser_id());
			stmt.setInt(2, restaurant.getRestaurant_id());
			if (discount != null) {
			stmt.setInt(3, discount.getDiscount_id()); //cuando se llegue a los controladores, CAMBIAR esto
				}
			else { stmt.setNull(3, Types.INTEGER); }
			stmt.setDouble(4, totalAmount);
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			
			// Insert the order details
			for (OrderDetail orderDetail : orderDetails) {
				stmt = DbConnector.getInstance().getConn().prepareStatement(
						  "INSERT INTO order_detail (order_id, detail_number, product_id, quantity, subtotal) "
						+ "VALUES (?,?,?,?,?)"
						);
				stmt.setInt(1, orderId);
				stmt.setInt(2, orderDetail.getDetail_number());
				stmt.setInt(3, orderDetail.getProduct().getProduct_id());
				stmt.setInt(4, orderDetail.getQuantity());
				stmt.setDouble(5, orderDetail.getSubtotal());
				stmt.executeUpdate();
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
		
	}
	
}
