package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.OrderDetail;

// Data Access class for OrderDetail table
public class OrderDetailDA {

	private Connection connection;
	
    // Initialize database connection
	public OrderDetailDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
    // Get a specific order detail by order ID and product ID
	public OrderDetail getOrderDetail(String idOrder, String idProduct) {
		String query = "SELECT * FROM OrderDetail WHERE idOrder = ? AND idProduct = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idOrder);
			ps.setString(2, idProduct);
			ResultSet rs = ps.executeQuery();
			
            // Create OrderDetail object if data is found
			if (rs.next()) {
				return new OrderDetail(
                    rs.getString("idOrder"),
                    rs.getString("idProduct"),
                    rs.getInt("qty")
                );
			}
		} catch (SQLException e) {
			System.err.println("Error getting order detail.");
			e.printStackTrace();
		}
		return null;
	}
	
    // Get all order details for a specific order
	public List<OrderDetail> getOrderDetails(String idOrder) {
		List<OrderDetail> orderDetails = new ArrayList<>();
		String query = "SELECT * from OrderDetail WHERE idOrder = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idOrder);
			ResultSet rs = ps.executeQuery();
			
            // Read each order detail from result set
			while (rs.next()) {
				OrderDetail orderDetail = new OrderDetail(
                    rs.getString("idOrder"),
                    rs.getString("idProduct"),
                    rs.getInt("qty")
                );
				orderDetails.add(orderDetail);
			}
		} catch (SQLException e) {
			System.err.println("Error getting order details.");
			e.printStackTrace();
		}
		return orderDetails;
	}
	
    // Get order detail for a customer order
	public OrderDetail getCustomerOrderDetail(String idOrder, String idProduct) {
		String query = "SELECT od.* FROM OrderDetail od JOIN OrderHeader oh ON od.idOrder = oh.idOrder WHERE od.idOrder = ? AND od.idProduct = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idOrder);
			ps.setString(2, idProduct);
			ResultSet rs = ps.executeQuery();
			
            // Create OrderDetail object if data is found
			if (rs.next()) {
				return new OrderDetail(
                    rs.getString("idOrder"),
                    rs.getString("idProduct"),
                    rs.getInt("qty")
                );
			}
		} catch (SQLException e) {
			System.err.println("Error getting customer order detail.");
			e.printStackTrace();
		}
		return null;
	}
	
    // Save new order detail into database
	public boolean saveOrderDetail(OrderDetail orderDetail) {
		String query = "INSERT INTO OrderDetail VALUES (?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, orderDetail.getIdOrder());
			ps.setString(2, orderDetail.getIdProduct());
			ps.setInt(3, orderDetail.getQty());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Order detail saved.");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving order detail.");
			e.printStackTrace();
		}
		return false;
	}

}
