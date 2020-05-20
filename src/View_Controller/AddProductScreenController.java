/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import Model.Part;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import static Model.Inventory.getAllParts;
import Model.Product;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zachf
 */
public class AddProductScreenController implements Initializable {

    @FXML
    private Button PartsSearchButton;
    @FXML
    private TextField PartsSearchText;
    @FXML
    private TableColumn<Part, Integer> AddPartID;
    @FXML
    private TableColumn<Part, String> AddPartName;
    @FXML
    private TableColumn<Part, Integer> AddInventoryLevel;
    @FXML
    private TableColumn<Part, Double> AddPrice;
    @FXML
    private Button Add;
    @FXML
    private TableColumn<Part, Integer> DeletePartID;
    @FXML
    private TableColumn<Part, String> DeletePartName;
    @FXML
    private TableColumn<Part, Integer> DeleteInventory;
    @FXML
    private TableColumn<Part, Double> DeletePrice;
    @FXML
    private Button Delete;
    @FXML
    private Button AddProductCancel;
    @FXML
    private Button AddProductSave;
    @FXML
    private TextField ProductID;
    @FXML
    private TextField ProductName;
    @FXML
    private TextField ProductInv;
    @FXML
    private TextField ProductPrice;
    @FXML
    private TextField ProductMax;
    @FXML
    private TextField ProductMin;
    @FXML
    private TableView<Part> AddPartTable;
    @FXML
    private TableView<Part> DeletePartTable;
    
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private String errorMessage = new String();
    private int productID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AddPartID.setCellValueFactory(cellData -> cellData.getValue().partIDProp().asObject());
        AddPartName.setCellValueFactory(cellData -> cellData.getValue().partNameProp());
        AddInventoryLevel.setCellValueFactory(cellData -> cellData.getValue().partStockProp().asObject());
        AddPrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProp().asObject());

        DeletePartID.setCellValueFactory(cellData -> cellData.getValue().partIDProp().asObject());
        DeletePartName.setCellValueFactory(cellData -> cellData.getValue().partNameProp());
        DeleteInventory.setCellValueFactory(cellData -> cellData.getValue().partStockProp().asObject());
        DeletePrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProp().asObject());

        updatePartTableView();
        updateCurrentPartTableView();

        productID = Inventory.getProductIDCount();
        ProductID.setText("Auto Gen: " + productID);
    }    
    
    public void updatePartTableView() {
        AddPartTable.setItems(getAllParts());
    }

    public void updateCurrentPartTableView() {
        DeletePartTable.setItems(currentParts);
    }
    
    void clearSearchAdd() {
        PartsSearchText.setText("");
        updatePartTableView();
    }

    @FXML
    private void searchAddProduct() {
        String searchPart = PartsSearchText.getText();
        int partIndex = -1;
        
        if (PartsSearchText.getText().length() > 0) {
            if (Inventory.lookupPart(searchPart) == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Search");
                alert.setHeaderText("Part not found");
                alert.setContentText("The part has not been found.");
                alert.showAndWait();
            } else {
                partIndex = Inventory.lookupPart(searchPart).getID();
                Part tempPart = Inventory.getAllParts().get(partIndex-1);

                ObservableList<Part> tempProdList = FXCollections.observableArrayList();
                tempProdList.add(tempPart);
                AddPartTable.setItems(tempProdList);
            }
        } else {
            clearSearchAdd();
        }
    }

    @FXML
    private void addAddProductPart(ActionEvent event) {
        Part part = AddPartTable.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateCurrentPartTableView();
    }

    @FXML
    private void deleteAddProductPart(ActionEvent event) {
        Part part = DeletePartTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Current Part Delete!");
        alert.setContentText("Are you sure you want to delete part " + part.getName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.out.println("Part deleted.");
            currentParts.remove(part);
        } else {
            System.out.println("Cancel clicked.");
        }
    }

    @FXML
    private void cancelAddProduct(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Delete");
        
        if (ProductName.getText().length() > 0) {
            alert.setContentText("Are you sure you want to cancel adding " + ProductName.getText() + " to the product list?");
        } else {
            alert.setContentText("Are you sure you want to cancel adding this product?");
        }
        
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent partsCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(partsCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("Cancel has been clicked.");
        }
    }

    @FXML
    private void saveAddProduct(ActionEvent event) throws IOException {
        String productName = ProductName.getText();
        String productInv = ProductInv.getText();
        String productPrice = ProductPrice.getText();
        String productMin = ProductMin.getText();
        String productMax = ProductMax.getText();

        try {

            errorMessage = Product.validProduct(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv),
                    Double.parseDouble(productPrice), currentParts, errorMessage);

            if (errorMessage.length() > 0) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Product");
                alert.setHeaderText("Error");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
            } else {
                System.out.println("Product name: " + productName);

                Product newProduct = new Product();

                newProduct.setID(productID);
                newProduct.setName(productName);
                newProduct.setPrice(Double.parseDouble(productPrice));
                newProduct.setStock(Integer.parseInt(productInv));
                newProduct.setMin(Integer.parseInt(productMin));
                newProduct.setMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.addProduct(newProduct);

                Parent productsSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(productsSave);

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be left blank!");
            alert.showAndWait();
        }
    }

    
}
