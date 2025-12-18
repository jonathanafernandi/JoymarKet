package view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import controller.CartItemHandler;
import controller.CustomerHandler;
import controller.ProductHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CartItem;
import model.Customer;
import model.Product;

// Window to display and manage customer's cart items
public class CartItemWindow {

	// Show cart window as modal
	public static void showCart(Stage parentStage, Customer customer) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(parentStage);
		stage.setTitle("My Cart - JoymarKet");
		
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
		// Top bar showing cart title and balance
		HBox topBar = new HBox(20);
		topBar.setPadding(new Insets(15, 30, 15, 30));
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setStyle("-fx-background-color: #FF9800;");
		
		Label titleLabel = new Label("My Shopping Cart");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		titleLabel.setTextFill(Color.WHITE);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Label balanceLabel = new Label("Balance: Rp" + formatCurrency(customer.getBalance()));
		balanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		balanceLabel.setTextFill(Color.web("#FFD700"));
		
		topBar.getChildren().addAll(titleLabel, spacer, balanceLabel);
		root.setTop(topBar);
		
		VBox centerBox = new VBox(20);
		centerBox.setPadding(new Insets(20));
		
		// Load cart items for customer
		List<CartItem> cartItems = CartItemHandler.getCartItems(customer.getIdUser());
		
		// If cart is empty
		if (cartItems.isEmpty()) {
			VBox emptyBox = new VBox(20);
			emptyBox.setAlignment(Pos.CENTER);
			emptyBox.setPadding(new Insets(50));
			
			Label messageLabel = new Label("Your cart is empty");
			messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
			messageLabel.setTextFill(Color.GRAY);
			
			Label subLabel = new Label("Start shopping to add items.");
			subLabel.setFont(Font.font("Arial", 14));
			subLabel.setTextFill(Color.GRAY);
			
			Button browseButton = new Button("Browse Products");
			browseButton.setOnAction(e -> {
				stage.close();
				ProductWindow.showProductsForCustomer(parentStage, customer);
			});
			
			emptyBox.getChildren().addAll(messageLabel, subLabel, browseButton);
			centerBox.getChildren().add(emptyBox);
		} 
		// If cart has items
		else {
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			scrollPane.setPrefHeight(350);
			
			VBox cartList = new VBox(12);
			cartList.setPadding(new Insets(10));
			
			// Used to calculate subtotal
			double[] subtotalRef = {0};
			
			for (CartItem cartItem : cartItems) {
				Product product = ProductHandler.getProduct(cartItem.getIdProduct());
				if (product != null) {
					HBox cartCard = createCartItemCard(cartItem, product, customer, stage, cartList);
					cartList.getChildren().add(cartCard);
					subtotalRef[0] += product.getPrice() * cartItem.getCount();
				}
			}
			
			scrollPane.setContent(cartList);
			
			// Checkout summary box
			VBox checkoutBox = new VBox(15);
			checkoutBox.setPadding(new Insets(20));
			checkoutBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
			
			HBox summaryRow = new HBox(20);
			summaryRow.setAlignment(Pos.CENTER_LEFT);
			
			VBox summaryLeft = new VBox(8);
			Label itemCountLabel = new Label("Total Items: " + cartItems.size());
			Label subtotalLabel = new Label("Subtotal: Rp" + formatCurrency(subtotalRef[0]));
			subtotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
			subtotalLabel.setTextFill(Color.web("#2E7D32"));
			
			summaryLeft.getChildren().addAll(itemCountLabel, subtotalLabel);
			
			Region spacer2 = new Region();
			HBox.setHgrow(spacer2, Priority.ALWAYS);
			
			// Promo input
			VBox promoBox = new VBox(8);
			Label promoLabel = new Label("Promo Code (optional):");
			TextField promoField = new TextField();
			promoField.setPromptText("Enter promo code");
			
			promoBox.getChildren().addAll(promoLabel, promoField);
			
			summaryRow.getChildren().addAll(summaryLeft, spacer2, promoBox);
			
			Separator separator = new Separator();
			
			// Action buttons
			HBox buttonRow = new HBox(15);
			buttonRow.setAlignment(Pos.CENTER);
			
			Button checkoutButton = new Button("Checkout");
			Button continueButton = new Button("Continue Shopping");
			Button clearButton = new Button("Clear Cart");
			
			// Checkout action
			checkoutButton.setOnAction(e -> {
				String error = CustomerHandler.checkout(customer.getIdUser(), promoField.getText().trim());
				
				if (error != null) {
					showAlert(Alert.AlertType.ERROR, "Checkout Failed", error);
				} else {
					showAlert(Alert.AlertType.INFORMATION, "Success", "Order placed successfully!");
					stage.close();
				}
			});
			
			continueButton.setOnAction(e -> {
				stage.close();
				ProductWindow.showProductsForCustomer(parentStage, customer);
			});
			
			// Clear cart confirmation
			clearButton.setOnAction(e -> {
				Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
				confirm.setTitle("Clear Cart");
				confirm.setHeaderText("Are you sure?");
				confirm.setContentText("This will remove all items from your cart.");
				
				confirm.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {
						boolean success = CartItemHandler.clearCart(customer.getIdUser());
						if (success) {
							stage.close();
							showCart(parentStage, customer);
						}
					}
				});
			});
			
			buttonRow.getChildren().addAll(checkoutButton, continueButton, clearButton);
			
			checkoutBox.getChildren().addAll(summaryRow, separator, buttonRow);
			centerBox.getChildren().addAll(scrollPane, checkoutBox);
		}
		
		// Close button
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> stage.close());
		
		HBox bottomBox = new HBox();
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.getChildren().add(closeButton);
		
		root.setCenter(centerBox);
		root.setBottom(bottomBox);
		
		stage.setScene(new Scene(root, 750, 650));
		stage.show();
	}
	
	// Create UI card for each cart item
	private static HBox createCartItemCard(CartItem cartItem, Product product, Customer customer, Stage stage, VBox parentList) {
		HBox card = new HBox(20);
		card.setPadding(new Insets(15));
		card.setAlignment(Pos.CENTER_LEFT);
		card.setStyle("-fx-background-color: #FFFFFF;");
		
		VBox infoBox = new VBox(8);
		
		Label nameLabel = new Label(product.getName());
		Label priceLabel = new Label("Rp" + formatCurrency(product.getPrice()) + " each");
		Label quantityLabel = new Label("Quantity: " + cartItem.getCount());
		Label subtotalLabel = new Label("Total: Rp" + formatCurrency(product.getPrice() * cartItem.getCount()));
		
		infoBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel, subtotalLabel);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Button removeButton = new Button("Remove");
		
		// Remove item from cart
		removeButton.setOnAction(e -> {
			String error = CustomerHandler.removeFromCart(customer.getIdUser(), product.getIdProduct());
			
			if (error != null) {
				showAlert(Alert.AlertType.ERROR, "Error", error);
			} else {
				parentList.getChildren().remove(card);
			}
		});
		
		card.getChildren().addAll(infoBox, spacer, removeButton);
		return card;
	}
	
	// Show alert dialog
	private static void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	// Format number to Indonesian currency format
	private static String formatCurrency(double amount) {
		return NumberFormat.getNumberInstance(new Locale("id", "ID")).format(amount);
	}
 
}
