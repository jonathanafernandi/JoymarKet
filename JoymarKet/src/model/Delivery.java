package model;

import java.util.List;

import database.DeliveryDA;

public class Delivery {

	private String idOrder;
	private String idCourier;
	private String status;
	
	public Delivery(String idOrder, String idCourier, String status) {
		this.idOrder = idOrder;
		this.idCourier = idCourier;
		this.status = status;
	}

	public String getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}

	public String getIdCourier() {
		return idCourier;
	}

	public void setIdCourier(String idCourier) {
		this.idCourier = idCourier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public static Delivery getDelivery(String idOrder, String idCourier) {
		DeliveryDA deliveryDA = new DeliveryDA();
		return deliveryDA.getDelivery(idOrder, idCourier);
	}
	
	public boolean editDeliveryStatus(String newStatus) {
		this.status = newStatus;
		DeliveryDA deliveryDA = new DeliveryDA();
		return deliveryDA.updateDeliveryStatus(this.idOrder, this.idCourier, newStatus);
	}
	
	public static boolean createDelivery(String idOrder, String idCourier) {
		Delivery delivery = new Delivery(idOrder, idCourier, "Pending");
		DeliveryDA deliveryDA = new DeliveryDA();
		return deliveryDA.saveDelivery(delivery);
	}
	
	public static List<Delivery> getAllDeliveries() {
	    DeliveryDA deliveryDA = new DeliveryDA();
	    return deliveryDA.getAllDeliveries();
	}
	
	public static List<Delivery> getCourierDeliveries(String idCourier) {
	    DeliveryDA deliveryDA = new DeliveryDA();
	    return deliveryDA.getCourierDeliveries(idCourier);
	}

}
