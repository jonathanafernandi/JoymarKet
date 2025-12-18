package view;

import controller.UserHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Admin;
import model.Courier;
import model.Customer;
import model.User;

public class UserWindow {

		public static void showLoginWindow(Stage stage) {
	
		// Root layout for the login page
		VBox root = new VBox(20);
		root.setPadding(new Insets(40));
		root.setAlignment(Pos.CENTER);
	
		// Background gradient for the login screen
		root.setStyle("-fx-background-color: linear-gradient(to bottom, #667EEA 0%, #764BA2 100%);");
	
		// Application title
		Label titleLabel = new Label("JoymarKet");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
		titleLabel.setTextFill(Color.WHITE);
	
		// Subtitle below the main title
		Label subtitleLabel = new Label("Grocery Marketplace");
		subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		subtitleLabel.setTextFill(Color.web("#E0E0E0"));
	
		// Container for the login form
		VBox formBox = new VBox(15);
		formBox.setMaxWidth(400);
		formBox.setPadding(new Insets(30));
		formBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
	
		// Login section label
		Label loginLabel = new Label("Login to your account");
		loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
	
		// Email input field
		TextField emailField = new TextField();
		emailField.setPromptText("Email");
		emailField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
	
		// Password input field
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
	
		// Label to show login errors
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setWrapText(true);
		errorLabel.setVisible(false);
	
		// Login button
		Button loginButton = new Button("Login");
		loginButton.setStyle("-fx-background-color: #667EEA; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12px; -fx-background-radius: 5px; -fx-cursor: hand;");
		loginButton.setMaxWidth(Double.MAX_VALUE);
	
		// Button to navigate to registration page
		Button registerButton = new Button("Create New Account");
		registerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #667EEA; -fx-font-size: 13px; -fx-underline: true; -fx-cursor: hand;");
	
		// Handle login process
		loginButton.setOnAction(e -> {
			String email = emailField.getText();
			String password = passwordField.getText();
	
			// Call login logic from UserHandler
			String error = UserHandler.login(email, password);
	
			if (error != null) {
				// Show error message if login fails
				errorLabel.setText(error);
				errorLabel.setVisible(true);
			} else {
				// Get logged-in user
				User currentUser = UserHandler.getUser();
				errorLabel.setVisible(false);
	
				// Redirect user based on their role
				if (currentUser instanceof Customer) {
					CustomerWindow.showCustomerDashboard(stage, (Customer) currentUser);
				} else if (currentUser instanceof Courier) {
					CourierWindow.showCourierDashboard(stage, (Courier) currentUser);
				} else if (currentUser instanceof Admin) {
					AdminWindow.showAdminDashboard(stage, (Admin) currentUser);
				}
			}
		});
	
		// Navigate to register window
		registerButton.setOnAction(e -> showRegisterWindow(stage));
	
		// Add all form components
		formBox.getChildren().addAll(
				loginLabel,
				emailField,
				passwordField,
				errorLabel,
				loginButton,
				registerButton
		);
	
		// Add all main components to root
		root.getChildren().addAll(titleLabel, subtitleLabel, formBox);
	
		// Set scene and display
		Scene scene = new Scene(root, 600, 700);
		stage.setScene(scene);
	}

	
	public static void showRegisterWindow(Stage stage) {
	
		// Root layout for register page
		VBox root = new VBox(20);
		root.setPadding(new Insets(30));
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: linear-gradient(to bottom, #667EEA 0%, #764BA2 100%);");
	
		// Wrapper to center the form
		VBox wrapperBox = new VBox();
		wrapperBox.setAlignment(Pos.CENTER);
		wrapperBox.setPadding(new Insets(20));
	
		// Register form container
		VBox formBox = new VBox(15);
		formBox.setMaxWidth(450);
		formBox.setPadding(new Insets(30));
		formBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
	
		// Register title
		Label titleLabel = new Label("Create New Account");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
	
		// Input fields
		TextField fullNameField = new TextField();
		fullNameField.setPromptText("Full Name");
	
		TextField emailField = new TextField();
		emailField.setPromptText("Email (@gmail.com)");
	
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password (min. 6 characters)");
	
		PasswordField confirmPasswordField = new PasswordField();
		confirmPasswordField.setPromptText("Confirm Password");
	
		TextField phoneField = new TextField();
		phoneField.setPromptText("Phone Number (10-13 digits)");
	
		TextArea addressArea = new TextArea();
		addressArea.setPromptText("Address");
		addressArea.setPrefRowCount(3);
		addressArea.setWrapText(true);
	
		// Error message label
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setWrapText(true);
		errorLabel.setVisible(false);
	
		// Register button
		Button registerButton = new Button("Register");
		registerButton.setStyle("-fx-background-color: #667EEA; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold;");
		registerButton.setMaxWidth(Double.MAX_VALUE);
	
		// Back to login button
		Button backButton = new Button("Back to Login");
		backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #667EEA; -fx-underline: true;");
	
		// Handle registration logic
		registerButton.setOnAction(e -> {
			String error = UserHandler.register(
					fullNameField.getText(),
					emailField.getText(),
					passwordField.getText(),
					confirmPasswordField.getText(),
					phoneField.getText(),
					addressArea.getText()
			);
	
			if (error != null) {
				errorLabel.setText(error);
				errorLabel.setVisible(true);
			} else {
				// Show success message
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Registration successful! Please login.");
				alert.showAndWait();
	
				// Redirect back to login page
				showLoginWindow(stage);
			}
		});
	
		backButton.setOnAction(e -> showLoginWindow(stage));
	
		// Add all components to form
		formBox.getChildren().addAll(
				titleLabel,
				fullNameField,
				emailField,
				passwordField,
				confirmPasswordField,
				phoneField,
				addressArea,
				errorLabel,
				registerButton,
				backButton
		);
	
		wrapperBox.getChildren().add(formBox);
	
		// Make form scrollable
		ScrollPane scrollPane = new ScrollPane(wrapperBox);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
	
		root.getChildren().add(scrollPane);
	
		// Set scene and display
		Scene scene = new Scene(root, 600, 700);
		stage.setScene(scene);
	}

		
		public static void showEditProfileWindow(Stage stage, User user) {
	
		// Root layout for the edit profile page
		VBox root = new VBox(15);
		root.setPadding(new Insets(30));
		root.setAlignment(Pos.TOP_CENTER);
		root.setStyle("-fx-background-color: #F5F5F5;");
	
		// Page title
		Label titleLabel = new Label("Edit Profile");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	
		// Container for the profile form
		VBox formBox = new VBox(15);
		formBox.setMaxWidth(500);
		formBox.setPadding(new Insets(25));
		formBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
	
		// Full name input (pre-filled with current data)
		TextField fullNameField = new TextField(user.getFullName());
		fullNameField.setPromptText("Full Name");
		fullNameField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
	
		// Email is shown but not editable
		Label emailLabel = new Label("Email: " + user.getEmail() + " (can not be changed)");
		emailLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 13px;");
	
		// Phone number input
		TextField phoneField = new TextField(user.getPhone());
		phoneField.setPromptText("Phone Number");
		phoneField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
	
		// Address input area
		TextArea addressArea = new TextArea(user.getAddress());
		addressArea.setPromptText("Address");
		addressArea.setPrefRowCount(4);
		addressArea.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
		addressArea.setWrapText(true);
	
		// Label to show validation or update errors
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setWrapText(true);
		errorLabel.setVisible(false);
	
		// Container for action buttons
		HBox buttonBox = new HBox(15);
		buttonBox.setAlignment(Pos.CENTER);
	
		// Save changes button
		Button saveButton = new Button("Save Changes");
		saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 25px; -fx-background-radius: 5px; -fx-cursor: hand;");
	
		// Cancel button
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 25px; -fx-background-radius: 5px; -fx-cursor: hand;");
	
		// Handle save profile action
		saveButton.setOnAction(e -> {
	
			// Call edit profile logic from UserHandler
			String error = UserHandler.editProfile(
					fullNameField.getText(),
					phoneField.getText(),
					addressArea.getText()
			);
	
			if (error != null) {
				// Show error if validation or update fails
				errorLabel.setText(error);
				errorLabel.setVisible(true);
			} else {
				// Show success message
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Profile updated successfully!");
				alert.showAndWait();
	
				// Reload user data and redirect based on role
				if (user instanceof Customer) {
					Customer updatedCustomer = Customer.getCustomer(user.getIdUser());
					CustomerWindow.showCustomerDashboard(stage, updatedCustomer);
	
				} else if (user instanceof Courier) {
					Courier updatedCourier = Courier.getCourier(user.getIdUser());
					CourierWindow.showCourierDashboard(stage, updatedCourier);
	
				} else if (user instanceof Admin) {
					Admin updatedAdmin = Admin.getAdmin(user.getIdUser());
					AdminWindow.showAdminDashboard(stage, updatedAdmin);
				}
			}
		});
	
		// Handle cancel action (go back without saving)
		cancelButton.setOnAction(e -> {
			if (user instanceof Customer) {
				CustomerWindow.showCustomerDashboard(stage, (Customer) user);
	
			} else if (user instanceof Courier) {
				CourierWindow.showCourierDashboard(stage, (Courier) user);
	
			} else if (user instanceof Admin) {
				AdminWindow.showAdminDashboard(stage, (Admin) user);
			}
		});
	
		// Add buttons to button container
		buttonBox.getChildren().addAll(saveButton, cancelButton);
	
		// Add all form components
		formBox.getChildren().addAll(
				new Label("Full Name:"),
				fullNameField,
				emailLabel,
				new Label("Phone:"),
				phoneField,
				new Label("Address:"),
				addressArea,
				errorLabel,
				buttonBox
		);
	
		// Add title and form to root
		root.getChildren().addAll(titleLabel, formBox);
	
		// Make the page scrollable
		ScrollPane scrollPane = new ScrollPane(root);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color: #F5F5F5;");
	
		// Set scene and display
		Scene scene = new Scene(scrollPane, 650, 650);
		stage.setScene(scene);
	}


}
