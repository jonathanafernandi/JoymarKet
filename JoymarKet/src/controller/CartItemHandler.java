package controller;

import java.util.List;

import model.CartItem;
import model.Product;

public class CartItemHandler {

	public static List<CartItem> getCartItems(String idCustomer) {
		return CartItem.getCartItems(idCustomer);
	}
	
	public static boolean createCartItem(String idCustomer, String idProduct, int count) {
	    return CartItem.createCartItem(idCustomer, idProduct, count);
	}
	
	public static boolean deleteCartItem(String idCustomer, String idProduct) {
		return CartItem.deleteCartItem(idCustomer, idProduct);
	}
	
	public static String editCartItem(String idCustomer, String idProduct, String countString) {
		Product product = Product.getProduct(idProduct);
		if (product == null) {
			return "Product not found.";
		}
		
		if (countString == null || countString.trim().isEmpty()) {
			return "Count must be filled!";
		}
		
		int count;
		try {
			count = Integer.parseInt(countString.trim());
		} catch (NumberFormatException e) {
			return "Count must be numeric.";
		}
		
		if (count < 1) {
			return "Count must be at least 1.";
		}
		if (count > product.getStock()) {
			return "Count exceeds available stock. Available: " + product.getStock();
		}
		
		CartItem cartItem = CartItem.getCartItem(idCustomer, idProduct);
		if (cartItem == null) {
			return "Cart item not found.";
		}
		
		boolean success = cartItem.editCartItem(count);
		if (!success) {
			return "Failed to update cart.";
		}
		
		System.out.println("Cart updated: " + product.getName() + " x " + count);
		return null;
	}
	
	public static boolean clearCart(String idCustomer) {
		return CartItem.clearCart(idCustomer);
	}

}
