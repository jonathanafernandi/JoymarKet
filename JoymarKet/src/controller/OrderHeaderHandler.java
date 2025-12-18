package controller;

import model.CartItem;
import model.Customer;
import model.OrderDetail;
import model.OrderHeader;
import model.Product;
import model.Promo;

import java.util.List;

// Controller that handles order-related processes
public class OrderHeaderHandler {

    // Get all orders in the system (admin purpose)
	public static List<OrderHeader> getAllOrders() {
		return OrderHeader.getAllOrders();
	}
	
    // Get all orders made by a specific customer
	public static List<OrderHeader> getCustomerOrders(String idCustomer) {
		return OrderHeader.getCustomerOrders(idCustomer);
	}
	
    // Get order header by order ID
	public static OrderHeader getOrderHeader(String idOrder) {
		return OrderHeader.getOrderHeader(idOrder);
	}
	
    // Handle checkout process for a customer
	public static String checkout(Customer customer, String promoCode) {
		String idCustomer = customer.getIdUser();
		
        // Get all items from customer's cart
		List<CartItem> cartItems = CartItem.getCartItems(idCustomer);
		
		if (cartItems.isEmpty()) {
			return "Cart is empty.";
		}
		
        // Calculate total price
		double total = 0;
		for (CartItem cartItem : cartItems) {
			Product product = Product.getProduct(cartItem.getIdProduct());
			if (product == null) {
				return "Product " + cartItem.getIdProduct() + " not found.";
			}
			
            // Check product stock before checkout
			if (product.getStock() < cartItem.getCount()) {
				return "Insufficient stock for " + product.getName() + ". Available: " + product.getStock() + ".";
			}
			
			total += product.getPrice() * cartItem.getCount();
		}
		
        // Apply promo code if provided
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
		
        // Check customer balance
		if (customer.getBalance() < total) {
			return "Insufficient balance. Required: Rp" 
                + String.format("%,d", (int)total) 
                + ", Current: Rp" 
                + String.format("%,d", (int)customer.getBalance()) + ".";
		}
		
        // Create order header
		boolean orderCreated = OrderHeader.createOrderHeader(idCustomer, idPromo, total);
		if (!orderCreated) {
			return "Failed to create order.";
		}
		
        // Get newly created order
		List<OrderHeader> orders = OrderHeader.getCustomerOrders(idCustomer);
		if (orders.isEmpty()) {
			return "Order not found after creation.";
		}
		OrderHeader order = orders.get(0);
		
        // Create order details and update product stock
		for (CartItem cartItem : cartItems) {
			Product product = Product.getProduct(cartItem.getIdProduct());
			
			OrderDetail.createOrderDetail(
                order.getIdOrder(),
                cartItem.getIdProduct(),
                cartItem.getCount()
            );
			
			int newStock = product.getStock() - cartItem.getCount();
			product.editProductStock(newStock);
		}
		
        // Update customer balance after checkout
		double newBalance = customer.getBalance() - total;
		Customer.updateBalance(idCustomer, newBalance);
		customer.setBalance(newBalance);
		
        // Clear cart after successful checkout
		CartItem.clearCart(idCustomer);
		
		System.out.println("Order created successfully. Order ID: " + order.getIdOrder() + ".");
		System.out.println("Total: Rp" + String.format("%,d", (int)total));
		System.out.println("New balance: Rp" + String.format("%,d", (int)newBalance));
		
		return null;
	}
	
    // Get all order details for a specific order
	public static List<OrderDetail> getOrderDetails(String idOrder) {
		return OrderDetail.getOrderDetails(idOrder);
	}

}
