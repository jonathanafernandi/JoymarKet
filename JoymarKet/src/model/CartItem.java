package model;

import java.util.List;

import database.CartItemDA;

public class CartItem {

	private String idCustomer;
	private String idProduct;
	private int count;
	
	public CartItem(String idCustomer, String idProduct, int count) {
		this.idCustomer = idCustomer;
		this.idProduct = idProduct;
		this.count = count;
	}

	public String getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public static boolean createCartItem(String idCustomer, String idProduct, int count) {
		CartItem cartItem = new CartItem(idCustomer, idProduct, count);
		CartItemDA cartItemDA = new CartItemDA();
		return cartItemDA.saveCartItem(cartItem);
	}
	
	public static boolean deleteCartItem(String idCustomer, String idProduct) {
		CartItemDA cartItemDA = new CartItemDA();
		return cartItemDA.deleteCartItem(idCustomer, idProduct);
	}
	
	public boolean editCartItem(int newCount) {
		this.count = newCount;
		CartItemDA cartItemDA = new CartItemDA();
		return cartItemDA.updateCartItem(this.idCustomer, this.idProduct, newCount);
	}
	
	public static List<CartItem> getCartItems(String idCustomer) {
	    CartItemDA cartItemDA = new CartItemDA();
	    return cartItemDA.getCartItems(idCustomer);
	}
	
	public static CartItem getCartItem(String idCustomer, String idProduct) {
        CartItemDA cartItemDA = new CartItemDA();
        return cartItemDA.getCartItem(idCustomer, idProduct);
    }
	
	public static boolean clearCart(String idCustomer) {
        CartItemDA cartItemDA = new CartItemDA();
        return cartItemDA.clearCart(idCustomer);
    }

}
