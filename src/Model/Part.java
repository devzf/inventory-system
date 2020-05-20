package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Zachary Fredricksen #000999825
 */
public class Part {
    private IntegerProperty id;
    private StringProperty name;
    private DoubleProperty price;
    private IntegerProperty stock;
    private IntegerProperty min;
    private IntegerProperty max;
    
    public Part() {
        id = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        stock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }
    
    public IntegerProperty partIDProp() {
        return id;
    }
    
    public StringProperty partNameProp() {
        return name;
    }
    
    public DoubleProperty partPriceProp() {
        return price;
    }
    
    public IntegerProperty partStockProp() {
        return stock;
    }
    
    public void setID(int partID) {
        this.id.set(partID);
    }
    
    public void setName(String partName) {
        this.name.set(partName);
    }
    
    public void setPrice(double partPrice) {
        this.price.set(partPrice);
    }
    
    public void setStock(int partStock) {
        this.stock.set(partStock);
    }
    
    public void setMin(int partMin) {
        this.min.set(partMin);
    }
    
    public void setMax(int partMax) {
        this.max.set(partMax);
    }
    
    public void setPrice(int partMax) {
        this.price.set(partMax);
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
    
    // checks for valid data in Part
    public static String validPart(String name, int min, int max, int inv, double price, String errorMessage) {
        if (name == null) {
            errorMessage = errorMessage + ("Missing name for part.");
        }
        if (inv < 1) {
            errorMessage = errorMessage + ("The inventory must be a positive number.");
        }
        if (price < 1) {
            errorMessage = errorMessage + ("The price must be a positive number.");
        }
        if (min > max) {
            errorMessage = errorMessage + ("The minimum must be less than the maximum.");
        }
        if (inv < min || inv > max) {
            errorMessage = errorMessage + ("Inventory number must be between minimum and maximum values.");
        }
        return errorMessage;
    }
}
