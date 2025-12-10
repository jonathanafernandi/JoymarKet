package model;

import database.UserDA;

public class User {

	private String idUser;
	private String fullName;
	private String email;
	private String password;
	private String phone;
	private String address;
	private String role;
	
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

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public static User getUser(String idUser) {
		UserDA userDA = new UserDA();
		return userDA.getUser(idUser);
	}
	
	public static User getUserByEmail(String email) {
        UserDA userDA = new UserDA();
        return userDA.getUserByEmail(email);
    }
	
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
