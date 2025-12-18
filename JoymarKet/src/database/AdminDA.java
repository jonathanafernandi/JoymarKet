package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Admin;

// Data Access class for Admin table
public class AdminDA {

	private Connection connection;
	
    // Initialize database connection
	public AdminDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
    // Get admin data by admin ID
	public Admin getAdmin(String idAdmin) {
		String query = "SELECT u.*, a.emergencyContact FROM User u JOIN Admin a ON u.idUser = a.idAdmin WHERE u.idUser = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idAdmin);
			ResultSet rs = ps.executeQuery();
			
            // Create Admin object if data is found
			if (rs.next()) {
				return new Admin(
                    rs.getString("idUser"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("role"),
                    rs.getString("emergencyContact")
                );
			}
		} catch (SQLException e) {
			System.err.println("Error getting admin: " + idAdmin + ".");
			e.printStackTrace();
		}
		return null;
	}
	
    // Save new admin data into database
	public boolean saveAdmin(Admin admin) {
		String query = "INSERT INTO Admin VALUES (?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, admin.getIdUser());
			ps.setString(2, admin.getEmergencyContact());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Admin saved: " + admin.getEmail() + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving admin: " + admin.getEmail() + ".");
			e.printStackTrace();
		}
		return false;
	}
	
    // Update existing admin data
	public boolean updateAdmin(Admin admin) {
		String query = "UPDATE Admin SET emergencyContact = ? WHERE idAdmin = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, admin.getEmergencyContact());
			ps.setString(2, admin.getIdUser());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Admin updated: " + admin.getEmail() + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error updating admin: " + admin.getEmail());
			e.printStackTrace();
		}
		return false;
	}

}
