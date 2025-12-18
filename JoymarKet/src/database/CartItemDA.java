package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.CartItem;

// Data Access class for CartItem table
public class CartItemDA {

	private Connection connection;
	
    // Initialize database connection
	public CartItemDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
    // Get all cart items for a specific customer
	public List<CartItem> getCartItems(String idCustomer) {
		List<CartItem> cartItems = new ArrayList<>();
		String query = "SELECT * FROM CartItem WHERE idCustomer = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idCustomer);
			ResultSet rs = ps.executeQuery();
			
            // Read each cart item from result set
			while (rs.next()) {
				CartItem cartItem = new CartItem(
                    rs.getString("idCustomer"),
                    rs.getString("idProduct"),
                    rs.getInt("count")
                );
				cartItems.add(cartItem);
			}
		} catch (SQLException e) {
			System.err.println("Error getting cart items for customer: " + idCustomer + ".");
			e.printStackTrace();
		}
		return cartItems;
	}
	
    // Get a specific cart item by customer and product
	public CartItem getCartItem(String idCustomer, String idProduct) {
		String query = "SELECT * FROM CartItem WHERE idCustomer = ? AND idProduct = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idCustomer);
			ps.setString(2, idProduct);
			ResultSet rs = ps.executeQuery();
			
            // Return cart item if found
			if (rs.next()) {
				return new CartItem(
                    rs.getString("idCustomer"),
                    rs.getString("idProduct"),
                    rs.getInt("count")
                );
			}
		} catch (SQLException e) {
			System.err.println("Error getting cart item.");
			e.printStackTrace();
		}
		return null;
	}
	
    // Save new cart item into database
	public boolean saveCartItem(CartItem cartItem) {
		String query = "INSERT INTO CartItem VALUES (?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, cartItem.getIdCustomer());
			ps.setString(2, cartItem.getIdProduct());
			ps.setInt(3, cartItem.getCount());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Cart item saved.");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving cart item.");
			e.printStackTrace();
		}
		return false;
	}
	
    // Update quantity of an existing cart item
	public boolean updateCartItem(String idCustomer, String idProduct, int newCount) {
		String query = "UPDATE CartItem SET count = ? WHERE idCustomer = ? AND idProduct = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, newCount);
			ps.setString(2, idCustomer);
			ps.setString(3, idProduct);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Cart item updated.");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error updating cart item.");
			e.printStackTrace();
		}
		return false;
	}
	
    // Delete a cart item from database
	public boolean deleteCartItem(String idCustomer, String idProduct) {
		String query = "DELETE FROM CartItem WHERE idCustomer = ? AND idProduct = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idCustomer);
			ps.setString(2, idProduct);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Cart item deleted.");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error deleting cart item.");
			e.printStackTrace();
		}
		return false;
	}
	
    // Remove all cart items for a customer
	public boolean clearCart(String idCustomer) {
		String query = "DELETE FROM CartItem WHERE idCustomer = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idCustomer);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Cart cleared for customer: " + idCustomer + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error clearing cart.");
			e.printStackTrace();
		}
		return false;
	}

}
