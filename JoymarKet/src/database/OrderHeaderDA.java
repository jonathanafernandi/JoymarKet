package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.OrderHeader;

public class OrderHeaderDA {

	private Connection connection;
	
	public OrderHeaderDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
	public OrderHeader getOrderHeader(String idOrder) {
		String query = "SELECT * FROM OrderHeader WHERE idOrder = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idOrder);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return new OrderHeader(rs.getString("idOrder"), rs.getString("idCustomer"), rs.getString("idPromo"), rs.getString("status"), rs.getTimestamp("orderedAt"), rs.getDouble("totalAmount"));
			}
		} catch (SQLException e) {
			System.err.println("Error getting order header: " + idOrder + ".");
			e.printStackTrace();
		}
		return null;
	}
	
	public OrderHeader getCustomerOrderHeader(String idOrder, String idCustomer) {
		String query = "SELECT * FROM OrderHeader WHERE idOrder = ? AND idCustomer = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idOrder);
			ps.setString(2, idCustomer);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return new OrderHeader(rs.getString("idOrder"), rs.getString("idCustomer"), rs.getString("idPromo"), rs.getString("status"), rs.getTimestamp("orderedAt"), rs.getDouble("totalAmount"));
			}
		} catch (SQLException e) {
			System.err.println("Error getting customer order header.");
			e.printStackTrace();
		}
		return null;
	}
	
	public List<OrderHeader> getCustomerOrders(String idCustomer) {
		List<OrderHeader> orders = new ArrayList<>();
		String query = "SELECT * FROM OrderHeader WHERE idCustomer = ? ORDER BY orderedAt DESC";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idCustomer);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				OrderHeader order = new OrderHeader(rs.getString("idOrder"), rs.getString("idCustomer"), rs.getString("idPromo"), rs.getString("status"), rs.getTimestamp("orderedAt"), rs.getDouble("totalAmount"));
				orders.add(order);
				
			}
		} catch (SQLException e) {
			System.err.println("Error getting customer orders.");
			e.printStackTrace();
		}
		return orders;
	}
	
	public List<OrderHeader> getAllOrders() {
		List<OrderHeader> orders = new ArrayList<>();
		String query = "SELECT * FROM OrderHeader ORDER BY orderedAt DESC";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				OrderHeader order = new OrderHeader(rs.getString("idOrder"), rs.getString("idCustomer"), rs.getString("idPromo"), rs.getString("status"), rs.getTimestamp("orderedAt"), rs.getDouble("totalAmount"));
				orders.add(order);
				
			}
		} catch (SQLException e) {
			System.err.println("Error getting all orders.");
			e.printStackTrace();
		}
		return orders;
	}
	
	public boolean saveOrderHeader(OrderHeader orderHeader) {
		String query = "INSERT INTO OrderHeader VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, orderHeader.getIdOrder());
			ps.setString(2, orderHeader.getIdCustomer());
			ps.setString(3, orderHeader.getIdPromo());
			ps.setString(4, orderHeader.getStatus());
			ps.setTimestamp(5, orderHeader.getOrderedAt());
			ps.setDouble(6, orderHeader.getTotalAmount());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Order header saved: " + orderHeader.getIdOrder() + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving order header.");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateOrderStatus(String idOrder, String newStatus) {
		String query = "UPDATE OrderHeader SET status = ? WHERE idOrder = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, newStatus);
			ps.setString(2, idOrder);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Order status updates: " + idOrder + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error updating order status.");
			e.printStackTrace();
		}
		return false;
	}
	
	public synchronized String generateOrderID() {
		try {
			String query = "SELECT idOrder FROM OrderHeader WHERE idOrder LIKE 'OR%' ORDER BY idOrder DESC LIMIT 1";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			int nextNumber = 1;
			
			if (rs.next()) {
				String lastID = rs.getString("idOrder");
				String numberPart = lastID.substring(2);
				int currentNumber = Integer.parseInt(numberPart);
				nextNumber = currentNumber + 1;
			}
			
			String formattedNumber = String.valueOf(nextNumber);
			while (formattedNumber.length() < 3) {
				formattedNumber = "0" + formattedNumber;
				
			}
			
			return "OR" + formattedNumber;
		} catch (SQLException e) {
			System.err.println("Error generating order ID.");
			e.printStackTrace();
			return null;
		}
	}

}
