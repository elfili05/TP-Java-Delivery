package main.java.data;
import java.util.LinkedList;
import java.sql.*;
import main.java.entities.ProductType;

public class ProductTypeRepository {
	
	public LinkedList<ProductType> getAll() throws SQLException {
		LinkedList<ProductType> productTypes = new LinkedList<>();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().createStatement();
			rs = stmt.executeQuery("SELECT product_type_id, name FROM product_type");
			
			if (rs != null) {
				while (rs.next()) {
					ProductType pt = new ProductType();
					pt.setProduct_type_id(rs.getInt("product_type_id"));
					pt.setName(rs.getString("name"));
					productTypes.add(pt);
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
		
		return productTypes;
	}
	
	public void addProductType(ProductType productType) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"INSERT INTO product_type (name) VALUES (?)"
					);
			stmt.setString(1, productType.getName());
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
	
	public ProductType getOne(int productTypeId) throws SQLException{
		ProductType productType = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"SELECT product_type_id, name FROM product_type WHERE product_type_id = ?"
					);
			stmt.setInt(1, productTypeId);
			rs = stmt.executeQuery();
			
			if (rs != null && rs.next()) {
				productType = new ProductType();
				productType.setProduct_type_id(rs.getInt("product_type_id"));
				productType.setName(rs.getString("name"));
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
		
		return productType;
	}
	
	public void deleteProductType(int productTypeId) throws SQLException{
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"DELETE FROM product_type WHERE product_type_id = ?"
					);
			stmt.setInt(1, productTypeId);
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
	
	public void updateProductType(ProductType productType) throws SQLException{
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"UPDATE product_type SET name = ? WHERE product_type_id = ?"
					);
			stmt.setString(1, productType.getName());
			stmt.setInt(2, productType.getProduct_type_id());
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
