package model;

import java.util.List;

import database.DeliveryDA;

// Delivery model class that represents order delivery information
public class Delivery {

	private String idOrder;
	private String idCourier;
	private String status;
	
    // Constructor to initialize delivery data
	public Delivery(String idOrder, String idCourier, String status) {
		this.idOrder = idOrder;
		this.idCourier = idCourier;
		this.status = status;
	}

    // Get order ID
	public String getIdOrder() {
		return idOrder;
	}

    // Set order ID
	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}

    // Get courier ID
	public String getIdCourier() {
		return idCourier;
	}

    // Set courier ID
	public void setIdCourier(String idCourier) {
		this.idCourier = idCourier;
	}

    // Get delivery status
	public String getStatus() {
		return status;
	}

    // Set delivery status
	public void setStatus(String status) {
		this.status = status;
	}
	
    // Get delivery data by order ID and courier ID
	public static Delivery getDelivery(String idOrder, String idCourier) {
		DeliveryDA deliveryDA = new DeliveryDA();
		return deliveryDA.getDelivery(idOrder, idCourier);
	}
	
    // Update delivery status
	public boolean editDeliveryStatus(String newStatus) {
		this.status = newStatus;
		DeliveryDA deliveryDA = new DeliveryDA();
		return deliveryDA.updateDeliveryStatus(this.idOrder, this.idCourier, newStatus);
	}
	
    // Create new delivery with default status
	public static boolean createDelivery(String idOrder, String idCourier) {
		Delivery delivery = new Delivery(idOrder, idCourier, "Pending");
		DeliveryDA deliveryDA = new DeliveryDA();
		return deliveryDA.saveDelivery(delivery);
	}
	
    // Get all deliveries
	public static List<Delivery> getAllDeliveries() {
	    DeliveryDA deliveryDA = new DeliveryDA();
	    return deliveryDA.getAllDeliveries();
	}
	
    // Get deliveries assigned to a specific courier
	public static List<Delivery> getCourierDeliveries(String idCourier) {
	    DeliveryDA deliveryDA = new DeliveryDA();
	    return deliveryDA.getCourierDeliveries(idCourier);
	}

}
