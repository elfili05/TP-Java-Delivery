package main.java.data;
import java.sql.*;
import main.java.entities.Order;
import main.java.entities.OrderDetail;
import java.sql.Types;

public class OrderRepository {

	public void addOrder(Order orderToAdd) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int orderId = 0;
		
		//Discount discount = discountRepo.getOne(totalAmount);
		
		try {
			// Insert the order
			//System.out.println(restaurant.getRestaurant_id());
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "INSERT INTO user_order (user_id, restaurant_id, date, discount_id, total_amount) "
					+ "VALUES (?, ?, CURDATE(),?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			System.out.println("user id: " + orderToAdd.getUser().getUser_id());
			stmt.setInt(1, orderToAdd.getUser().getUser_id());
			stmt.setInt(2, orderToAdd.getRestaurant().getRestaurant_id());
			if (orderToAdd.getDiscount() != null) {
			stmt.setInt(3, orderToAdd.getDiscount().getDiscount_id()); //cuando se llegue a los controladores, CAMBIAR esto
				}
			else { stmt.setNull(3, Types.INTEGER); }
			stmt.setDouble(4, orderToAdd.getTotal());
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			
			// Insert the order details
			for (OrderDetail orderDetail : orderToAdd.getOrder_details()) {
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
