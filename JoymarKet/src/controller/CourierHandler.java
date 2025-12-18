package controller;

import java.util.List;

import model.Courier;
import model.Delivery;

// Controller that handles courier-related data access
public class CourierHandler {
	
    // Get all deliveries assigned to a specific courier
	public static List<Delivery> getCourierDeliveries(String idCourier) {
		return Delivery.getCourierDeliveries(idCourier);
	}
	
    // Get courier data based on courier ID
	public static Courier getCourier(String idCourier) {
	    return Courier.getCourier(idCourier);
	}
	
    // Retrieve all available couriers from the database
	public static List<Courier> getAllCouriers() {
		return Courier.getAllCouriers();
	}

}
