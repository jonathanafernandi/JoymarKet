package view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import controller.CourierHandler;
import controller.DeliveryHandler;
import controller.OrderHeaderHandler;
import controller.ProductHandler;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Admin;
import model.Courier;
import model.OrderHeader;
import model.Product;

// Admin main window (dashboard and admin features)
public class AdminWindow {

	// Show admin dashboard menu
	public static void showAdminDashboard(Stage stage, Admin admin) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5");
		
		// Top bar with admin name
		HBox topBar = createTopBar(admin);
		root.setTop(topBar);
		
		// Center container
		VBox centerBox = new VBox(25);
		centerBox.setPadding(new Insets(40));
		centerBox.setAlignment(Pos.CENTER);
		
		Label welcomeLabel = new Label("Admin Dashboard");
		welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		
		Label nameLabel = new Label(admin.getFullName());
		nameLabel.setFont(Font.font("Arial", 16));
		nameLabel.setTextFill(Color.web("#555555"));
		
		// Menu buttons container
		VBox menuBox = new VBox(20);
		menuBox.setAlignment(Pos.CENTER);
		
		Button viewOrdersButton = createMenuButton("View All Orders", "#2196F3");
		Button manageProductsButton = createMenuButton("Manage Products", "#4CAF50");
		Button assignCourierButton = createMenuButton("Assign Courier", "#FF9800");
		Button editProfileButton = createMenuButton("Edit Profile", "#607D8B");
		Button logoutButton = createMenuButton("Logout", "#F44336");
		
		// Button actions
		viewOrdersButton.setOnAction(e -> showAllOrders(stage, admin));
		manageProductsButton.setOnAction(e -> showManageProducts(stage, admin));
		assignCourierButton.setOnAction(e -> showAssignCourier(stage, admin));
		editProfileButton.setOnAction(e -> UserWindow.showEditProfileWindow(stage, admin));
		logoutButton.setOnAction(e -> {
			UserHandler.logout();
			UserWindow.showLoginWindow(stage);
		});
		
		menuBox.getChildren().addAll(
			viewOrdersButton,
			manageProductsButton,
			assignCourierButton,
			editProfileButton,
			logoutButton
		);
		
		centerBox.getChildren().addAll(welcomeLabel, nameLabel, menuBox);
		root.setCenter(centerBox);
		
		stage.setScene(new Scene(root, 900, 650));
	}
	
	// Show all orders page
	private static void showAllOrders(Stage stage, Admin admin) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		root.setTop(createTopBar(admin));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("All Orders");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		List<OrderHeader> orders = OrderHeaderHandler.getAllOrders();
		
		// If no orders exist
		if (orders.isEmpty()) {
			Label emptyLabel = new Label("No orders found.");
			emptyLabel.setTextFill(Color.GRAY);
			centerBox.getChildren().addAll(titleLabel, emptyLabel);
		} else {
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			
			VBox orderList = new VBox(15);
			for (OrderHeader order : orders) {
				orderList.getChildren().add(createOrderCardForAdmin(order));
			}
			
			scrollPane.setContent(orderList);
			centerBox.getChildren().addAll(titleLabel, scrollPane);
		}
		
		// Back button
		Button backButton = new Button("Back to Dashboard");
		backButton.setOnAction(e -> showAdminDashboard(stage, admin));
		
		centerBox.getChildren().add(backButton);
		root.setCenter(centerBox);
		
		stage.setScene(new Scene(root, 900, 650));
	}
	
	// Show manage products page
	private static void showManageProducts(Stage stage, Admin admin) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		root.setTop(createTopBar(admin));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("Manage Products");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		List<Product> products = ProductHandler.getAllProducts();
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		
		VBox productList = new VBox(15);
		for (Product product : products) {
			productList.getChildren().add(
				createProductCardForAdmin(product, stage, admin)
			);
		}
		
		scrollPane.setContent(productList);
		
		Button backButton = new Button("Back to Dashboard");
		backButton.setOnAction(e -> showAdminDashboard(stage, admin));
		
		centerBox.getChildren().addAll(titleLabel, scrollPane, backButton);
		root.setCenter(centerBox);
		
		stage.setScene(new Scene(root, 900, 650));
	}
	
	// Show assign courier page
	private static void showAssignCourier(Stage stage, Admin admin) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		root.setTop(createTopBar(admin));
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(30));
		
		Label titleLabel = new Label("Assign Courier to Order");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		List<OrderHeader> orders = OrderHeaderHandler.getAllOrders();
		List<Courier> couriers = CourierHandler.getAllCouriers();
		
		// Form container
		VBox formBox = new VBox(15);
		formBox.setPadding(new Insets(25));
		formBox.setStyle("-fx-background-color: #FFFFFF;");
		
		ComboBox<String> orderCombo = new ComboBox<>();
		for (OrderHeader order : orders) {
			orderCombo.getItems().add(order.getIdOrder() + " - " + order.getStatus());
		}
		
		ComboBox<String> courierCombo = new ComboBox<>();
		for (Courier courier : couriers) {
			courierCombo.getItems().add(courier.getIdUser() + " - " + courier.getFullName());
		}
		
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		errorLabel.setVisible(false);
		
		Button assignButton = new Button("Assign");
		assignButton.setOnAction(e -> {
			if (orderCombo.getValue() == null || courierCombo.getValue() == null) {
				errorLabel.setText("Please select both order and courier.");
				errorLabel.setVisible(true);
				return;
			}
			
			String idOrder = orderCombo.getValue().split(" - ")[0];
			String idCourier = courierCombo.getValue().split(" - ")[0];
			
			String error = DeliveryHandler.assignCourierToOrder(idOrder, idCourier);
			if (error != null) {
				errorLabel.setText(error);
				errorLabel.setVisible(true);
			} else {
				showAlert(Alert.AlertType.INFORMATION, "Success", "Courier assigned successfully!");
				showAdminDashboard(stage, admin);
			}
		});
		
		formBox.getChildren().addAll(orderCombo, courierCombo, errorLabel, assignButton);
		
		Button backButton = new Button("Back to Dashboard");
		backButton.setOnAction(e -> showAdminDashboard(stage, admin));
		
		centerBox.getChildren().addAll(titleLabel, formBox, backButton);
		root.setCenter(centerBox);
		
		stage.setScene(new Scene(root, 900, 650));
	}
	
	// Create top navigation bar
	private static HBox createTopBar(Admin admin) {
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setStyle("-fx-background-color: #2196F3;");
		
		Label logoLabel = new Label("JoymarKet Admin");
		logoLabel.setTextFill(Color.WHITE);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label userLabel = new Label(admin.getFullName());
		userLabel.setTextFill(Color.WHITE);
		
		topBar.getChildren().addAll(logoLabel, spacer, userLabel);
		return topBar;
	}
	
	// Helper method to create menu buttons
	private static Button createMenuButton(String text, String color) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
		button.setPrefWidth(350);
		return button;
	}
	
	// Show alert dialog
	private static void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	// Get color based on order status
	private static Color getStatusColor(String status) {
		switch (status) {
			case "Pending": return Color.ORANGE;
			case "In Progress": return Color.BLUE;
			case "Delivered": return Color.GREEN;
			default: return Color.GRAY;
		}
	}
	
	// Format currency to Indonesian format
	private static String formatCurrency(double amount) {
		return NumberFormat.getNumberInstance(
			new Locale("id", "ID")
		).format(amount);
	}

	
}
