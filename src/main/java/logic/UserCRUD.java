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

	// usados por la pantalla "Mi perfil": el usuario consulta y edita sus propios datos.
	public User getById(int userId) throws SQLException {
		return ur.getById(userId);
	}

	// lanza SQLIntegrityConstraintViolationException si el email ya pertenece a otro usuario.
	public Boolean updateProfile(User userToUpdate) throws SQLException {
		return ur.updateProfile(userToUpdate);
	}

}
