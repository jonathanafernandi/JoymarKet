package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDA {

	private Connection connection;
	
	public UserDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
	public User getUser(String idUser) {
		String query = "SELECT * FROM User WHERE idUser = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idUser);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return new User(rs.getString("idUser"), rs.getString("fullName"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("address"), rs.getString("role"));
			}
		} catch (SQLException e) {
			System.err.println("Error getting user by ID: " + idUser + ".");
			e.printStackTrace();
		}
		return null;
	}
	
	public User getUserByEmail(String email) {
		String query = "SELECT * FROM User WHERE email = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return new User(rs.getString("idUser"), rs.getString("fullName"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("address"), rs.getString("role"));
			}
		} catch (SQLException e) {
			System.err.println("Error getting user by email: " + email + ".");
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean saveUser(User user) {
		String query = "INSERT INTO User VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, user.getIdUser());
			ps.setString(2, user.getFullName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPassword());
			ps.setString(5, user.getPhone());
			ps.setString(6, user.getAddress());
			ps.setString(7, user.getRole());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User saved: " + user.getEmail() + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving user: " + user.getEmail() + ".");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateUser(User user) {
		String query = "UPDATE User SET fullName = ?, email = ?, password = ?, phone = ?, address = ? WHERE idUser = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, user.getFullName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getPhone());
			ps.setString(5, user.getAddress());
			ps.setString(6, user.getIdUser());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User updated: " + user.getEmail() + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error updating user: " + user.getEmail() + ".");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteUser(String idUser) {
		String query = "DELETE FROM User WHERE idUser = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idUser);

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User deleted: " + idUser + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error deleting user: " + idUser + ".");
			e.printStackTrace();
		}
		return false;
	}

}
