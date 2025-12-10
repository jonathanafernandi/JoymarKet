package controller;

import model.CartItem;
import model.Customer;
import model.OrderDetail;
import model.OrderHeader;
import model.Product;
import model.Promo;

import java.util.List;

public class OrderHeaderHandler {

	public static List<OrderHeader> getAllOrders() {
		return OrderHeader.getAllOrders();
	}
	
	public static List<OrderHeader> getCustomerOrders(String idCustomer) {
		return OrderHeader.getCustomerOrders(idCustomer);
	}
	
	public static OrderHeader getOrderHeader(String idOrder) {
		return OrderHeader.getOrderHeader(idOrder);
	}
	
	public static String checkout(Customer customer, String promoCode) {
		String idCustomer = customer.getIdUser();
		
		List<CartItem> cartItems = CartItem.getCartItems(idCustomer);
		
		if (cartItems.isEmpty()) {
			return "Cart is empty.";
		}
		
		double total = 0;
		for (CartItem cartItem : cartItems) {
			Product product = Product.getProduct(cartItem.getIdProduct());
			if (product == null) {
				return "Product " + cartItem.getIdProduct() + " not found.";
			}
			
			if (product.getStock() < cartItem.getCount()) {
				return "Insufficient stock for " + product.getName() + ". Available: " + product.getStock() + ".";
			}
			
			total += product.getPrice() * cartItem.getCount();
		}
		
		String idPromo = null;
		if (promoCode != null && !promoCode.trim().isEmpty()) {
			Promo promo = Promo.getPromo(promoCode.trim());
			if (promo == null) {
				return "Invalid promo code.";
			}
			
			idPromo = promo.getIdPromo();
			double discount = total * promo.getDiscountPercentage();
			total -= discount;
			
			System.out.println("Promo applied: " + promo.getHeadline() + " (-" + (promo.getDiscountPercentage() * 100) + "%).");
		}
		
		if (customer.getBalance() < total) {
			return "Insufficient balance. Required: Rp" + String.format("%,d", (int)total) + ", Current: Rp" + String.format("%,d", (int)customer.getBalance()) + ".";
		}
		
		boolean orderCreated = OrderHeader.createOrderHeader(idCustomer, idPromo, total);
		if (!orderCreated) {
			return "Failed to create order.";
		}
		
		List<OrderHeader> orders = OrderHeader.getCustomerOrders(idCustomer);
		if (orders.isEmpty()) {
			return "Order not found after creation.";
		}
		OrderHeader order = orders.get(0);
		
		for (CartItem cartItem : cartItems) {
			Product product = Product.getProduct(cartItem.getIdProduct());
			
			OrderDetail.createOrderDetail(order.getIdOrder(), cartItem.getIdProduct(), cartItem.getCount());
			
			int newStock = product.getStock() - cartItem.getCount();
			product.editProductStock(newStock);
		}
		
		double newBalance = customer.getBalance() - total;
		Customer.updateBalance(idCustomer, newBalance);
		customer.setBalance(newBalance);
		
		CartItem.clearCart(idCustomer);
		
		System.out.println("Order created successfully. Order ID: " + order.getIdOrder() + ".");
		System.out.println("Total: Rp" + String.format("%,d", (int)total));
		System.out.println("New balance: Rp" + String.format("%,d", (int)newBalance));
		
		return null;
	}
	
	public static List<OrderDetail> getOrderDetails(String idOrder) {
		return OrderDetail.getOrderDetails(idOrder);
	}

}
