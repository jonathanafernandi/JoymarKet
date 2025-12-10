package controller;

import java.util.List;

import model.CartItem;
import model.Customer;
import model.OrderDetail;
import model.OrderHeader;
import model.Product;
import model.Promo;

public class CustomerHandler {
	
	public static String topUpBalance(String idCustomer, double amount) {
		if (amount < 10000) {
			return "Top-up amount must be at least Rp10.000.";
		}
		
		Customer customer = Customer.getCustomer(idCustomer);
		if (customer == null) {
			return "Customer not found.";
		}
		
		boolean success = customer.topUpBalance(amount);
		if (!success) {
			return "Failed to top up balance.";
		}
		
		System.out.println("Balance topped up: " + idCustomer + " + " + amount + ".");
		return null;
	}
	
	public static String addToCart(String idCustomer, String idProduct, int quantity) {
		if (quantity <= 0) {
			return "Quantity must be greater than 0.";
		}
		
		Product product = Product.getProduct(idProduct);
		if (product == null) {
			return "Product not found.";
		}
		
		if (product.getStock() <= 0) {
			return "Product is out of stock.";
		}
		
		if (quantity > product.getStock()) {
			return "Quantity exceeds available stock. Available: " + product.getStock() + ".";
		}
		
		CartItem existingItem = CartItem.getCartItem(idCustomer, idProduct);
		
		if (existingItem != null) {
			int newQuantity = existingItem.getCount() + quantity;
			
			if (newQuantity > product.getStock()) {
				return "Total quantity exceeds available stock. Available: " + product.getStock() + ".";
			}
			
			boolean success = existingItem.editCartItem(newQuantity);
			if (!success) {
				return "Failed to update cart.";
			}
		} else {
			boolean success = CartItem.createCartItem(idCustomer, idProduct, quantity);
			if (!success) {
				return "Failed to add to cart.";
			}
		}
		
		System.out.println("Product added to cart: " + idProduct + " x " + quantity + ".");
		return null;
	}
	
	public static String removeFromCart(String idCustomer, String idProduct) {
		CartItem cartItem = CartItem.getCartItem(idCustomer, idProduct);
		if (cartItem == null) {
			return "Item not found in cart.";
		}
		
		boolean success = CartItem.deleteCartItem(idCustomer, idProduct);
		if (!success) {
			return "Failed to remove from cart.";
		}
		
		System.out.println("Product removed from cart: " + idProduct + ".");
		return null;
	}
	
	public static String checkout(String idCustomer, String promoCode) {
		List<CartItem> cartItems = CartItem.getCartItems(idCustomer);
		
		if (cartItems.isEmpty()) {
			return "Cart is empty.";
		}
		
		double subtotal = 0;
		for (CartItem cartItem : cartItems) {
			Product product = Product.getProduct(cartItem.getIdProduct());
			if (product == null) {
				return "Product not found: " + cartItem.getIdProduct();
			}
			
			if (product.getStock() < cartItem.getCount()) {
				return "Insufficient stock for: " + product.getName() + ".";
			}
			
			subtotal += product.getPrice() * cartItem.getCount();
		}
		
		String idPromo = null;
		double discount = 0;
		
		if (promoCode != null && !promoCode.isEmpty()) {
			Promo promo = Promo.getPromo(promoCode);
			if (promo == null) {
				return "Invalid promo code.";
			}
			idPromo = promo.getIdPromo();
			discount = subtotal * promo.getDiscountPercentage();
		}
		
		double totalAmount = subtotal - discount;
		
		Customer customer = Customer.getCustomer(idCustomer);
		if (customer == null) {
			return "Customer not found.";
		}
		
		if (customer.getBalance() < totalAmount) {
			return "Insufficient balance. Your balance: Rp" + customer.getBalance() + ", Total: Rp" + totalAmount + ".";
		}
		
		boolean orderCreted = OrderHeader.createOrderHeader(idCustomer, idPromo, totalAmount);
		if (!orderCreted) {
			return "Failed to create order.";
		}
		
		List<OrderHeader> orders = OrderHeader.getCustomerOrders(idCustomer);
		if (orders.isEmpty()) {
			return "Failed to retrieve order.";
		}
		OrderHeader newOrder = orders.get(0);
		
		for (CartItem cartItem : cartItems) {
			boolean detailCreted = OrderDetail.createOrderDetail(newOrder.getIdOrder(), cartItem.getIdProduct(), cartItem.getCount());
			
			if (!detailCreted) {
				return "Failed to create order details.";
			}
			
			Product product = Product.getProduct(cartItem.getIdProduct());
			int newStock = product.getStock() - cartItem.getCount();
			boolean stockUpdated = product.editProductStock(newStock);
			
			if (!stockUpdated) {
				return "Failed to update product stock.";
			}
		}
		
		double newBalance = customer.getBalance() - totalAmount;
		boolean balanceUpdated = Customer.updateBalance(idCustomer, newBalance);
		if (!balanceUpdated) {
			return "Failed to update balance;";
		}
		
		boolean cartCleared = CartItem.clearCart(idCustomer);
		if (!cartCleared) {
			return "Failed to clear cart.";
		}
		
		System.out.println("Checkout successful: Order " + newOrder.getIdOrder() + ".");
		return null;
	}
	
	public static Customer getCustomer(String idCustomer) {
		return Customer.getCustomer(idCustomer);
	}

}
