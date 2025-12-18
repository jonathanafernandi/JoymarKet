package model;

import database.AdminDA;

// Admin model class that extends User
public class Admin extends User {

	private String emergencyContact;
	
    // Constructor to initialize admin data
	public Admin(String idUser, String fullName, String email, String password, String phone, String address,
			String role, String emergencyContact) {
		super(idUser, fullName, email, password, phone, address, role);
		this.emergencyContact = emergencyContact;
	}

    // Get emergency contact number
	public String getEmergencyContact() {
		return emergencyContact;
	}

    // Update emergency contact number
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	
    // Get admin data from database by admin ID
	public static Admin getAdmin(String idAdmin) {
		AdminDA adminDA = new AdminDA();
		return adminDA.getAdmin(idAdmin);
	}

}
