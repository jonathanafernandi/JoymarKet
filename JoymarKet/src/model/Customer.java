package model;

import database.CustomerDA;
import database.UserDA;

// Customer model class that extends User
public class Customer extends User {

	private double balance;
	
    // Constructor to initialize customer data
	public Customer(String idUser, String fullName, String email, String password, String phone, String address,
			String role, double balance) {
		super(idUser, fullName, email, password, phone, address, role);
		this.balance = balance;
	}

    // Get customer balance
	public double getBalance() {
		return balance;
	}

    // Set customer balance
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
    // Register new customer account
	public static boolean registerAccount(String fullName, String email, String password, String phone, String address) {
		CustomerDA customerDA = new CustomerDA();
		String idCustomer = customerDA.generateCustomerID();
		
        // Generate unique customer ID
		if (idCustomer == null) {
			System.err.println("Failed to generate customer ID.");
			return false;
		}
		
        // Create user record
		User user = new User(idCustomer, fullName, email, password, phone, address, "Customer");
		UserDA userDA = new UserDA();
		
		if (!userDA.saveUser(user)) {
			System.err.println("Failed to create user record for customer.");
			return false;
		}
		
        // Create customer record with initial balance
		Customer customer = new Customer(idCustomer, fullName, email, password, phone, address, "Customer", 0.0);
		
		if (!customerDA.saveCustomer(customer)) {
			System.err.println("Failed to create customer record.");
			userDA.deleteUser(idCustomer);
			return false;
		}
		
		System.out.println("Customer registered successfully with ID: " + idCustomer + ".");
		return true;
	}
	
    // Get customer data by customer ID
	public static Customer getCustomer(String idCustomer) {
		CustomerDA customerDA = new CustomerDA();
		return customerDA.getCustomer(idCustomer);
	}
	
    // Top up customer balance
	public boolean topUpBalance(double amount) {
		if (amount <= 0) {
			System.err.println("Top-up amount must be positive");
			return false;
		}
		
		this.balance += amount;
		CustomerDA customerDA = new CustomerDA();
		return customerDA.updateBalance(getIdUser(), this.balance);
	}
	
    // Update customer balance directly
	public static boolean updateBalance(String idCustomer, double newBalance) {
        CustomerDA customerDA = new CustomerDA();
        return customerDA.updateBalance(idCustomer, newBalance);
    }

}
