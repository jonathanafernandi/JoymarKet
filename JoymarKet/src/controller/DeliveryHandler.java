package controller;

import java.util.List;

import model.Delivery;
import model.OrderHeader;

// Controller that handles delivery and courier assignment logic
public class DeliveryHandler {
	
    // Get all delivery records
	public static List<Delivery> getAllDeliveries() {
		return Delivery.getAllDeliveries();
	}
	
    // Get a specific delivery by order and courier
	public static Delivery getDelivery(String idOrder, String idCourier) {
		return Delivery.getDelivery(idOrder, idCourier);
	}
	
    // Assign a courier to an order
	public static String assignCourierToOrder(String idOrder, String idCourier) {

        // Courier must be selected
		if (idCourier == null || idCourier.trim().isEmpty()) {
			return "Courier must be selected.";
		}
		
        // Check if order exists
		OrderHeader order = OrderHeader.getOrderHeader(idOrder);
		if (order == null) {
			return "Order not found.";
		}
		
        // Prevent duplicate delivery assignment
		Delivery existingDelivery = Delivery.getDelivery(idOrder, idCourier);
		if (existingDelivery != null) {
			return "Delivery already assigned to this courier.";
		}
		
        // Create delivery record
		boolean success = Delivery.createDelivery(idOrder, idCourier);
		if (!success) {
			return "Failed to assign order to courier.";
		}
		
		System.out.println("Order assigned to courier: " + idOrder + " -> " + idCourier + ".");
		return null;
	}
	
    // Update delivery status by courier
	public static String editDeliveryStatus(String idOrder, String idCourier, String newStatus) {

        // Check if delivery exists
		Delivery delivery = Delivery.getDelivery(idOrder, idCourier);
		if (delivery == null) {
			return "Delivery not found or not assigned to you.";
		}
		
        // Status must be selected
		if (newStatus == null || newStatus.trim().isEmpty()) {
			return "Status must be selected.";
		}
		
        // Validate allowed status values
		if (!newStatus.equals("Pending") &&
            !newStatus.equals("In Progress") &&
            !newStatus.equals("Delivered")) {
			return "Invalid status. Use: Pending, In Progress, or Delivered.";
		}
		
        // Update delivery status
		boolean success = delivery.editDeliveryStatus(newStatus);
		if (!success) {
			return "Failed to update delivery status.";
		}
		
        // Update order status when delivery is completed
		if (newStatus.equals("Delivered")) {
			OrderHeader order = OrderHeader.getOrderHeader(idOrder);
			if (order != null) {
				order.editOrderHeaderStatus("Delivered");
			}
		}
		
		System.out.println("Delivery status updated: " + idOrder + " -> " + newStatus + ".");
		return null;
	}
	
    // Get all deliveries assigned to a courier
	public static List<Delivery> getCourierDeliveries(String idCourier) {
		return Delivery.getCourierDeliveries(idCourier);
	}

}
