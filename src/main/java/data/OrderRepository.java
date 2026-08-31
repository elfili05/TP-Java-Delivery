package main.java.data;
import java.sql.*;
import java.util.LinkedList;
import main.java.entities.OrderDetail;
import main.java.entities.Restaurant;
import main.java.entities.User;

public class OrderRepository {

	public void addOrder(LinkedList<OrderDetail> orderDetails, User user, Restaurant restaurant, double totalAmount) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DiscountRepository discountRepo = new DiscountRepository(); //cuando se llegue a los controladores, CAMBIAR esto
		int orderId = 0;
		
		try {
			// Insert the order
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "INSERT INTO `order` (user_id, restaurant_id, order_date, discount_id) "
					+ "VALUES (?, ?, CURDATE(),?)",
					Statement.RETURN_GENERATED_KEYS
					);
			stmt.setInt(1, user.getUser_id());
			stmt.setInt(2, restaurant.getRestaurant_id());
			stmt.setInt(3, discountRepo.getOne(totalAmount).getDiscount_id()); //cuando se llegue a los controladores, CAMBIAR esto
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			
			// Insert the order details
			for (OrderDetail orderDetail : orderDetails) {
				stmt = DbConnector.getInstance().getConn().prepareStatement(
						  "INSERT INTO order_detail (order_id, detail_number, product_id, quantity) "
						+ "VALUES (?,?,?,?)"
						);
				stmt.setInt(1, orderId);
				stmt.setInt(2, (orderDetails.indexOf(orderDetail) + 1) );
				stmt.setInt(3, orderDetail.getProduct().getProduct_id());
				stmt.setInt(4, orderDetail.getQuantity());
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
