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
		VBox root = new VBox(20);
		root.setPadding(new Insets(40));
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: linear-gradient(to bottom, #667EEA 0%, #764BA2 100%);");
		
		Label titleLabel = new Label("JoymarKet");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
		titleLabel.setTextFill(Color.WHITE);
		
		Label subtitleLabel = new Label("Grocery Marketplace");
		subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		subtitleLabel.setTextFill(Color.web("#E0E0E0"));
		
		VBox formBox = new VBox(15);
		formBox.setMaxWidth(400);
		formBox.setPadding(new Insets(30));
		formBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
		
		Label loginLabel = new Label("Login to your account");
		loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		
		TextField emailField = new TextField();
		emailField.setPromptText("Email");
		emailField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
		
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
		
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setWrapText(true);
		errorLabel.setVisible(false);
		
		Button loginButton = new Button("Login");
		loginButton.setStyle("-fx-background-color: #667EEA; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12px; -fx-background-radius: 5px; -fx-cursor: hand;");
		loginButton.setMaxWidth(Double.MAX_VALUE);
		
		Button registerButton = new Button("Create New Account");
		registerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #667EEA; -fx-font-size: 13px; -fx-underline: true; -fx-cursor: hand;");
		
		loginButton.setOnAction(e -> {
			String email = emailField.getText();
			String password = passwordField.getText();
			
			String error = UserHandler.login(email, password);
			if (error != null) {
				errorLabel.setText(error);
				errorLabel.setVisible(true);
			} else {
				User currentUser = UserHandler.getUser();
				errorLabel.setVisible(false);
				
				if (currentUser instanceof Customer) {
					CustomerWindow.showCustomerDashboard(stage, (Customer) currentUser);
				} else if (currentUser instanceof Courier) {
					CourierWindow.showCourierDashboard(stage, (Courier) currentUser);
				} else if (currentUser instanceof Admin) {
					AdminWindow.showAdminDashboard(stage, (Admin) currentUser);
				}
			}
		});
		
		registerButton.setOnAction(e -> showRegisterWindow(stage));
		
		formBox.getChildren().addAll(loginLabel, emailField, passwordField, errorLabel, loginButton, registerButton);
		
		root.getChildren().addAll(titleLabel, subtitleLabel, formBox);
		
		Scene scene = new Scene(root, 600, 700);
		stage.setScene(scene);
	}
	
	public static void showRegisterWindow(Stage stage) {
		VBox root = new VBox(20);
		root.setPadding(new Insets(30));
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: linear-gradient(to bottom, #667EEA 0%, #764BA2 100%);");
		
		VBox wrapperBox = new VBox();
		wrapperBox.setAlignment(Pos.CENTER);
		wrapperBox.setPadding(new Insets(20));
		
		VBox formBox = new VBox(15);
		formBox.setMaxWidth(450);
		formBox.setPadding(new Insets(30));
		formBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
		
		Label titleLabel = new Label("Create New Account");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		
		TextField fullNameField = new TextField();
		fullNameField.setPromptText("Full Name");
		fullNameField.setStyle("-fx-font-size: 13px; -fx-padding: 10px;");
		
		TextField emailField = new TextField();
		emailField.setPromptText("Email (@gmail.com)");
		emailField.setStyle("-fx-font-size: 13px; -fx-padding: 10px;");
		
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password (min. 6 characters)");
		passwordField.setStyle("-fx-font-size: 13px; -fx-padding: 10px;");
		
		PasswordField confirmPasswordField = new PasswordField();
		confirmPasswordField.setPromptText("Confirm Password");
		confirmPasswordField.setStyle("-fx-font-size: 13px; -fx-padding: 10px;");
		
		TextField phoneField = new TextField();
		phoneField.setPromptText("Phone Number (10-13 digits)");
		phoneField.setStyle("-fx-font-size: 13px; -fx-padding: 10px;");
		
		TextArea addressArea = new TextArea();
		addressArea.setPromptText("Address");
		addressArea.setPrefRowCount(3);
		addressArea.setStyle("-fx-font-size: 13px; -fx-padding: 10px;");
		addressArea.setWrapText(true);
		
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setWrapText(true);
		errorLabel.setVisible(false);
		
		Button registerButton = new Button("Register");
		registerButton.setStyle("-fx-background-color: #667EEA; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12px; -fx-background-radius: 5px; -fx-cursor: hand;");
		registerButton.setMaxWidth(Double.MAX_VALUE);
		
		Button backButton = new Button("Back to Login");
		backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #667EEA; -fx-font-size: 13px; -fx-underline: true; -fx-cursor: hand;");
		
		registerButton.setOnAction(e -> {
			String error = UserHandler.register(fullNameField.getText(), emailField.getText(), passwordField.getText(), confirmPasswordField.getText(), phoneField.getText(), addressArea.getText());
			
			if (error != null) {
				errorLabel.setText(error);
				errorLabel.setVisible(true);
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Registration successful! Please login.");
				alert.showAndWait();
				showLoginWindow(stage);
			}
		});
		
		backButton.setOnAction(e -> showLoginWindow(stage));
		
		formBox.getChildren().addAll(titleLabel, fullNameField, emailField, passwordField, confirmPasswordField, phoneField, addressArea, errorLabel, registerButton, backButton);
		
		wrapperBox.getChildren().add(formBox);
		
		ScrollPane scrollPane = new ScrollPane(wrapperBox);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
		
		root.getChildren().add(scrollPane);
		
		Scene scene = new Scene(root, 600, 700);
		stage.setScene(scene);
	}
	
	public static void showEditProfileWindow(Stage stage, User user) {
		VBox root = new VBox(15);
		root.setPadding(new Insets(30));
		root.setAlignment(Pos.TOP_CENTER);
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		Label titleLabel = new Label("Edit Profile");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		
		VBox formBox = new VBox(15);
		formBox.setMaxWidth(500);
		formBox.setPadding(new Insets(25));
		formBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
		
		TextField fullNameField = new TextField(user.getFullName());
		fullNameField.setPromptText("Full Name");
		fullNameField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
		
		Label emailLabel = new Label("Email: " + user.getEmail() + " (can not be changed)");
		emailLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 13px;");
		
		TextField phoneField = new TextField(user.getPhone());
		phoneField.setPromptText("Phone Number");
		phoneField.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
		
		TextArea addressArea = new TextArea(user.getAddress());
		addressArea.setPromptText("Address");
		addressArea.setPrefRowCount(4);
		addressArea.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
		addressArea.setWrapText(true);
		
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setWrapText(true);
		errorLabel.setVisible(false);
		
		HBox buttonBox = new HBox(15);
		buttonBox.setAlignment(Pos.CENTER);
		
		Button saveButton = new Button("Save Changes");
		saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 25px; -fx-background-radius: 5px; -fx-cursor: hand;");
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 25px; -fx-background-radius: 5px; -fx-cursor: hand;");
		
		saveButton.setOnAction(e -> {
			String error = UserHandler.editProfile(fullNameField.getText(), phoneField.getText(), addressArea.getText());
			
			if (error != null) {
				errorLabel.setText(error);
				errorLabel.setVisible(true);
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Profile updated successfully!");
				alert.showAndWait();
				
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
		
		cancelButton.setOnAction(e -> {
			if (user instanceof Customer) {
				CustomerWindow.showCustomerDashboard(stage, (Customer) user);
			} else if (user instanceof Courier) {
				CourierWindow.showCourierDashboard(stage, (Courier) user);
			} else if (user instanceof Admin) {
				AdminWindow.showAdminDashboard(stage, (Admin) user);
			}
		});
		
		buttonBox.getChildren().addAll(saveButton, cancelButton);
		
		formBox.getChildren().addAll(new Label("Full Name:"), fullNameField, emailLabel, new Label("Phone:"), phoneField, new Label("Address:"), addressArea, errorLabel, buttonBox);
		
		root.getChildren().addAll(titleLabel, formBox);
		
		ScrollPane scrollPane = new ScrollPane(root);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color: #F5F5F5;");
		
		Scene scene = new Scene(scrollPane, 650, 650);
		stage.setScene(scene);
	}

}
