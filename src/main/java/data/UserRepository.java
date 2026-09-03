package main.java.data;
import java.sql.*;
import java.sql.SQLException;
import main.java.entities.User;
import java.util.LinkedList;

//FORMATO DE UNA CONSULTA A LA BASE DE DATOS
//	try {
//		stmt= DbConnector.getInstance().getConn().createStatement();
//		rs= stmt.executeQuery();
//		if(rs!=null) {
//			while(rs.next()) {
//			}
//		}
//		
//	} catch (SQLException e) {
//		e.printStackTrace();
//		
//	} finally {
//
//try {
//		if(rs!=null) {rs.close();}
//		if(stmt!=null) {stmt.close();}
//		DbConnector.getInstance().releaseConn();
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
//}

public class UserRepository {
	
	// el objeto userToSearch tiene el email y la password para poder buscarlo en la base de datos.
	public User getOne(User userToSearch) throws SQLException {
		User u = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"select email, name, surname, phone_number, dni, address, role from user where "
					+ "email=? and password=?"
					); // solo los datos que se necesitan mostrar en los siguientes casos de uso, excluyendo la password.
			if (stmt == null) {}
			stmt.setString(1, userToSearch.getEmail());
			stmt.setString(2, userToSearch.getPassword());
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				u = new User();
				u.setEmail(rs.getString("email"));
				u.setName(rs.getString("name"));
				u.setSurname(rs.getString("surname"));
				u.setPhone_number(rs.getString("phone_number"));
				u.setDni(rs.getString("dni"));
				u.setAddress(rs.getString("address"));
				u.setRole(rs.getString("role"));
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("aca!");
				if (rs != null) { rs.close(); }
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return u;
		
	}
	
	public Boolean addUser(User userToAdd) throws SQLException {
		Boolean result = false;
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"insert into user (email, password, name, surname, phone_number, dni, address, role) values (?, ?, ?, ?, ?, ?, ?, ?)"
					);
			stmt.setString(1, userToAdd.getEmail());
			stmt.setString(2, userToAdd.getPassword());
			stmt.setString(3, userToAdd.getName());
			stmt.setString(4, userToAdd.getSurname());
			stmt.setString(5, userToAdd.getPhone_number());
			stmt.setString(6, userToAdd.getDni());
			stmt.setString(7, userToAdd.getAddress());
			stmt.setString(8, "client");
			
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
	
	public Boolean deleteUser(String email) throws SQLException {
		Boolean result = false;
		String errorMessage = null;
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"delete from user where email=?"
					);
			stmt.setString(1, email);
			stmt.executeUpdate();
			
			result = true;
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			errorMessage = e1.getMessage();
			result = false;
		} finally {
			try {
				if (stmt != null) { stmt.close(); }
				DbConnector.getInstance().releaseConn();
			} catch (SQLException e2) {
				e2.printStackTrace();
				result = false;
			}
			
			if (errorMessage != null) {
				result = false;
			}
		}
		return result;
	}
	
	public Boolean updateUser(User userToUpdate) throws SQLException {
		
		Boolean result = false;
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"update user set password=?, name=?, surname=?, phone_number=?, dni=?, address=? where email=?"
					);
			stmt.setString(1, userToUpdate.getPassword());
			stmt.setString(2, userToUpdate.getName());
			stmt.setString(3, userToUpdate.getSurname());
			stmt.setString(4, userToUpdate.getPhone_number());
			stmt.setString(5, userToUpdate.getDni());
			stmt.setString(6, userToUpdate.getAddress());
			stmt.setString(7, userToUpdate.getEmail());
			
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

	// actualiza los datos que puede tocar un admin (incluye el rol), nunca la password.
	public Boolean updateUserAdmin(User userToUpdate) throws SQLException {

		Boolean result = false;
		PreparedStatement stmt = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"update user set name=?, surname=?, phone_number=?, dni=?, address=?, role=? where email=?"
					);
			stmt.setString(1, userToUpdate.getName());
			stmt.setString(2, userToUpdate.getSurname());
			stmt.setString(3, userToUpdate.getPhone_number());
			stmt.setString(4, userToUpdate.getDni());
			stmt.setString(5, userToUpdate.getAddress());
			stmt.setString(6, userToUpdate.getRole());
			stmt.setString(7, userToUpdate.getEmail());

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

	public LinkedList<User> getAll() throws SQLException{
		LinkedList<User> users = new LinkedList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DbConnector.getInstance().getConn().prepareStatement(
					"select email, name, surname, phone_number, dni, address, role from user"
					);
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					User u = new User();
					u.setEmail(rs.getString("email"));
					u.setName(rs.getString("name"));
					u.setSurname(rs.getString("surname"));
					u.setPhone_number(rs.getString("phone_number"));
					u.setDni(rs.getString("dni"));
					u.setAddress(rs.getString("address"));
					u.setRole(rs.getString("role"));
					users.add(u);
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
		return users;
	}
	
}
