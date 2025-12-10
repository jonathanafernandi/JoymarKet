package controller;

import java.util.List;

import model.Delivery;
import model.OrderHeader;

public class DeliveryHandler {
	
	public static List<Delivery> getAllDeliveries() {
		return Delivery.getAllDeliveries();
	}
	
	public static Delivery getDelivery(String idOrder, String idCourier) {
		return Delivery.getDelivery(idOrder, idCourier);
	}
	
	public static String assignCourierToOrder(String idOrder, String idCourier) {
		if (idCourier == null || idCourier.trim().isEmpty()) {
			return "Courier must be selected.";
		}
		
		OrderHeader order = OrderHeader.getOrderHeader(idOrder);
		if (order == null) {
			return "Order not found.";
		}
		
		Delivery existingDelivery = Delivery.getDelivery(idOrder, idCourier);
		if (existingDelivery != null) {
			return "Delivery already assigned to this courier.";
		}
		
		boolean success = Delivery.createDelivery(idOrder, idCourier);
		if (!success) {
			return "Failed to assign order to courier.";
		}
		
		System.out.println("Order assigned to courier: " + idOrder + " -> " + idCourier + ".");
		return null;
	}
	
	public static String editDeliveryStatus(String idOrder, String idCourier, String newStatus) {
		Delivery delivery = Delivery.getDelivery(idOrder, idCourier);
		if (delivery == null) {
			return "Delivery not found or not assigned to you.";
		}
		
		if (newStatus == null || newStatus.trim().isEmpty()) {
			return "Status must be selected.";
		}
		
		if (!newStatus.equals("Pending") && !newStatus.equals("In Progress") && !newStatus.equals("Delivered")) {
			return "Invalid status. Use: Pending, In Progress, or Delivered.";
		}
		
		boolean success = delivery.editDeliveryStatus(newStatus);
		if (!success) {
			return "Failed to update delivery status.";
		}
		
		if (newStatus.equals("Delivered")) {
			OrderHeader order = OrderHeader.getOrderHeader(idOrder);
			if (order != null) {
				order.editOrderHeaderStatus("Delivered");
			}
		}
		
		System.out.println("Delivery status updated: " + idOrder + " -> " + newStatus + ".");
		return null;
	}
	
	public static List<Delivery> getCourierDeliveries(String idCourier) {
		return Delivery.getCourierDeliveries(idCourier);
	}

}
