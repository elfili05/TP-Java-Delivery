package main.java.data;
import java.sql.*;
import java.sql.SQLException;
import main.java.entities.Discount;
import java.util.LinkedList;

public class DiscountRepository {
	
	public Discount getOne(double amount) throws SQLException {
		Discount d = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "select discount_id, MAX(minimum_amount) as min_amount, discount_percentage\r\n"
					  + "from discount \r\n"
					  + "group by discount_percentage, discount_id\r\n"
					  + "having min_amount <= ?\r\n"
					  + "order by min_amount desc\r\n"
					  + "LIMIT 1;"
					);
			stmt.setDouble(1, amount);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				d = new Discount();
				d.setDiscount_id(rs.getInt("discount_id"));
				d.setMinimum_amount(rs.getDouble("min_amount"));
				d.setDiscount_percentage(rs.getDouble("discount_percentage"));
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
		
		return d;
	}
	
	public Boolean addDiscount(Discount discount) throws SQLException{
		PreparedStatement stmt = null;
		Boolean result = false;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"insert into discount (minimum_amount, discount_percentage) values (?, ?)"
					);
			stmt.setDouble(1, discount.getMinimum_amount());
			stmt.setDouble(2, discount.getDiscount_percentage());
			stmt.executeUpdate();
			result = true;
		} catch (SQLIntegrityConstraintViolationException e) {
			// minimum_amount es UNIQUE: se propaga para que logic lo traduzca a un mensaje controlado.
			throw e;
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
	
	public LinkedList<Discount> getAll() throws SQLException {
		LinkedList<Discount> discounts = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"select discount_id, minimum_amount, discount_percentage from discount order by minimum_amount"
					);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Discount d = new Discount();
					d.setDiscount_id(rs.getInt("discount_id"));
					d.setMinimum_amount(rs.getDouble("minimum_amount"));
					d.setDiscount_percentage(rs.getDouble("discount_percentage"));
					discounts.add(d);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e; // un fallo de BD no se muestra como "lista vacía": la pantalla avisa.
		} finally {
			try {
				if (rs != null) { rs.close(); }
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return discounts;
	}
	
	// trae un descuento por su id; se usa para precargar la pantalla de edición.
	public Discount getById(int discountId) throws SQLException {
		Discount d = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"select discount_id, minimum_amount, discount_percentage from discount where discount_id = ?"
					);
			stmt.setInt(1, discountId);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				d = new Discount();
				d.setDiscount_id(rs.getInt("discount_id"));
				d.setMinimum_amount(rs.getDouble("minimum_amount"));
				d.setDiscount_percentage(rs.getDouble("discount_percentage"));
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

		return d;
	}

	// cantidad de pedidos que usaron este descuento; un fallo de BD se propaga (no se puede asumir que "no hay pedidos").
	public int countOrdersUsing(int discountId) throws SQLException {
		int count = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"select count(*) from user_order where discount_id = ?"
					);
			stmt.setInt(1, discountId);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			try {
				if (rs != null) { rs.close(); }
				if (stmt != null) { stmt.close(); }
			} finally {
				DbConnector.getInstance().releaseConn();
			}
		}
		return count;
	}

	public Boolean deleteDiscount(int discountId) throws SQLException{
		PreparedStatement stmt = null;
		Boolean result = false;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"delete from discount where discount_id = ?"
					);
			stmt.setInt(1, discountId);
			result = stmt.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			// la FK de user_order impide borrar un descuento ya aplicado a pedidos: se propaga para que logic lo traduzca.
			throw e;
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

	public Boolean updateDiscount(Discount discount) throws SQLException{
		PreparedStatement stmt = null;
		Boolean result = false;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"update discount set minimum_amount = ?, discount_percentage = ? where discount_id = ?"
					);
			stmt.setDouble(1, discount.getMinimum_amount());
			stmt.setDouble(2, discount.getDiscount_percentage());
			stmt.setInt(3, discount.getDiscount_id());
			result = stmt.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			// minimum_amount es UNIQUE: cambiarlo a uno ya usado por otro descuento llega hasta acá; se propaga a logic.
			throw e;
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
