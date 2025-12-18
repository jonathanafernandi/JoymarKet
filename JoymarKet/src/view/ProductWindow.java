package view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import controller.CustomerHandler;
import controller.ProductHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import model.Product;

public class ProductWindow {

	public static void showProductsForCustomer(Stage parentStage, Customer customer) {
	
		// Create a new modal window for browsing products
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(parentStage);
		stage.setTitle("Browse Products - JoymarKet");
	
		// Root layout
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
	
		// ================= TOP BAR =================
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #667EEA;");
	
		// Page title
		Label titleLabel = new Label("Browse Products");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		titleLabel.setTextFill(Color.WHITE);
	
		// Spacer to push balance label to the right
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
	
		// Display customer balance
		Label balanceLabel =
			new Label("Rp" + formatCurrency(customer.getBalance()));
		balanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		balanceLabel.setTextFill(Color.web("#FFD700"));
	
		topBar.getChildren().addAll(titleLabel, spacer, balanceLabel);
		root.setTop(topBar);
	
		// ================= CENTER CONTENT =================
		VBox centerBox = new VBox(15);
		centerBox.setPadding(new Insets(20));
	
		// -------- FILTER SECTION --------
		HBox filterBox = new HBox(15);
		filterBox.setAlignment(Pos.CENTER_LEFT);
	
		Label filterLabel = new Label("Filter by Category:");
		filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	
		// Category selection combo box
		ComboBox<String> categoryCombo = new ComboBox<>();
		categoryCombo.getItems().addAll(
			"All", "Dairy", "Meat", "Fruits", "Vegetables", "Seafood"
		);
		categoryCombo.setValue("All");
		categoryCombo.setStyle("-fx-font-size: 13px;");
	
		// Refresh button to reload products
		Button refreshButton = new Button("Refresh");
		refreshButton.setStyle(
			"-fx-background-color: #2196F3; " +
			"-fx-text-fill: #FFFFFF; " +
			"-fx-font-size: 13px; " +
			"-fx-padding: 8px 15px; " +
			"-fx-cursor: hand;"
		);
	
		filterBox.getChildren().addAll(
			filterLabel, categoryCombo, refreshButton
		);
	
		// -------- PRODUCT LIST --------
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color: transparent;");
		scrollPane.setPrefHeight(400);
	
		VBox productList = new VBox(12);
		productList.setPadding(new Insets(10));
	
		// Runnable to load and filter products dynamically
		Runnable loadProducts = () -> {
	
			productList.getChildren().clear();
			List<Product> products =
				ProductHandler.getAvailableProducts();
	
			String selectedCategory = categoryCombo.getValue();
	
			for (Product product : products) {
				if (selectedCategory.equals("All") ||
					product.getCategory().equals(selectedCategory)) {
	
					HBox productCard =
						createProductCard(product, customer, stage);
					productList.getChildren().add(productCard);
				}
			}
	
			// Show message if no products match filter
			if (productList.getChildren().isEmpty()) {
				Label emptyLabel =
					new Label("No products available in this category.");
				emptyLabel.setFont(Font.font("Arial", 14));
				emptyLabel.setTextFill(Color.GRAY);
				productList.getChildren().add(emptyLabel);
			}
		};
	
		// Initial load
		loadProducts.run();
	
		// Reload products when filter changes
		categoryCombo.setOnAction(e -> loadProducts.run());
		refreshButton.setOnAction(e -> loadProducts.run());
	
		scrollPane.setContent(productList);
	
		// ================= BOTTOM BUTTONS =================
		HBox buttonBox = new HBox(15);
		buttonBox.setPadding(new Insets(15));
		buttonBox.setAlignment(Pos.CENTER);
	
		Button viewCartButton = new Button("View Cart");
		viewCartButton.setStyle(
			"-fx-background-color: #FF9800; " +
			"-fx-text-fill: #FFFFFF; " +
			"-fx-font-size: 14px; " +
			"-fx-font-weight: bold; " +
			"-fx-padding: 10px 25px; " +
			"-fx-cursor: hand;"
		);
	
		Button closeButton = new Button("Close");
		closeButton.setStyle(
			"-fx-background-color: #607D8B; " +
			"-fx-text-fill: #FFFFFF; " +
			"-fx-font-size: 14px; " +
			"-fx-padding: 10px 25px; " +
			"-fx-cursor: hand;"
		);
	
		// Navigate to cart window
		viewCartButton.setOnAction(e -> {
			stage.close();
			CartItemWindow.showCart(parentStage, customer);
		});
	
		// Close product window
		closeButton.setOnAction(e -> stage.close());
	
		buttonBox.getChildren().addAll(viewCartButton, closeButton);
	
		centerBox.getChildren().addAll(filterBox, scrollPane);
		root.setCenter(centerBox);
		root.setBottom(buttonBox);
	
		Scene scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void showProductsForAdmin(Stage parentStage) {
		// Create admin product management window
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(parentStage);
		stage.setTitle("Manage Products - JoymarKet Admin");
	
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
	
		// ================= TOP BAR =================
		HBox topBar = new HBox();
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER);
		topBar.setStyle("-fx-background-color: #2196F3;");
	
		Label titleLabel = new Label("Manage Products");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		titleLabel.setTextFill(Color.WHITE);
	
		topBar.getChildren().add(titleLabel);
		root.setTop(topBar);
	
		// ================= PRODUCT LIST =================
		VBox centerBox = new VBox(15);
		centerBox.setPadding(new Insets(20));
	
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefHeight(450);
	
		VBox productList = new VBox(12);
		productList.setPadding(new Insets(10));
	
		// Load all products for admin
		List<Product> products = ProductHandler.getAllProducts();
	
		for (Product product : products) {
			VBox productCard =
				createProductCardForAdmin(product, productList);
			productList.getChildren().add(productCard);
		}
	
		scrollPane.setContent(productList);
	
		// Close button
		Button closeButton = new Button("Close");
		closeButton.setStyle(
			"-fx-background-color: #607D8B; " +
			"-fx-text-fill: #FFFFFF; " +
			"-fx-font-size: 14px; " +
			"-fx-padding: 10px 25px; " +
			"-fx-cursor: hand;"
		);
		closeButton.setOnAction(e -> stage.close());
	
		centerBox.getChildren().addAll(scrollPane, closeButton);
		root.setCenter(centerBox);
	
		Scene scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		stage.show();
	}
	
	private static HBox createProductCard(Product product, Customer customer, Stage stage) {
	
		// Main horizontal container for a single product card
		HBox card = new HBox(20);
		card.setPadding(new Insets(15));
		card.setAlignment(Pos.CENTER_LEFT);
		card.setStyle(
			"-fx-background-color: #FFFFFF; " +
			"-fx-background-radius: 8; " +
			"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
		);
	
		// ================= PRODUCT INFO SECTION =================
		VBox infoBox = new VBox(6);
	
		// Product name
		Label nameLabel = new Label(product.getName());
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
	
		// Product price
		Label priceLabel =
			new Label("Rp" + formatCurrency(product.getPrice()));
		priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		priceLabel.setTextFill(Color.web("#2E7D32"));
	
		// Product stock information
		Label stockLabel =
			new Label("Stock: " + product.getStock());
		stockLabel.setFont(Font.font("Arial", 12));
		stockLabel.setTextFill(Color.web("#666666"));
	
		// Product category
		Label categoryLabel = new Label(product.getCategory());
		categoryLabel.setFont(Font.font("Arial", 11));
		categoryLabel.setTextFill(Color.web("#999999"));
	
		infoBox.getChildren().addAll(
			nameLabel, priceLabel, stockLabel, categoryLabel
		);
	
		// Spacer to push action section to the right
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
	
		// ================= ACTION SECTION =================
		VBox actionBox = new VBox(8);
		actionBox.setAlignment(Pos.CENTER);
	
		Label quantityLabel = new Label("Quantity:");
		quantityLabel.setFont(Font.font("Arial", 11));
	
		// Spinner for selecting purchase quantity
		Spinner<Integer> quantitySpinner =
			new Spinner<>(1, product.getStock(), 1);
		quantitySpinner.setPrefWidth(80);
		quantitySpinner.setEditable(true);
	
		// Add-to-cart button
		Button addToCartButton = new Button("Add to Cart");
		addToCartButton.setStyle(
			"-fx-background-color: #4CAF50; " +
			"-fx-text-fill: #FFFFFF; " +
			"-fx-font-size: 12px; " +
			"-fx-padding: 8px 15px; " +
			"-fx-cursor: hand; " +
			"-fx-background-radius: 5;"
		);
	
		// Handle add-to-cart action
		addToCartButton.setOnAction(e -> {
	
			int quantity = quantitySpinner.getValue();
			String error =
				CustomerHandler.addToCart(
					customer.getIdUser(),
					product.getIdProduct(),
					quantity
				);
	
			if (error != null) {
				showAlert(Alert.AlertType.ERROR, "Error", error);
			} else {
				showAlert(
					Alert.AlertType.INFORMATION,
					"Success",
					product.getName() + " x " + quantity + " added to cart!"
				);
	
				// Reset quantity after successful addition
				quantitySpinner.getValueFactory().setValue(1);
			}
		});
	
		actionBox.getChildren().addAll(
			quantityLabel, quantitySpinner, addToCartButton
		);
	
		// Assemble product card
		card.getChildren().addAll(infoBox, spacer, actionBox);
		return card;
	}
	
	private static VBox createProductCardForAdmin(Product product, VBox parentList) {
		// Main vertical container for admin product card
		VBox card = new VBox(12);
		card.setPadding(new Insets(15));
		card.setStyle(
			"-fx-background-color: #FFFFFF; " +
			"-fx-background-radius: 8; " +
			"-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
		);
	
		// ================= TOP ROW =================
		HBox topRow = new HBox(15);
		topRow.setAlignment(Pos.CENTER_LEFT);
	
		// Product ID
		Label idLabel =
			new Label("ID: " + product.getIdProduct());
		idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		idLabel.setTextFill(Color.web("#666666"));
	
		// Product name
		Label nameLabel = new Label(product.getName());
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	
		topRow.getChildren().addAll(
			idLabel, new Label("|"), nameLabel
		);
	
		// ================= DETAILS ROW =================
		HBox detailsRow = new HBox(30);
		detailsRow.setAlignment(Pos.CENTER_LEFT);
	
		// Product price
		Label priceLabel =
			new Label("Rp" + formatCurrency(product.getPrice()));
		priceLabel.setFont(Font.font("Arial", 14));
	
		// Product stock with color indicator
		Label stockLabel =
			new Label("Stock: " + product.getStock());
		stockLabel.setFont(Font.font("Arial", 14));
		stockLabel.setTextFill(
			product.getStock() > 0
				? Color.web("#4CAF50")
				: Color.web("#F44336")
		);
	
		// Product category
		Label categoryLabel = new Label(product.getCategory());
		categoryLabel.setFont(Font.font("Arial", 13));
		categoryLabel.setTextFill(Color.web("#666666"));
	
		detailsRow.getChildren().addAll(
			priceLabel, stockLabel, categoryLabel
		);
	
		Separator separator = new Separator();
	
		// ================= ACTION ROW =================
		HBox actionRow = new HBox(15);
		actionRow.setAlignment(Pos.CENTER_LEFT);
	
		Label updateLabel = new Label("Update Stock:");
		updateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
	
		// Text field for new stock input
		TextField stockfField =
			new TextField(String.valueOf(product.getStock()));
		stockfField.setPrefWidth(100);
		stockfField.setPromptText("New stock");
	
		// Update stock button
		Button updateButton = new Button("Update");
		updateButton.setStyle(
			"-fx-background-color: #2196F3; " +
			"-fx-text-fill: #FFFFFF; " +
			"-fx-font-size: 13px; " +
			"-fx-padding: 8px 20px; " +
			"-fx-cursor: hand;"
		);
	
		Label statusLabel = new Label();
		statusLabel.setFont(Font.font("Arial", 11));
	
		// Handle stock update logic
		updateButton.setOnAction(e -> {
			try {
				int newStock =
					Integer.parseInt(stockfField.getText());
	
				if (newStock < 0) {
					statusLabel.setText("Stock can not be negative.");
					statusLabel.setTextFill(Color.RED);
					return;
				}
	
				boolean success =
					product.editProductStock(newStock);
	
				if (success) {
					statusLabel.setText("Stock updated successfully!");
					statusLabel.setTextFill(Color.GREEN);
	
					stockLabel.setText("Stock: " + newStock);
					stockLabel.setTextFill(
						newStock > 0
							? Color.web("#4CAF50")
							: Color.web("F44336")
					);
				} else {
					statusLabel.setText("Failed to update stock.");
					statusLabel.setTextFill(Color.RED);
				}
			} catch (NumberFormatException ex) {
				statusLabel.setText("Invalid stock value.");
				statusLabel.setTextFill(Color.RED);
			}
		});
	
		actionRow.getChildren().addAll(
			updateLabel, stockfField, updateButton, statusLabel
		);
	
		// Assemble admin product card
		card.getChildren().addAll(
			topRow, detailsRow, separator, actionRow
		);
	
		return card;
	}

		
	private static void showAlert(Alert.AlertType type, String title, String message) {
	
		// Create a new alert dialog with the specified alert type
		Alert alert = new Alert(type);
	
		// Set the title of the alert window
		alert.setTitle(title);
	
		// Remove header text to keep the dialog simple
		alert.setHeaderText(null);
	
		// Set the main message shown to the user
		alert.setContentText(message);
	
		// Display the alert and wait until the user closes it
		alert.showAndWait();
	}
	
	private static String formatCurrency(double amount) {
	
		// Format a number into Indonesian currency style (e.g., 1.000.000)
		return NumberFormat
				.getNumberInstance(new Locale("id", "ID"))
				.format(amount);
	}

}
