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
	
	public void addDiscount(Discount discount) throws SQLException{
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"insert into discount (minimum_amount, discount_percentage) values (?, ?)"
					);
			stmt.setDouble(1, discount.getMinimum_amount());
			stmt.setDouble(2, discount.getDiscount_percentage());
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
	
	public LinkedList<Discount> getAll() throws SQLException {
		LinkedList<Discount> discounts = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"select minimum_amount, discount_percentage from discount"
					);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Discount d = new Discount();
					d.setMinimum_amount(rs.getDouble("minimum_amount"));
					d.setDiscount_percentage(rs.getDouble("discount_percentage"));
					discounts.add(d);
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
		
		return discounts;
	}
	
	public void deleteDiscount(double minimumAmount) throws SQLException{
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"delete from discount where minimum_amount = ?"
					);
			stmt.setDouble(1, minimumAmount);
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
	
	public void updateDiscount(Discount discount) throws SQLException{
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"update discount set discount_percentage = ? where minimum_amount = ?"
					);
			stmt.setDouble(1, discount.getDiscount_percentage());
			stmt.setDouble(2, discount.getMinimum_amount());
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
