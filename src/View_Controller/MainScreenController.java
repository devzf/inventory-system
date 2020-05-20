/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import InventorySystem.InventorySystem;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import Model.Inventory;
import Model.Part;
import static Model.Inventory.validatePartDelete;
import Model.Product;
import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zachf
 */
public class MainScreenController implements Initializable {

    @FXML
    private Button PartsSearch;
    @FXML
    private TextField PartsSearchText;
    @FXML
    private TableColumn<Part, Integer> PartsTablePartID;
    @FXML
    private TableColumn<Part, String> PartsTablePartName;
    @FXML
    private TableColumn<Part, Integer> PartsTableInventoryLevel;
    @FXML
    private TableColumn<Part, Double> PartsTablePrice;
    @FXML
    private Button PartsDelete;
    @FXML
    private Button PartsModify;
    @FXML
    private Button PartsAdd;
    @FXML
    private Button ProductsSearch;
    @FXML
    private TextField ProductsSearchText;
    @FXML
    private TableColumn<Product, Integer> ProductsTableProductID;
    @FXML
    private TableColumn<Product, String> ProductsTableProductName;
    @FXML
    private TableColumn<Product, Integer> ProductsTableInventoryLevel;
    @FXML
    private TableColumn<Product, Double> ProductsTablePrice;
    @FXML
    private Button ProductsDelete;
    @FXML
    private Button ProductsModify;
    @FXML
    private Button ProductsAdd;
    @FXML
    private Button MainExit;
    @FXML
    private TableView<Part> PartsTable;
    @FXML
    private TableView<Product> ProductsTable;

    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() {
        return modifyProductIndex;
    }

    public MainScreenController() {
    }
    
    public void runMain(InventorySystem mainApp) {
        updatePartTableView();
        updateProductTableView();
    }

    void clearSearchParts() {
        PartsSearchText.setText("");
        updatePartTableView();
    }
 
    void clearSearchProducts() {
        PartsSearchText.setText("");
        updateProductTableView();
        
    }

    public void updatePartTableView() {
        PartsTable.setItems(getAllParts());
    }

    public void updateProductTableView() {
        ProductsTable.setItems(getAllProducts());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PartsTablePartID.setCellValueFactory(cellData -> cellData.getValue().partIDProp().asObject());
        PartsTablePartName.setCellValueFactory(cellData -> cellData.getValue().partNameProp());
        PartsTableInventoryLevel.setCellValueFactory(cellData -> cellData.getValue().partStockProp().asObject());
        PartsTablePrice.setCellValueFactory(cellData -> cellData.getValue().partPriceProp().asObject());
        ProductsTableProductID.setCellValueFactory(cellData -> cellData.getValue().productIDProp().asObject());
        ProductsTableProductName.setCellValueFactory(cellData -> cellData.getValue().productNameProp());
        ProductsTableInventoryLevel.setCellValueFactory(cellData -> cellData.getValue().productStockProp().asObject());
        ProductsTablePrice.setCellValueFactory(cellData -> cellData.getValue().productPriceProp().asObject());
        updatePartTableView();
        updateProductTableView();
    }    

    @FXML
    private void searchParts(ActionEvent event) throws IOException {
        String searchText = PartsSearchText.getText();
        System.out.println(searchText);
        int partIndex = -1;
        if (PartsSearchText.getText().length() > 0) {
            if (Inventory.lookupPart(searchText) == null) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Search Error");
                alert.setHeaderText("Part not found");
                alert.setContentText("The search term entered does not match any part!");
                alert.showAndWait();
            } else {
                partIndex = Inventory.lookupPart(searchText).getID();
                Part tempPart = Inventory.getAllParts().get(partIndex-1);
                ObservableList<Part> tempProdList = FXCollections.observableArrayList();
                tempProdList.add(tempPart);
                PartsTable.setItems(tempProdList);
            }
        } else {
            clearSearchParts();
        }
    }

    @FXML
    private void deletePart(ActionEvent event) {
        Part part = PartsTable.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Part Delete Error!");
            alert.setHeaderText("Part cannot be removed!");
            alert.setContentText("This part is used in a product.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Delete");
            alert.setHeaderText("Confirm?");
            alert.setContentText("Are you sure you want to delete " + part.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Model.Inventory.deletePart(part);
                updatePartTableView();
                System.out.println("Part " + part.getName() + " was removed.");
            } else {
                System.out.println("Part " + part.getName() + " was not removed.");
            }
        }
    }

    @FXML
    private void modifyPart(ActionEvent event) throws IOException {
        if(PartsTable.getSelectionModel().getSelectedItem() != null) {
            modifyPart = PartsTable.getSelectionModel().getSelectedItem();
            modifyPartIndex = getAllParts().indexOf(modifyPart);
            Parent modifyParts;
            modifyParts = FXMLLoader.load(getClass().getResource("ModifyPartScreen.fxml"));

            Scene scene = new Scene(modifyParts);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error Modifying Part");
            alert.setHeaderText("Error Modifying Part");
            alert.setContentText("Please select a part in the table before modifying.");
            alert.showAndWait();
        }
    }

    @FXML
    private void addPart(ActionEvent event) throws IOException {
        Parent addParts = FXMLLoader.load(getClass().getResource("AddPartScreen.fxml"));
        Scene scene = new Scene(addParts);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void searchProducts(ActionEvent event) {
        String searchText = ProductsSearchText.getText();
        int prodIndex = -1;
        if (ProductsSearchText.getText().length() > 0) {
            if (Inventory.lookupProduct(searchText) == null) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Search Error!");
                alert.setHeaderText("Product not found");
                alert.setContentText("The search term entered does not match any Product!");
                alert.showAndWait();
            } else {
                prodIndex = Inventory.lookupProduct(searchText).getID();
                Product tempProd = Inventory.getAllProducts().get(prodIndex-1);
                ObservableList<Product> tempProdList = FXCollections.observableArrayList();
                tempProdList.add(tempProd);
                ProductsTable.setItems(tempProdList);
            }
        } else {
            clearSearchProducts();
        }
    }

    @FXML
    private void deleteProduct(ActionEvent event) throws IOException {
        Product product = ProductsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Delete Product?");
        alert.setContentText("Are you sure you want to delete " + product.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Model.Inventory.deleteProduct(product);
            updateProductTableView();
            System.out.println("Product " + product.getName() + " was deleted.");
        } else {
            System.out.println("Error deleting product. " + product.getName() + " was not deleted.");
        }
    }

    @FXML
    private void modifyProduct(ActionEvent event) throws IOException {
        if(ProductsTable.getSelectionModel().getSelectedItem() != null) {
            modifyProduct = ProductsTable.getSelectionModel().getSelectedItem();
            modifyProductIndex = getAllProducts().indexOf(modifyProduct);
            Parent modifyProducts = FXMLLoader.load(getClass().getResource("ModifyProductScreen.fxml"));
            Scene scene = new Scene(modifyProducts);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error Modifying Product");
            alert.setHeaderText("Error Modifying Product");
            alert.setContentText("Please select a product in the table before modifying.");
            alert.showAndWait();
        }        
    }

    @FXML
    private void addProduct(ActionEvent event) throws IOException {
        Parent addProducts = FXMLLoader.load(getClass().getResource("AddProductScreen.fxml"));
        Scene scene = new Scene(addProducts);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void exitMainScreen(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Leave Inventory System ");
        alert.setHeaderText(" Leave Program ");
        alert.setContentText("Are you sure you want to leave?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println("Exit failed. Please try again.");
        }
    }

    
}
