package main.java.data;
import java.sql.*;
import java.util.LinkedList;
import main.java.entities.Product;
import main.java.entities.Restaurant;
import main.java.entities.ProductType;
//import java.data.DbConnector;

public class ProductRepository {
	
	public LinkedList<Product> getAll(Restaurant res) throws SQLException{
		ProductTypeRepository ptRepo = new ProductTypeRepository();
		LinkedList<Product> products = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "SELECT product_id, description, price "
					+ "FROM product prod "
					+ "WHERE prod.restaurant_id = ?"
					);
			stmt.setInt(1, res.getRestaurant_id());
			rs = stmt.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					Product p = new Product();
					p.setProduct_id(rs.getInt("product_id"));
					p.setDescription(rs.getString("description"));
					p.setPrice(rs.getDouble("price"));
					ptRepo.setProductType(p);
					products.add(p);
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
		
		return products;
	}
	
	public LinkedList<Product> getByType(Restaurant res, String pt_name) throws SQLException{
		ProductTypeRepository ptRepo = new ProductTypeRepository();
		LinkedList<Product> products = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		if (pt_name == null || pt_name.isEmpty() || pt_name.equalsIgnoreCase("all")) {
			return getAll(res);
		}
		
		else {
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "SELECT prod.product_id, prod.description, prod.price, pt.name "
					+ "FROM product prod "
					+ "INNER JOIN product_type pt "
					+ "     ON prod.product_type_id = pt.product_type_id "
					+ "WHERE prod.restaurant_id = ? AND pt.name = ?"
					);
			stmt.setInt(1, res.getRestaurant_id());
			stmt.setString(2, pt_name);
			rs = stmt.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					Product p = new Product();
					p.setProduct_id(rs.getInt("prod.product_id"));
					p.setDescription(rs.getString("prod.description"));
					p.setPrice(rs.getDouble("prod.price"));
					ptRepo.setProductType(p);
					products.add(p);
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
		
		return products;
	}
		
	}
	
	public Product getOne(int productId) throws SQLException{
		ProductTypeRepository ptRepo = new ProductTypeRepository();
		Product p = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "SELECT product_id, description, price, pt.name"
					+ "FROM product prod"
					+ "INNER JOIN product_type pt "
					+ "     ON prod.product_type_id = pt.product_type_id"
					+ "WHERE product_id = ?"
					);
			stmt.setInt(1, productId);
			rs = stmt.executeQuery();
			
			if (rs != null && rs.next()) {
				p = new Product();
				p.setProduct_id(rs.getInt("product_id"));
				p.setDescription(rs.getString("description"));
				p.setPrice(rs.getDouble("price"));
				ptRepo.setProductType(p);
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
		
		return p;
	}

	public void deleteProduct(int productId) throws SQLException{
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "DELETE FROM product WHERE product_id = ?"
					);
			stmt.setInt(1, productId);
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
	
	public void addProduct(Product p, Restaurant res, ProductType pt) throws SQLException{
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "INSERT INTO product (description, price, restaurant_id, product_type_id) "
					+ "VALUES (?, ?, ?, ?)"
					);
			stmt.setString(1, p.getDescription());
			stmt.setDouble(2, p.getPrice());
			stmt.setInt(3, res.getRestaurant_id());
			stmt.setInt(4, pt.getProduct_type_id());
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
	
	public void updateProduct(Product p, ProductType pt) throws SQLException{
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					  "UPDATE product SET description = ?, price = ?, product_type_id = ? "
					+ "WHERE product_id = ?"
					);
			stmt.setString(1, p.getDescription());
			stmt.setDouble(2, p.getPrice());
			stmt.setInt(3, pt.getProduct_type_id());
			stmt.setInt(4, p.getProduct_id());
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
