package model;

import database.UserDA;

// User model class that represents basic user data
public class User {

	private String idUser;
	private String fullName;
	private String email;
	private String password;
	private String phone;
	private String address;
	private String role;
	
    // Constructor to initialize user data
	public User(String idUser, String fullName, String email, String password, String phone, String address,
			String role) {
		this.idUser = idUser;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.address = address;
		this.role = role;
	}

    // Get user ID
	public String getIdUser() {
		return idUser;
	}

    // Set user ID
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

    // Get full name
	public String getFullName() {
		return fullName;
	}

    // Set full name
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

    // Get email
	public String getEmail() {
		return email;
	}

    // Set email
	public void setEmail(String email) {
		this.email = email;
	}

    // Get password
	public String getPassword() {
		return password;
	}

    // Set password
	public void setPassword(String password) {
		this.password = password;
	}

    // Get phone number
	public String getPhone() {
		return phone;
	}

    // Set phone number
	public void setPhone(String phone) {
		this.phone = phone;
	}

    // Get address
	public String getAddress() {
		return address;
	}

    // Set address
	public void setAddress(String address) {
		this.address = address;
	}

    // Get user role
	public String getRole() {
		return role;
	}

    // Set user role
	public void setRole(String role) {
		this.role = role;
	}
	
    // Get user data by user ID
	public static User getUser(String idUser) {
		UserDA userDA = new UserDA();
		return userDA.getUser(idUser);
	}
	
    // Get user data using email
	public static User getUserByEmail(String email) {
        UserDA userDA = new UserDA();
        return userDA.getUserByEmail(email);
    }
	
    // Update user profile data
	public boolean editProfile(String fullName, String email, String password, String phone, String address) {
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.address = address;
		
		UserDA userDA = new UserDA();
		return userDA.updateUser(this);
	}

}
