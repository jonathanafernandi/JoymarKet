package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Product;

// Data Access class for Product table
public class ProductDA {

	private Connection connection;
	
    // Initialize database connection
	public ProductDA() {
		this.connection = DatabaseConnection.getInstance().getConnection();
	}
	
    // Get product data by product ID
	public Product getProduct(String idProduct) {
		String query = "SELECT * FROM Product WHERE idProduct = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idProduct);
			ResultSet rs = ps.executeQuery();
			
            // Create Product object if data is found
			if (rs.next()) {
				return new Product(
                    rs.getString("idProduct"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                );
			}
		} catch (SQLException e) {
			System.err.println("Error getting product: " + idProduct + ".");
			e.printStackTrace();
		}
		return null;
	}
	
    // Get all products from database
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM Product";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
            // Read each product from result set
			while (rs.next()) {
				Product product = new Product(
                    rs.getString("idProduct"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                );
				products.add(product);
			}
		} catch (SQLException e) {
			System.err.println("Error getting all products");
			e.printStackTrace();
		}
		return products;
	}
	
    // Get products that have stock greater than zero
	public List<Product> getAvailableProducts() {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM Product WHERE stock > 0";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
            // Read available products
			while (rs.next()) {
				Product product = new Product(
                    rs.getString("idProduct"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("category")
                );
				products.add(product);
			}
		} catch (SQLException e) {
			System.err.println("Error getting available products.");
			e.printStackTrace();
		}
		return products;
	}
	
    // Save new product data into database
	public boolean saveProduct(Product product) {
		String query = "INSERT INTO Product VALUES (?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, product.getIdProduct());
			ps.setString(2, product.getName());
			ps.setDouble(3, product.getPrice());
			ps.setInt(4, product.getStock());
			ps.setString(5, product.getCategory());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Product saved: " + product.getName() + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error saving product: " + product.getName() + ".");
			e.printStackTrace();
		}
		return false;
	}
	
    // Update stock value for a product
	public boolean updateProductStock(String idProduct, int newStock) {
		String query = "UPDATE Product SET stock = ? WHERE idProduct = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, newStock);
			ps.setString(2, idProduct);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Product stock updated: " + idProduct + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error updating product stock: " + idProduct + ".");
			e.printStackTrace();
		}
		return false;
	}
	
    // Delete product from database
	public boolean deleteProduct(String idProduct) {
		String query = "DELETE FROM Product WHERE idProduct = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, idProduct);
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Product deleted: " + idProduct + ".");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error deleting product: " + idProduct);
			e.printStackTrace();
		}
		return false;
	}

}
