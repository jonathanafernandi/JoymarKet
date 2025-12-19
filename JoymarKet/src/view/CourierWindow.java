package view;

import java.util.List;

import controller.CourierHandler;
import controller.DeliveryHandler;
import controller.UserHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Courier;
import model.Delivery;

// Courier dashboard and delivery management window
public class CourierWindow {
 
	// Show main courier dashboard
	public static void showCourierDashboard(Stage stage, Courier courier) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		// Top bar with courier info
		HBox topBar = createTopBar(courier);
		root.setTop(topBar);
		
		VBox centerBox = new VBox(25);
		centerBox.setPadding(new Insets(40));
		centerBox.setAlignment(Pos.CENTER);
		
		Label welcomeLabel = new Label("Welcome, " + courier.getFullName() + "!");
		welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		
		Label vehicleLabel = new Label(courier.getVehicleType() + " - " + courier.getVehiclePlate());
		vehicleLabel.setFont(Font.font("Arial", 16));
		vehicleLabel.setTextFill(Color.web("#555555"));
		
		// Menu buttons
		VBox menuBox = new VBox(20);
		menuBox.setAlignment(Pos.CENTER);
		
		Button viewDeliveriesButton = createMenuButton("My Deliveries", "#FF9800");
		Button editProfileButton = createMenuButton("Edit Profile", "#607D8B");
		Button logoutButton = createMenuButton("Logout", "#F44336");
		
		// Button actions
		viewDeliveriesButton.setOnAction(e -> showDeliveries(stage, courier));
		editProfileButton.setOnAction(e -> UserWindow.showEditProfileWindow(stage, courier));
		logoutButton.setOnAction(e -> {
			UserHandler.logout();
			UserWindow.showLoginWindow(stage);
		});
		
		menuBox.getChildren().addAll(viewDeliveriesButton, editProfileButton, logoutButton);
		centerBox.getChildren().addAll(welcomeLabel, vehicleLabel, menuBox);
		root.setCenter(centerBox);
		
		stage.setScene(new Scene(root, 900, 650));
	}
	
	// Show list of deliveries assigned to courier
	private static void showDeliveries(Stage stage, Courier courier) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5");
		
		root.setTop(createTopBar(courier));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("My Deliveries");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		// Load deliveries for courier
		List<Delivery> deliveries = CourierHandler.getCourierDeliveries(courier.getIdUser());
		
		// If no deliveries yet
		if (deliveries.isEmpty()) {
			Label emptyLabel = new Label("No deliveries assigned yet.");
			emptyLabel.setFont(Font.font("Arial", 16));
			emptyLabel.setTextFill(Color.GRAY);
			centerBox.getChildren().addAll(titleLabel, emptyLabel);
		} 
		// If deliveries exist
		else {
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			
			VBox deliveryList = new VBox(15);
			deliveryList.setPadding(new Insets(10));
			
			for (Delivery delivery : deliveries) {
				VBox deliveryCard = createDeliveryCard(delivery, stage, courier);
				deliveryList.getChildren().add(deliveryCard);
			}
			
			scrollPane.setContent(deliveryList);
			centerBox.getChildren().addAll(titleLabel, scrollPane);
		}
		
		// Back button
		Button backButton = new Button("Back to Dashboard");
		backButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-cursor: hand;");
		
		backButton.setOnAction(e -> showCourierDashboard(stage, courier));
		
		centerBox.getChildren().add(backButton);
		root.setCenter(centerBox);
		
		stage.setScene(new Scene(root, 900, 650));
	}
	
	// Create top navigation bar for courier
	private static HBox createTopBar(Courier courier) {
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #FF9800;");
		
		Label logoLabel = new Label("JoymarKet Courier");
		logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		logoLabel.setTextFill(Color.WHITE);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label userLabel = new Label(courier.getFullName());
		userLabel.setFont(Font.font("Arial", 14));
		userLabel.setTextFill(Color.WHITE);
		
		topBar.getChildren().addAll(logoLabel, spacer, userLabel);
		return topBar;
	}
	
	// Helper method to create menu buttons
	private static Button createMenuButton(String text, String color) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 20px 40px; -fx-background-radius: 8; -fx-cursor: hand;");
		button.setPrefWidth(250);
		button.setPrefHeight(80);
		return button;
	}
	
	// Create delivery card with status update option
	private static VBox createDeliveryCard(Delivery delivery, Stage stage, Courier courier) {
		VBox card = new VBox(12);
		card.setPadding(new Insets(20));
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8;");
		
		Label orderLabel = new Label("Order ID: " + delivery.getIdOrder());
		orderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label statusLabel = new Label("Status: " + delivery.getStatus());
		statusLabel.setFont(Font.font("Arial", 14));
		statusLabel.setTextFill(getStatusColor(delivery.getStatus()));
		
		HBox actionBox = new HBox(15);
		actionBox.setAlignment(Pos.CENTER_LEFT);
		
		ComboBox<String> statusCombo = new ComboBox<>();
		statusCombo.getItems().addAll("Pending", "In Progress", "Delivered");
		statusCombo.setValue(delivery.getStatus());
		statusCombo.setStyle("-fx-font-size: 13px;");
		
		Button updateButton = new Button("Update Status");
		updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 8px 15px; -fx-cursor: hand;");
		
		updateButton.setOnAction(e -> {
			String newStatus = statusCombo.getValue();
			String error = DeliveryHandler.editDeliveryStatus(delivery.getIdOrder(), courier.getIdUser(), newStatus);
			
			if (error != null) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setContentText(error);
				alert.showAndWait();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setContentText("Delivery status updated!");
				alert.showAndWait();
				showDeliveries(stage, courier);
			}
		});
		
		actionBox.getChildren().addAll(new Label("Updated to:"), statusCombo, updateButton);
		
		card.getChildren().addAll(orderLabel, statusLabel, new Separator(), actionBox);
		return card;
	}
	
	// Get color based on delivery status
	private static Color getStatusColor(String status) {
		switch (status) {
			case "Pending":
				return Color.ORANGE;
			case "In Progress":
				return Color.ORANGE;
			case "Delivered":
				return Color.GREEN;
			default:
				return Color.GRAY;
		}
	}

}
