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

public class CartItemWindow {

	public static void showCart(Stage parentStage, Customer customer) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(parentStage);
		stage.setTitle("My Cart - JoymarKet");
		
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #F5F5F5;");
		
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
		
		List<CartItem> cartItems = CartItemHandler.getCartItems(customer.getIdUser());
		
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
			browseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12px 30px; -fx-cursor: hand; -fx-background-radius: 5;");
			browseButton.setOnAction(e -> {
				stage.close();
				ProductWindow.showProductsForCustomer(parentStage, customer);
			});
			
			emptyBox.getChildren().addAll(messageLabel, subLabel, browseButton);
			centerBox.getChildren().add(emptyBox);
		} else {
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			scrollPane.setPrefHeight(350);
			
			VBox cartList = new VBox(12);
			cartList.setPadding(new Insets(10));
			
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
			
			VBox checkoutBox = new VBox(15);
			checkoutBox.setPadding(new Insets(20));
			checkoutBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
			
			HBox summaryRow = new HBox(20);
			summaryRow.setAlignment(Pos.CENTER_LEFT);
			
			VBox summaryLeft = new VBox(8);
			Label itemCountLabel = new Label("Total Items: " + cartItems.size());
			itemCountLabel.setFont(Font.font("Arial", 14));
			
			Label subtotalLabel = new Label("Subtotal: Rp" + formatCurrency(subtotalRef[0]));
			subtotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
			subtotalLabel.setTextFill(Color.web("#2E7D32"));
			
			summaryLeft.getChildren().addAll(itemCountLabel, subtotalLabel);
			
			Region spacer2 = new Region();
			HBox.setHgrow(spacer2, Priority.ALWAYS);
			
			VBox promoBox = new VBox(8);
			Label promoLabel = new Label("Promo Code (optional):");
			promoLabel.setFont(Font.font("Arial", 12));
			
			TextField promoField = new TextField();
			promoField.setPromptText("Enter promo code");
			promoField.setStyle("-fx-font-size: 13px; -fx-padding: 8px;");
			promoField.setPrefWidth(200);
			
			promoBox.getChildren().addAll(promoLabel, promoField);
			
			summaryRow.getChildren().addAll(summaryLeft, spacer2, promoBox);
			
			Separator separator = new Separator();
			
			HBox buttonRow = new HBox(15);
			buttonRow.setAlignment(Pos.CENTER);
			
			Button checkoutButton = new Button("Checkout");
			checkoutButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12px 40px; -fx-cursor: hand; -fx-background-radius: 5;");
			
			Button continueButton = new Button("Continue Shopping");
			continueButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 25px; -fx-cursor: hand;");
			
			Button clearButton = new Button("Clear Cart");
			clearButton.setStyle("-fx-background-color: F44336; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 25px; -fx-cursor: hand;");
			
			checkoutButton.setOnAction(e -> {
				String error = CustomerHandler.checkout(customer.getIdUser(), promoField.getText().trim());
				
				if (error != null) {
					showAlert(Alert.AlertType.ERROR, "Checkout Failed", error);
				} else {
					showAlert(Alert.AlertType.INFORMATION, "Success", "Order placed successfully!\n\nThank you for shopping with JoymarKet!");
					stage.close();
				}
			});
			
			continueButton.setOnAction(e -> {
				stage.close();
				ProductWindow.showProductsForCustomer(parentStage, customer);
			});
			
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
		
		Button closeButton = new Button("Close");
		closeButton.setStyle("-fx-background-color: #607D8B; -fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10px 25px; -fx-cursor: hand;");
		closeButton.setOnAction(e -> stage.close());
		
		HBox bottomBox = new HBox();
		bottomBox.setPadding(new Insets(15));
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.getChildren().add(closeButton);
		
		root.setCenter(centerBox);
		root.setBottom(bottomBox);
		
		Scene scene = new Scene(root, 750, 650);
		stage.setScene(scene);
		stage.show();
	}
	
	private static HBox createCartItemCard(CartItem cartItem, Product product, Customer customer, Stage stage, VBox parentList) {
		HBox card = new HBox(20);
		card.setPadding(new Insets(15));
		card.setAlignment(Pos.CENTER_LEFT);
		card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
		
		VBox infoBox = new VBox(8);
		
		Label nameLabel = new Label(product.getName());
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label priceLabel = new Label("Rp" + formatCurrency(product.getPrice()) + " each");
		priceLabel.setFont(Font.font("Arial", 13));
		priceLabel.setTextFill(Color.web("#666666"));
		
		Label quantityLabel = new Label("Quantity: " + cartItem.getCount());
		quantityLabel.setFont(Font.font("Arial", 13));
		
		Label subtotalLabel = new Label("Total: Rp" + formatCurrency(product.getPrice() * cartItem.getCount()));
		subtotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		subtotalLabel.setTextFill(Color.web("#2E7D32"));
		
		infoBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel, subtotalLabel);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		VBox actionBox = new VBox(10);
		actionBox.setAlignment(Pos.CENTER);
		
		Button removeButton = new Button("Remove");
		removeButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-size: 12px; -fx-padding: 8px 20px; -fx-cursor: hand; -fx-background-radius: 5;");
		
		removeButton.setOnAction(e -> {
			String error = CustomerHandler.removeFromCart(customer.getIdUser(), product.getIdProduct());
			
			if (error != null) {
				showAlert(Alert.AlertType.ERROR, "Error", error);
			} else {
				parentList.getChildren().remove(card);
				
				if (parentList.getChildren().isEmpty()) {
					stage.close();
					showCart(stage.getOwner() instanceof Stage ? (Stage) stage.getOwner() : null, customer);
				}
			}
		});
		
		actionBox.getChildren().add(removeButton);
		
		card.getChildren().addAll(infoBox, spacer, actionBox);
		return card;
	}
	
	private static void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	private static String formatCurrency(double amount) {
		return NumberFormat.getNumberInstance(new Locale("id", "ID")).format(amount);
	}

}
