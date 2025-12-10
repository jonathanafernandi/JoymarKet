package controller;

import java.util.List;

import model.Product;

public class ProductHandler {

	public static Product getProduct(String idProduct) {
		return Product.getProduct(idProduct);
	}
	
	public static List<Product> getAllProducts() {
		return Product.getAllProducts();
	}
	
	public static List<Product> getAvailableProducts() {
		return Product.getAvailableProducts();
	}
	
	public static String editProductStock(String idProduct, String stockString) {
		Product product = Product.getProduct(idProduct);
		if (product == null) {
			return "Product not found.";
		}
		
		if (stockString == null || stockString.trim().isEmpty()) {
			return "Stock must be filled.";
		}
		
		int newStock;
		try {
			newStock = Integer.parseInt(stockString.trim());
		} catch (NumberFormatException e) {
			return "Stock must be numeric.";
		}
		
		if (newStock < 0) {
			return "Stock can not be negative.";
		}
		
		boolean success = product.editProductStock(newStock);
		
		if (!success) {
			return "Failed to update product stock.";
		}
		
		System.out.println("Product stock updated: " + product.getName() + " - New stock: " + newStock + ".");
		return null;
	}

}
