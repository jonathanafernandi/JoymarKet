package controller;

import java.util.List;

import model.CartItem;
import model.Product;

// Controller class that handles cart item operations
public class CartItemHandler {

    // Get all cart items for a specific customer
    public static List<CartItem> getCartItems(String idCustomer) {
        return CartItem.getCartItems(idCustomer);
    }
    
    // Create a new cart item when customer adds a product to cart
    public static boolean createCartItem(String idCustomer, String idProduct, int count) {
        return CartItem.createCartItem(idCustomer, idProduct, count);
    }
    
    // Remove a product from the customer's cart
    public static boolean deleteCartItem(String idCustomer, String idProduct) {
        return CartItem.deleteCartItem(idCustomer, idProduct);
    }
    
    // Update the quantity of a product in the cart
    public static String editCartItem(String idCustomer, String idProduct, String countString) {

        // Get product data to check stock availability
        Product product = Product.getProduct(idProduct);
        if (product == null) {
            return "Product not found.";
        }
        
        // Validate count input
        if (countString == null || countString.trim().isEmpty()) {
            return "Count must be filled!";
        }
        
        int count;
        try {
            count = Integer.parseInt(countString.trim());
        } catch (NumberFormatException e) {
            return "Count must be numeric.";
        }
        
        // Quantity must be at least 1
        if (count < 1) {
            return "Count must be at least 1.";
        }

        // Quantity must not exceed available stock
        if (count > product.getStock()) {
            return "Count exceeds available stock. Available: " + product.getStock();
        }
        
        // Check if the cart item exists
        CartItem cartItem = CartItem.getCartItem(idCustomer, idProduct);
        if (cartItem == null) {
            return "Cart item not found.";
        }
        
        // Update cart item quantity
        boolean success = cartItem.editCartItem(count);
        if (!success) {
            return "Failed to update cart.";
        }
        
        // Log update result
        System.out.println("Cart updated: " + product.getName() + " x " + count);
        return null;
    }
    
    // Remove all items from the customer's cart
    public static boolean clearCart(String idCustomer) {
        return CartItem.clearCart(idCustomer);
    }

}
