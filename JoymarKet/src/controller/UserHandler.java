package controller;

import model.Admin;
import model.Courier;
import model.Customer;
import model.User;

// Controller that handles user authentication and profile actions
public class UserHandler {

	private static User currentUser;
	
    // Edit profile for the currently logged-in user
	public static String editProfile(String fullName, String phone, String address) {

        // User must be logged in
		if (currentUser == null) {
			return "No user logged in.";
		}
		
        // Validate full name
		if (fullName == null || fullName.trim().isEmpty()) {
			return "Full name must be filled.";
		}
		
        // Validate phone number
		if (phone == null || phone.trim().isEmpty()) {
			return "Phone must be filled.";
		}
		if (!isNumeric(phone.trim())) {
			return "Phone must be numeric.";
		}
		if (phone.trim().length() < 10 || phone.trim().length() > 13) {
			return "Phone must be 10-13 digits.";
		}
		
        // Validate address
		if (address == null || address.trim().isEmpty()) {
			return "Address must be filled.";
		}
		
        // Update user profile
		boolean success = currentUser.editProfile(
            fullName.trim(),
            currentUser.getEmail(),
            currentUser.getPassword(),
            phone.trim(),
            address.trim()
        );
		
		if (!success) {
			return "Failed to update profile.";
		}
		
		return null;
	}
	
    // Handle user login
	public static String login(String email, String password) {

        // Validate login input
		if (email == null || email.trim().isEmpty()) {
			return "Email must be filled.";
		}
		
		if (password == null || password.isEmpty()) {
			return "Password must be filled.";
		}
		
        // Get user by email
		User user = User.getUserByEmail(email.trim());
		
		if (user == null) {
			return "Email not found in database";
		}
		
        // Check password
		if (!user.getPassword().equals(password)) {
			return "Invalid password.";
		}
		
        // Set current user based on role
		String role = user.getRole();
		String idUser = user.getIdUser();
		
		if (role.equals("Customer")) {
			currentUser = Customer.getCustomer(idUser);
		} else if (role.equals("Courier")) {
			currentUser = Courier.getCourier(idUser);
		} else if (role.equals("Admin")) {
			currentUser = Admin.getAdmin(idUser);
		} else {
			currentUser = user;
		}
		
		System.out.println("Login successful: " + user.getFullName() + " (" + role + ").");
		return null;
	}
	
    // Register new customer account
	public static String register(String fullName, String email, String password, String confirmPassword, String phone, String address) {

        // Validate full name
		if (fullName == null || fullName.trim().isEmpty()) {
			return "Full name can not be empty.";
		}
		
        // Validate email
		if (email == null || email.trim().isEmpty()) {
			return "Email must be filled.";
		}
		
		if (!email.trim().endsWith("@gmail.com")) {
			return "Email must end with @gmail.com.";
		}
		
		if (User.getUserByEmail(email.trim()) != null) {
			return "Email already registered.";
		}
		
        // Validate password
		if (password == null || password.length() < 6) {
			return "Password must be at least 6 characters.";
		}
		
		if (!password.equals(confirmPassword)) {
			return "Password and confirm password must match.";
		}
		
        // Validate phone
		if (phone == null || phone.trim().isEmpty()) {
			return "Phone must be filled.";
		}
		
		if (!isNumeric(phone.trim())) {
			return "Phone must be numeric.";
		}
		
		if (phone.trim().length() < 10 || phone.trim().length() > 13) {
			return "Phone must be 10-13 digits.";
		}
		
        // Validate address
		if (address == null || address.trim().isEmpty()) {
			return "Address must be filled.";
		}
		
        // Create new customer account
		boolean success = Customer.registerAccount(
            fullName.trim(),
            email.trim(),
            password,
            phone.trim(),
            address.trim()
        );
		
		if (!success) {
			return "Registration failed. Please try again.";
		}
		
		return null;
	}
	
    // Get currently logged-in user
	public static User getUser() {
		return currentUser;
	}
	
    // Logout current user
	public static void logout() {
		currentUser = null;
		System.out.println("User logged out.");
	}
	
    // Helper method to check numeric string (no regex)
	private static boolean isNumeric(String string) {
		if (string == null || string.isEmpty()) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

}
