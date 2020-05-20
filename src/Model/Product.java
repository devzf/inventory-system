package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Zachary Fredricksen #000999825
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private IntegerProperty id;
    private StringProperty name;
    private DoubleProperty price;
    private IntegerProperty stock;
    private IntegerProperty min;
    private IntegerProperty max;
    
    public Product() {
        id = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        stock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }
    
    public IntegerProperty productIDProp() {
        return id;
    }
    
    public StringProperty productNameProp() {
        return name;
    }
    
    public DoubleProperty productPriceProp() {
        return price;
    }
    
    public IntegerProperty productStockProp() {
        return stock;
    }
    
    public void setID(int productID) {
        this.id.set(productID);
    }
    
    public void setName(String productName) {
        this.name.set(productName);
    }
    
    public void setPrice(double productPrice) {
        this.price.set(productPrice);
    }
    
    public void setStock(int productStock) {
        this.stock.set(productStock);
    }
    
    public void setMin(int productMin) {
        this.min.set(productMin);
    }
    
    public void setMax(int productMax) {
        this.max.set(productMax);
    }
    
    public void setPrice(int productMax) {
        this.price.set(productMax);
    }

    public int getID() {
        return this.id.get();
    }
    
    public String getName() {
        return this.name.get();
    }
    
    public double getPrice() {
        return this.price.get();
    }
    
    public int getStock() {
        return this.stock.get();
    }
    
    public int getMin() {
        return this.min.get();
    }
    
    public int getMax() {
        return this.max.get();
    }
    
    public void setProductParts(ObservableList<Part> parts) {
        this.associatedParts = parts;
    }
        
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }
    
    public void deleteAssociatedPart(Part associatedPart) {
        this.associatedParts.remove(associatedPart);
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
    
    //Product Validation Method
    public static String validProduct(String name, int min, int max, int inv, double price, ObservableList<Part> allParts, String errorMessage) {
        double partsCost = 0.00;
        for (int index = 0; index < allParts.size(); index++) {
            partsCost = partsCost + allParts.get(index).getPrice();
        }
        if (name.equals("")) {
            errorMessage = errorMessage + ("Missing name for product.");
        }
        if (min < 0) {
            errorMessage = errorMessage + ("The inventory must be a positive number.");
        }
        if (price < 0) {
            errorMessage = errorMessage + ("The price must be a positive number.");
        }
        if (min > max) {
            errorMessage = errorMessage + ("The minimum must be less than the maximum.");
        }
        if (inv < min || inv > max) {
            errorMessage = errorMessage + ("Inventory number must be between minimum and maximum values.");
        }
        if (allParts.size() < 1) {
            errorMessage = errorMessage + ("Product should have at least 1 part.");
        }
        if (partsCost > price) {
            errorMessage = errorMessage + ("The price should be greater than the cost of parts.");
        }
        
        return errorMessage;
    }
}
