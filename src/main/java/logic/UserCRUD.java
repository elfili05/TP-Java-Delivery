package main.java.logic;
import main.java.data.UserRepository;
import java.sql.*;
import java.util.LinkedList;
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

	// usados por el panel admin para listar/editar/eliminar usuarios.
	public LinkedList<User> getAll() throws SQLException {
		return ur.getAll();
	}

	public Boolean updateUser(User userToUpdate) throws SQLException {
		return ur.updateUserAdmin(userToUpdate);
	}

	public Boolean deleteUser(String email) throws SQLException {
		return ur.deleteUser(email);
	}

}
