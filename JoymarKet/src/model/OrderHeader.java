package model;

import java.sql.Timestamp;
import java.util.List;

import database.OrderHeaderDA;

// Model class that represents order header information
public class OrderHeader {

	private String idOrder;
	private String idCustomer;
	private String idPromo;
	private String status;
	private Timestamp orderedAt;
	private double totalAmount;
	
    // Constructor to initialize order header data
	public OrderHeader(String idOrder, String idCustomer, String idPromo, String status, Timestamp orderedAt,
			double totalAmount) {
		this.idOrder = idOrder;
		this.idCustomer = idCustomer;
		this.idPromo = idPromo;
		this.status = status;
		this.orderedAt = orderedAt;
		this.totalAmount = totalAmount;
	}

    // Get order ID
	public String getIdOrder() {
		return idOrder;
	}

    // Set order ID
	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}

    // Get customer ID
	public String getIdCustomer() {
		return idCustomer;
	}

    // Set customer ID
	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

    // Get promo ID
	public String getIdPromo() {
		return idPromo;
	}

    // Set promo ID
	public void setIdPromo(String idPromo) {
		this.idPromo = idPromo;
	}

    // Get order status
	public String getStatus() {
		return status;
	}

    // Set order status
	public void setStatus(String status) {
		this.status = status;
	}

    // Get order timestamp
	public Timestamp getOrderedAt() {
		return orderedAt;
	}

    // Set order timestamp
	public void setOrderedAt(Timestamp orderedAt) {
		this.orderedAt = orderedAt;
	}

    // Get total order amount
	public double getTotalAmount() {
		return totalAmount;
	}

    // Set total order amount
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
    // Create new order header
	public static boolean createOrderHeader(String idCustomer, String idPromo, double totalAmount) {
		OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
		String idOrder = orderHeaderDA.generateOrderID();
		
        // Generate order ID
		if (idOrder == null) {
			System.err.println("Failed to generate order ID.");
			return false;
		}
		
        // Set order time
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		OrderHeader orderHeader = new OrderHeader(idOrder, idCustomer, idPromo, "Pending", now, totalAmount);
		
		boolean success = orderHeaderDA.saveOrderHeader(orderHeader);
		if (success) {
			System.out.println("Order created successfully with ID: " + idOrder);
		}
		return success;
	}
	
    // Update order status
	public boolean editOrderHeaderStatus(String newStatus) {
		this.status = newStatus;
		OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
		return orderHeaderDA.updateOrderStatus(this.idOrder, newStatus);
	}
	
    // Get order header by order ID
	public static OrderHeader getOrderHeader(String idOrder) {
		OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
		return orderHeaderDA.getOrderHeader(idOrder);
	}
	
    // Get order header for a specific customer
	public static OrderHeader getCustomerOrderHeader(String idOrder, String idCustomer) {
		OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
		return orderHeaderDA.getCustomerOrderHeader(idOrder, idCustomer);
	}
	
    // Get all orders
	public static List<OrderHeader> getAllOrders() {
	    OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
	    return orderHeaderDA.getAllOrders();
	}
	
    // Get all orders for a customer
	public static List<OrderHeader> getCustomerOrders(String idCustomer) {
	    OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
	    return orderHeaderDA.getCustomerOrders(idCustomer);
	}

}
