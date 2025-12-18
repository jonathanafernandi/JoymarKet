package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Promo;

// Data Access class for Promo table
public class PromoDA {

	private Connection connection;
	
    // Initialize database connection
	public PromoDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
    // Get promo data using promo code
	public Promo getPromoByCode(String code) {
		String query = "SELECT * FROM Promo WHERE code = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			
            // Create Promo object if data is found
			if (rs.next()) {
				return new Promo(
                    rs.getString("idPromo"),
                    rs.getString("code"),
                    rs.getString("headline"),
                    rs.getDouble("discountPercentage")
                );
			}
		} catch (SQLException e) {
			System.err.println("Error getting promo by code: " + code + ".");
			e.printStackTrace();
		}
		return null;
	}
	
    // Get promo data by promo ID
	public Promo getPromo(String idPromo) {
		String query = "SELECT * FROM Promo WHERE idPromo = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idPromo);
			ResultSet rs = ps.executeQuery();
			
            // Create Promo object if data is found
			if (rs.next()) {
				return new Promo(
                    rs.getString("idPromo"),
                    rs.getString("code"),
                    rs.getString("headline"),
                    rs.getDouble("discountPercentage")
                );
			}
		} catch (SQLException e) {
			System.err.println("Error getting promo: " + idPromo);
			e.printStackTrace();
		}
		return null;
	}
	
    // Get all promo data from database
	public List<Promo> getAllPromos() {
		List<Promo> promos = new ArrayList<>();
		String query = "SELECT * FROM Promo";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
            // Read each promo from result set
			while (rs.next()) {
				Promo promo = new Promo(
                    rs.getString("idPromo"),
                    rs.getString("code"),
                    rs.getString("headline"),
                    rs.getDouble("discountPercentage")
                );
				promos.add(promo);
			}
		} catch (SQLException e) {
			System.err.println("Error getting all promos.");
			e.printStackTrace();
		}
		return promos;
	}
	
    // Save new promo data into database
	public boolean savePromo(Promo promo) {
		String query = "INSERT INTO Promo VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, promo.getIdPromo());
			ps.setString(2, promo.getCode());
			ps.setString(3, promo.getHeadline());
			ps.setDouble(4, promo.getDiscountPercentage());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Promo saved: " + promo.getCode() + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving promo: " + promo.getCode() + ".");
			e.printStackTrace();
		}
		return false;
	}

}
