package main.java.data;
import java.util.LinkedList;

import java.sql.*;

import main.java.entities.Product;
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
		
		return productTypes;
	}
	
	public Boolean addProductType(ProductType productType) throws SQLException {
		PreparedStatement stmt = null;
		Boolean result = false;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"INSERT INTO product_type (name) VALUES (?)"
					);
			stmt.setString(1, productType.getName());
			stmt.executeUpdate();
			result = true;
		} catch (SQLIntegrityConstraintViolationException e) {
			// product_type.name es UNIQUE: se propaga para mostrar "ya existe" (cubre dos altas simultáneas con el mismo nombre).
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
	
	public Boolean deleteProductType(int productTypeId) throws SQLException{
		PreparedStatement stmt = null;
		Boolean result = false;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"DELETE FROM product_type WHERE product_type_id = ?"
					);
			stmt.setInt(1, productTypeId);
			result = stmt.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			// la FK de product impide borrar un tipo en uso: se propaga para que logic lo traduzca a un mensaje controlado.
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
	
	public Boolean updateProductType(ProductType productType) throws SQLException{
		PreparedStatement stmt = null;
		Boolean result = false;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"UPDATE product_type SET name = ? WHERE product_type_id = ?"
					);
			stmt.setString(1, productType.getName());
			stmt.setInt(2, productType.getProduct_type_id());
			result = stmt.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			// product_type.name es UNIQUE: se propaga para mostrar "ya existe" (cubre dos altas simultáneas con el mismo nombre).
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
	
	public void setProductType(Product productToSearch) {
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			stmt=DbConnector.getInstance().getConn().prepareStatement(
					  "select pt.product_type_id, pt.name\r\n"
					  + "from product_type pt \r\n"
					  + "inner join product prod \r\n"
					  + "	on prod.product_type_id = pt.product_type_id\r\n"
					  + "where prod.product_id = ?;"
					);
			stmt.setInt(1, productToSearch.getProduct_id());
			rs= stmt.executeQuery();
			if(rs!=null) {
					rs.next();
					ProductType pt = new ProductType();
					pt.setProduct_type_id(rs.getInt("pt.product_type_id"));
					pt.setName(rs.getString("pt.name"));
					productToSearch.setProduct_type(pt);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(stmt!=null) {stmt.close();}
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
