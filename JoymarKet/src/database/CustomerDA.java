package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Customer;

public class CustomerDA {

	private Connection connection;

	public CustomerDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
	public Customer getCustomer(String idCustomer) {
		String query = "SELECT u.*, c.balance FROM User u JOIN Customer c ON u.idUser = c.idCustomer WHERE u.idUser = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idCustomer);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return new Customer(rs.getString("idUser"), rs.getString("fullName"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("address"), rs.getString("role"), rs.getDouble("balance"));
			}
		} catch (SQLException e) {
			System.err.println("Error getting customer: " + idCustomer + ".");
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean saveCustomer(Customer customer) {
		String query = "INSERT INTO Customer VALUES (?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, customer.getIdUser());
			ps.setDouble(2, customer.getBalance());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Customer saved: " + customer.getEmail() + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving customer: " + customer.getEmail() + ".");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateBalance(String idCustomer, double newBalance) {
		String query = "UPDATE Customer SET balance = ? WHERE idCustomer = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setDouble(1, newBalance);
			ps.setString(2, idCustomer);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Customer balance updated: " + idCustomer + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error updating balance: " + idCustomer + ".");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteCustomer(String idCustomer) {
		String query = "DELETE FROM Customer WHERE idCustomer = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idCustomer);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Customer deleted: " + idCustomer + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error deleting customer: " + idCustomer + ".");
			e.printStackTrace();
		}
		return false;
	}
	
	public synchronized String generateCustomerID() {
		try {
			String query = "SELECT idCustomer FROM Customer WHERE idCustomer LIKE 'CU%' ORDER BY idCustomer DESC LIMIT 1";
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			int nextNumber = 1;
			
			if (rs.next()) {
				String lastID = rs.getString("idCustomer");
				String numberPart = lastID.substring(2);
				int currentNumber = Integer.parseInt(numberPart);
				nextNumber = currentNumber + 1;
			}
			
			String formattedNumber = String.valueOf(nextNumber);
			while (formattedNumber.length() < 3) {
				formattedNumber = "0" + formattedNumber;
				
			}
			
			return "CU" + formattedNumber;
		} catch (SQLException e) {
			System.err.println("Error generating customer ID.");
			e.printStackTrace();
			return null;
		}
	}

}
