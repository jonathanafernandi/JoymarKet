package model;

import java.util.List;

import database.CartItemDA;

// Model class that represents an item in the shopping cart
public class CartItem {

	private String idCustomer;
	private String idProduct;
	private int count;
	
    // Constructor to create cart item object
	public CartItem(String idCustomer, String idProduct, int count) {
		this.idCustomer = idCustomer;
		this.idProduct = idProduct;
		this.count = count;
	}

    // Get customer ID
	public String getIdCustomer() {
		return idCustomer;
	}

    // Set customer ID
	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

    // Get product ID
	public String getIdProduct() {
		return idProduct;
	}

    // Set product ID
	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

    // Get product quantity
	public int getCount() {
		return count;
	}

    // Set product quantity
	public void setCount(int count) {
		this.count = count;
	}
	
    // Create new cart item in database
	public static boolean createCartItem(String idCustomer, String idProduct, int count) {
		CartItem cartItem = new CartItem(idCustomer, idProduct, count);
		CartItemDA cartItemDA = new CartItemDA();
		return cartItemDA.saveCartItem(cartItem);
	}
	
    // Delete cart item from database
	public static boolean deleteCartItem(String idCustomer, String idProduct) {
		CartItemDA cartItemDA = new CartItemDA();
		return cartItemDA.deleteCartItem(idCustomer, idProduct);
	}
	
    // Update cart item quantity
	public boolean editCartItem(int newCount) {
		this.count = newCount;
		CartItemDA cartItemDA = new CartItemDA();
		return cartItemDA.updateCartItem(this.idCustomer, this.idProduct, newCount);
	}
	
    // Get all cart items for a customer
	public static List<CartItem> getCartItems(String idCustomer) {
	    CartItemDA cartItemDA = new CartItemDA();
	    return cartItemDA.getCartItems(idCustomer);
	}
	
    // Get a specific cart item
	public static CartItem getCartItem(String idCustomer, String idProduct) {
        CartItemDA cartItemDA = new CartItemDA();
        return cartItemDA.getCartItem(idCustomer, idProduct);
    }
	
    // Clear all cart items for a customer
	public static boolean clearCart(String idCustomer) {
        CartItemDA cartItemDA = new CartItemDA();
        return cartItemDA.clearCart(idCustomer);
    }

}
