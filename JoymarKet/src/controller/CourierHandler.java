package controller;

import java.util.List;

import model.Courier;
import model.Delivery;

public class CourierHandler {
	
	public static List<Delivery> getCourierDeliveries(String idCourier) {
		return Delivery.getCourierDeliveries(idCourier);
	}
	
	public static Courier getCourier(String idCourier) {
	    return Courier.getCourier(idCourier);
	}
	
	public static List<Courier> getAllCouriers() {
		return Courier.getAllCouriers();
	}

}
