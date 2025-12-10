package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Courier;

public class CourierDA {

	private Connection connection;
	
	public CourierDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
	public Courier getCourier(String idCourier) {
		String query = "SELECT u.*, c.vehicleType, c.vehiclePlate FROM User u JOIN Courier c ON u.idUser = c.idCourier WHERE u.idUser = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idCourier);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return new Courier(rs.getString("idUser"), rs.getString("fullName"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("address"), rs.getString("role"), rs.getString("vehicleType"), rs.getString("vehiclePlate"));
			}
		} catch (SQLException e) {
			System.out.println("Error getting courier: " + idCourier + ".");
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Courier> getAllCouriers() {
		List<Courier> couriers = new ArrayList<>();
		String query = "SELECT u.*, c.vehicleType, c.vehiclePlate FROM User u JOIN Courier c ON u.idUser = c.idCourier";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Courier courier = new Courier(rs.getString("idUser"), rs.getString("fullName"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("address"), rs.getString("role"), rs.getString("vehicleType"), rs.getString("vehiclePlate"));
				couriers.add(courier);
			}
		} catch (SQLException e) {
			System.err.println("Error getting all couriers.");
			e.printStackTrace();
		}
		return couriers;
	}
	
	public boolean saveCourier(Courier courier) {
		String query = "INSERT INTO Courier VALUES (?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, courier.getIdUser());
			ps.setString(2, courier.getVehicleType());
			ps.setString(3, courier.getVehiclePlate());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Courier saved: " + courier.getEmail() + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving courier: " + courier.getEmail() + ".");
			e.printStackTrace();
		}
		return false;
	}

}
