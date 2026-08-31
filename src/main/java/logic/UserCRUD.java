package main.java.logic;
import main.java.data.UserRepository;
import java.sql.*;
import main.java.entities.User;

public class UserCRUD {
	
	private UserRepository ur;

	public UserCRUD() {
		ur = new UserRepository();
	}
	
	public User validateUser(User u) throws SQLException {
		
		User result = ur.getOne(u);
		if (result != null) {
			return result;
		}
		else {
			return u;
		}
		
	}
	
	public Boolean addUser(User userToAdd) throws SQLException {
		return ur.addUser(userToAdd);
	}
	
}
