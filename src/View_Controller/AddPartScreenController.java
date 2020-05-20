/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author zachf
 */
public class AddPartScreenController implements Initializable {
    
    private boolean isOutsourced;
    private String exceptionMessage = new String();
    private int partID;

    @FXML
    private RadioButton AddPartOutsourced;
    @FXML
    private RadioButton AddPartInHouse;
    @FXML
    private TextField PartID;
    @FXML
    private TextField PartName;
    @FXML
    private TextField PartCompanyName;
    @FXML
    private TextField PartInv;
    @FXML
    private TextField PartPrice;
    @FXML
    private TextField PartMax;
    @FXML
    private TextField PartMin;
    @FXML
    private Label PartLabelCompanyName;
    @FXML
    private Button AddPartCancel;
    @FXML
    private Button AddPartSave;
    @FXML
    private Label PartLabelMachineID;
    @FXML
    private TextField PartMachineID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartIDCount();
        PartID.setText("Auto Gen: " + partID);
        
        // Set initial state to outsourced.
        AddPartInHouse.setSelected(false);
        AddPartOutsourced.setSelected(true);
        PartLabelMachineID.setVisible(false);
        PartMachineID.setVisible(false);
        PartMachineID.setText("");
        PartLabelCompanyName.setVisible(true);
        PartCompanyName.setVisible(true);
        isOutsourced = true;
    }    

    @FXML
    private void enableAddOutsourcedPart(ActionEvent event) {
        AddPartInHouse.setSelected(false);
        AddPartOutsourced.setSelected(true);
        PartLabelMachineID.setVisible(false);
        PartMachineID.setVisible(false);
        PartMachineID.setText("");
        PartLabelCompanyName.setVisible(true);
        PartCompanyName.setVisible(true);
        isOutsourced = true;
    }

    @FXML
    private void enableAddInHousePart(ActionEvent event) {
        AddPartInHouse.setSelected(true);
        AddPartOutsourced.setSelected(false);
        PartLabelMachineID.setVisible(true);
        PartMachineID.setVisible(true);
        PartLabelCompanyName.setVisible(false);
        PartCompanyName.setVisible(false);
        PartCompanyName.setText("");
        isOutsourced = false;
    }

    @FXML
    private void cancelAddPart(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to delete part " + PartName.getText() + "?");
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
    private void saveAddPart(ActionEvent event) throws IOException {
        String partName = PartName.getText();
        String partInv = PartInv.getText();
        String partPrice = PartPrice.getText();
        String partMin = PartMin.getText();
        String partMax = PartMax.getText();
        String partCompanyName = PartCompanyName.getText();
        String partMachineID = PartMachineID.getText();
        
        try {
            exceptionMessage = Part.validPart(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error Adding Part");
                alert.setHeaderText("Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                if (isOutsourced) {
                    OutsourcedPart outsourcedPart = new OutsourcedPart();

                    outsourcedPart.setID(partID);
                    outsourcedPart.setName(partName);
                    outsourcedPart.setPrice(Double.parseDouble(partPrice));
                    outsourcedPart.setStock(Integer.parseInt(partInv));
                    outsourcedPart.setMin(Integer.parseInt(partMin));
                    outsourcedPart.setMax(Integer.parseInt(partMax));
                    outsourcedPart.setCompanyName(partCompanyName);
                    Inventory.addPart(outsourcedPart);
                } else {
                    InhousePart inhousePart = new InhousePart();

                    inhousePart.setID(partID);
                    inhousePart.setName(partName);
                    inhousePart.setPrice(Double.parseDouble(partPrice));
                    inhousePart.setStock(Integer.parseInt(partInv));
                    inhousePart.setMin(Integer.parseInt(partMin));
                    inhousePart.setMax(Integer.parseInt(partMax));
                    inhousePart.setMachineID(Integer.parseInt(partMachineID));          
                    Inventory.addPart(inhousePart);
                }

                Parent partsSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(partsSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error Adding Part");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText("Form has empty fields.");
            alert.showAndWait();
        }
    }
    
}
