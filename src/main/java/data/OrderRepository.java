package main.java.data;
import java.sql.*;
import main.java.entities.Discount;
import main.java.entities.Order;
import main.java.entities.OrderDetail;
import main.java.entities.Product;
import main.java.entities.Restaurant;
import main.java.entities.User;
import java.sql.Types;
import java.util.LinkedList;

public class OrderRepository {

	// guarda el pedido (cabecera + detalles) como una sola transacción: o se guardan todos los registros o ninguno.
	// devuelve true solo si todo se confirmó; ante cualquier error se hace rollback y devuelve false.
	public Boolean addOrder(Order orderToAdd) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement detailStmt = null;
		ResultSet rs = null;
		int orderId = 0;
		Boolean result = false;
		
		try {
			conn = DbConnector.getInstance().getConn();
			// la conexión es propia del hilo del request (ver DbConnector), así que la transacción no se filtra a otros requests.
			conn.setAutoCommit(false);
			
			// Insert the order
			stmt = conn.prepareStatement(
					  "INSERT INTO user_order (user_id, restaurant_id, date, discount_id, total_amount) "
					+ "VALUES (?, ?, CURDATE(),?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			stmt.setInt(1, orderToAdd.getUser().getUser_id());
			stmt.setInt(2, orderToAdd.getRestaurant().getRestaurant_id());
			if (orderToAdd.getDiscount() != null) {
				stmt.setInt(3, orderToAdd.getDiscount().getDiscount_id());
			}
			else { stmt.setNull(3, Types.INTEGER); }
			stmt.setDouble(4, orderToAdd.getTotal());
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			if (orderId == 0) {
				throw new SQLException("No se obtuvo el id del pedido recién insertado.");
			}
			
			// Insert the order details
			detailStmt = conn.prepareStatement(
					  "INSERT INTO order_detail (order_id, detail_number, product_id, quantity, subtotal) "
					+ "VALUES (?,?,?,?,?)"
					);
			for (OrderDetail orderDetail : orderToAdd.getOrder_details()) {
				detailStmt.setInt(1, orderId);
				detailStmt.setInt(2, orderDetail.getDetail_number());
				detailStmt.setInt(3, orderDetail.getProduct().getProduct_id());
				detailStmt.setInt(4, orderDetail.getQuantity());
				detailStmt.setDouble(5, orderDetail.getSubtotal());
				detailStmt.executeUpdate();
			}
			
			conn.commit();
			result = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
			try {
				if (conn != null) { conn.rollback(); }
			} catch (SQLException rollbackError) {
				rollbackError.printStackTrace();
			}
			
		} finally {
			try {
				if (rs != null) { rs.close(); }
				if (stmt != null) { stmt.close(); }
				if (detailStmt != null) { detailStmt.close(); }
				if (conn != null) { conn.setAutoCommit(true); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// marca un pedido pendiente como entregado; devuelve true solo si se actualizó una fila
	// (0 filas = el pedido no existe o ya estaba entregado, así que es seguro ante un doble clic).
	public Boolean deliverOrder(int orderId) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;

		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "UPDATE user_order "
					  + "SET status = 'delivered' "
					  + "WHERE order_id = ? AND status = 'pending'"
					);
			stmt.setInt(1, orderId);
			result = stmt.executeUpdate() == 1;

		} catch (SQLException e) {
			// un fallo de BD se propaga: no es lo mismo que "ya estaba entregado", y el servlet muestra cada caso por separado.
			e.printStackTrace();
			throw e;

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

	// trae los pedidos según su estado ('pending' o 'delivered') con cliente, restaurante y descuento;
	// los ítems se cargan después en un segundo paso con getDetails.
	public LinkedList<Order> getByStatus(String status) throws SQLException {
		LinkedList<Order> orders = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// pendientes del más viejo al más nuevo; entregados del más nuevo al más viejo.
		String direction = "pending".equals(status) ? "ASC" : "DESC";

		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "SELECT o.order_id, o.date, o.status, "
					+ "u.user_id, u.name AS user_name, u.surname AS user_surname, u.email, "
					+ "r.restaurant_id, r.name AS restaurant_name, "
					+ "d.discount_id, d.discount_percentage "
					+ "FROM user_order o "
					+ "INNER JOIN user u ON u.user_id = o.user_id "
					+ "INNER JOIN restaurant r ON r.restaurant_id = o.restaurant_id "
					+ "LEFT JOIN discount d ON d.discount_id = o.discount_id "
					+ "WHERE o.status = ? "
					+ "ORDER BY o.order_id " + direction
					);
			stmt.setString(1, status);
			rs = stmt.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					User user = new User();
					user.setUser_id(rs.getInt("user_id"));
					user.setName(rs.getString("user_name"));
					user.setSurname(rs.getString("user_surname"));
					user.setEmail(rs.getString("email"));

					Restaurant restaurant = new Restaurant();
					restaurant.setRestaurant_id(rs.getInt("restaurant_id"));
					restaurant.setName(rs.getString("restaurant_name"));

					Order order = new Order(user, restaurant);
					order.setOrder_id(rs.getInt("order_id"));
					order.setStatus(rs.getString("status"));
					if (rs.getDate("date") != null) {
						order.setOrder_date(rs.getDate("date").toLocalDate());
					}

					int discountId = rs.getInt("discount_id");
					if (!rs.wasNull()) {
						Discount discount = new Discount();
						discount.setDiscount_id(discountId);
						discount.setDiscount_percentage(rs.getDouble("discount_percentage"));
						order.setDiscount(discount);
					}

					orders.add(order);
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

		for (Order order : orders) {
			order.setOrder_details(getDetails(order.getOrder_id()));
		}

		return orders;
	}

	// trae los ítems (producto x cantidad) de un pedido, ordenados por número de detalle.
	private LinkedList<OrderDetail> getDetails(int orderId) throws SQLException {
		LinkedList<OrderDetail> details = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "SELECT d.detail_number, d.quantity, d.subtotal, p.product_id, p.description "
					+ "FROM order_detail d "
					+ "INNER JOIN product p ON p.product_id = d.product_id "
					+ "WHERE d.order_id = ? "
					+ "ORDER BY d.detail_number"
					);
			stmt.setInt(1, orderId);
			rs = stmt.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					Product product = new Product();
					product.setProduct_id(rs.getInt("product_id"));
					product.setDescription(rs.getString("description"));
					// el precio unitario se deduce del subtotal guardado en el pedido: así un cambio de precio posterior no altera el historial.
					int quantity = rs.getInt("quantity");
					product.setPrice(quantity > 0 ? rs.getDouble("subtotal") / quantity : 0);

					OrderDetail detail = new OrderDetail(product, quantity, rs.getInt("detail_number"));
					detail.setOrder_id(orderId);
					details.add(detail);
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

		return details;
	}

}
