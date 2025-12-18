package controller;

import java.util.List;

import model.Product;

// Controller that handles product-related actions
public class ProductHandler {

    // Get product data by product ID
	public static Product getProduct(String idProduct) {
		return Product.getProduct(idProduct);
	}
	
    // Get all products from the database
	public static List<Product> getAllProducts() {
		return Product.getAllProducts();
	}
	
    // Get products that are currently available (stock > 0)
	public static List<Product> getAvailableProducts() {
		return Product.getAvailableProducts();
	}
	
    // Update product stock (admin action)
	public static String editProductStock(String idProduct, String stockString) {

        // Check if product exists
		Product product = Product.getProduct(idProduct);
		if (product == null) {
			return "Product not found.";
		}
		
        // Validate stock input
		if (stockString == null || stockString.trim().isEmpty()) {
			return "Stock must be filled.";
		}
		
		int newStock;
		try {
			newStock = Integer.parseInt(stockString.trim());
		} catch (NumberFormatException e) {
			return "Stock must be numeric.";
		}
		
        // Stock cannot be negative
		if (newStock < 0) {
			return "Stock can not be negative.";
		}
		
        // Update stock value
		boolean success = product.editProductStock(newStock);
		
		if (!success) {
			return "Failed to update product stock.";
		}
		
		System.out.println("Product stock updated: " + product.getName() + " - New stock: " + newStock + ".");
		return null;
	}

}
