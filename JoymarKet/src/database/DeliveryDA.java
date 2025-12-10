package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Delivery;

public class DeliveryDA {

	private Connection connection;
	
	public DeliveryDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
	public Delivery getDelivery(String idOrder, String idCourier) {
		String query = "SELECT * FROM Delivery WHERE idOrder = ? AND idCourier = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idOrder);
			ps.setString(2, idCourier);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return new Delivery(rs.getString("idOrder"), rs.getString("idCourier"), rs.getString("status"));
			}
		} catch (SQLException e) {
			System.err.println("Error getting delivery.");
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Delivery> getAllDeliveries() {
		List<Delivery> deliveries = new ArrayList<>();
		String query = "SELECT * FROM Delivery";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Delivery delivery = new Delivery(rs.getString("idOrder"), rs.getString("idCourier"), rs.getString("status"));
				deliveries.add(delivery);
				
			}
		} catch (SQLException e) {
			System.err.println("Error getting all deliveries.");
			e.printStackTrace();
		}
		return deliveries;
	}
	
	public List<Delivery> getCourierDeliveries(String idCourier) {
		List<Delivery> deliveries = new ArrayList<>();
		String query = "SELECT * FROM Delivery WHERE idCourier = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idCourier);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Delivery delivery = new Delivery(rs.getString("idOrder"), rs.getString("idCourier"), rs.getString("status"));
				deliveries.add(delivery);
				
			}
		} catch (SQLException e) {
			System.err.println("Error getting courier deliveries.");
			e.printStackTrace();
		}
		return deliveries;
	}
	
	public boolean saveDelivery(Delivery delivery) {
		String query = "INSERT INTO Delivery VALUES (?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, delivery.getIdOrder());
			ps.setString(2, delivery.getIdCourier());
			ps.setString(3, delivery.getStatus());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Delivery saved.");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving delivery.");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateDeliveryStatus(String idOrder, String idCourier, String newStatus) {
		String query = "UPDATE Delivery SET status = ? WHERE idOrder = ? AND idCourier = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, newStatus);
			ps.setString(2, idOrder);
			ps.setString(3, idCourier);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Delivery status updated.");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error updating delivery status.");
			e.printStackTrace();
		}
		return false;
	}

}
