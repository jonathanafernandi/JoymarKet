package model;

import java.sql.Timestamp;
import java.util.List;

import database.OrderHeaderDA;

public class OrderHeader {

	private String idOrder;
	private String idCustomer;
	private String idPromo;
	private String status;
	private Timestamp orderedAt;
	private double totalAmount;
	
	public OrderHeader(String idOrder, String idCustomer, String idPromo, String status, Timestamp orderedAt,
			double totalAmount) {
		this.idOrder = idOrder;
		this.idCustomer = idCustomer;
		this.idPromo = idPromo;
		this.status = status;
		this.orderedAt = orderedAt;
		this.totalAmount = totalAmount;
	}

	public String getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}

	public String getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getIdPromo() {
		return idPromo;
	}

	public void setIdPromo(String idPromo) {
		this.idPromo = idPromo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getOrderedAt() {
		return orderedAt;
	}

	public void setOrderedAt(Timestamp orderedAt) {
		this.orderedAt = orderedAt;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public static boolean createOrderHeader(String idCustomer, String idPromo, double totalAmount) {
		OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
		String idOrder = orderHeaderDA.generateOrderID();
		
		if (idOrder == null) {
			System.err.println("Failed to generate order ID.");
			return false;
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		OrderHeader orderHeader = new OrderHeader(idOrder, idCustomer, idPromo, "Pending", now, totalAmount);
		
		boolean success = orderHeaderDA.saveOrderHeader(orderHeader);
		if (success) {
			System.out.println("Order created successfully with ID: " + idOrder);
		}
		return success;
	}
	
	public boolean editOrderHeaderStatus(String newStatus) {
		this.status = newStatus;
		OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
		return orderHeaderDA.updateOrderStatus(this.idOrder, newStatus);
	}
	
	public static OrderHeader getOrderHeader(String idOrder) {
		OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
		return orderHeaderDA.getOrderHeader(idOrder);
	}
	
	public static OrderHeader getCustomerOrderHeader(String idOrder, String idCustomer) {
		OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
		return orderHeaderDA.getCustomerOrderHeader(idOrder, idCustomer);
	}
	
	public static List<OrderHeader> getAllOrders() {
	    OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
	    return orderHeaderDA.getAllOrders();
	}
	
	public static List<OrderHeader> getCustomerOrders(String idCustomer) {
	    OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
	    return orderHeaderDA.getCustomerOrders(idCustomer);
	}

}
