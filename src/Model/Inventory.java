package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Zachary Fredricksen #000999825
 */
public class Inventory {
    
    private static int indexPart = 0;
    private static int indexProduct = 0;
    
    private static int partIDCount = 0;
    private static int productIDCount = 0;
    
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    // class constructor
    public Inventory() {
        
    }
    
    public static int getPartIDCount() {
        partIDCount++;
        return partIDCount;
    }
    
    public static int getProductIDCount() {
        productIDCount++;
        return productIDCount;
    }
    
    public static void addPart(Part part) {
        allParts.add(part);
    }
    
    public static void addProduct(Product product) {
        allProducts.add(product);
    }
    
    public static Part lookupPart(int partID) {
        boolean foundPart = false;
        int indexPart = 0;
        
        for (int index = 0; index < allParts.size(); index++) {
            if (partID == allParts.get(index).getID()) {
                indexPart = index;
                foundPart = true;
            }
        }
        
        if (foundPart) {
            return allParts.get(indexPart);
        } else {
            System.out.println("No parts found.");
            return null;
        }        
    }
    
    public static Product lookupProduct(int productID) {
        boolean foundProduct = false;
        int indexProduct = 0;
        
        for (int index = 0; index < allProducts.size(); index++) {
            if (productID == allProducts.get(index).getID()) {
                indexProduct = index;
                foundProduct = true;
            }
        }
        
        if (foundProduct) {
            return allProducts.get(indexProduct);
        } else {
            System.out.println("No products found.");
            return null;
        }
    }
    
    public static Part lookupPart(String partName) {
        boolean foundPart = false;
        int indexPart = 0;
        
        for (int index = 0; index < allParts.size(); index++) {
            if (partName == null ? allParts.get(index).getName() == null : partName.equals(allParts.get(index).getName())) {
                indexPart = index;
                foundPart = true;
            }
        }
        
        if (foundPart) {
            return allParts.get(indexPart);
        } else {
            System.out.println("No parts found.");
            return null;
        } 
    }
    
    public static Product lookupProduct(String productName) {
        boolean foundProduct = false;
        int indexProduct = 0;
        
        for (int index = 0; index < allProducts.size(); index++) {
            if (productName == null ? allProducts.get(index).getName() == null : productName.equals(allProducts.get(index).getName())) {
                indexProduct = index;
                foundProduct = true;
            }
        }
        
        if (foundProduct) {
            return allProducts.get(indexProduct);
        } else {
            System.out.println("No products found.");
            return null;
        }
    }
    
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }
    
    public static void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }
    
    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }
    
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean validatePartDelete(Part part) {
        boolean isFound = false;

        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getAllAssociatedParts().contains(part)) {
                isFound = true;
            }
        }
        return isFound;
    }
}