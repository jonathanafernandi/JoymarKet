package model;

import java.util.List;

import database.CourierDA;

public class Courier extends User {

	private String vehicleType;
	private String vehiclePlate;
	
	public Courier(String idUser, String fullName, String email, String password, String phone, String address,
			String role, String vehicleType, String vehiclePlate) {
		super(idUser, fullName, email, password, phone, address, role);
		this.vehicleType = vehicleType;
		this.vehiclePlate = vehiclePlate;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehiclePlate() {
		return vehiclePlate;
	}

	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}
	
	public static Courier getCourier(String idCourier) {
		CourierDA courierDA = new CourierDA();
		return courierDA.getCourier(idCourier);
	}
	
	public static List<Courier> getAllCouriers() {
	    CourierDA courierDA = new CourierDA();
	    return courierDA.getAllCouriers();
	}

}
