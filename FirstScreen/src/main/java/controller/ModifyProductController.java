package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.lookupPartId;
import static model.Inventory.lookupPartName;
    /** The modify product controller pulls up the selected product data. */
public class ModifyProductController implements Initializable {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    Stage stage;
    Parent scene;

    @FXML
    public TextField searchPart;
    @FXML
    public TableView<Part> addPartTableView;
    @FXML
    public TableColumn<Part, Integer> addPartIdCol;
    @FXML
    public TableColumn<Part, String> addPartNameCol;
    @FXML
    public TableColumn<Part, Integer> addPartInvCol;
    @FXML
    public TableColumn<Part, Double> addPartPriceCol;
    @FXML
    public TableView<Part> removePartTableView;
    @FXML
    public TableColumn<Part, Integer> removePartIdCol;
    @FXML
    public TableColumn<Part, String> removePartNameCol;
    @FXML
    public TableColumn<Part, Integer> removePartInvCol;
    @FXML
    public TableColumn<Part, Double> removePartPriceCol;
    @FXML
    public TextField modifyProductIdTxt;
    @FXML
    public TextField modifyProductNameTxt;
    @FXML
    public TextField modifyProductInvTxt;
    @FXML
    public TextField modifyProductPriceTxt;
    @FXML
    public TextField modifyProductMinTxt;
    @FXML
    public TextField modifyProductMaxTxt;

    /** The send product method retrieves the selected products data. */
    public void sendProduct(Product product) {
        modifyProductIdTxt.setText(String.valueOf(product.getId()));
        modifyProductInvTxt.setText(String.valueOf(product.getStock()));
        modifyProductNameTxt.setText(product.getName());
        modifyProductPriceTxt.setText(String.valueOf(product.getPrice()));
        modifyProductMinTxt.setText(String.valueOf(product.getMin()));
        modifyProductMaxTxt.setText(String.valueOf(product.getMax()));
        removePartTableView.setItems(product.getAllAssociatedParts());
    }

    /** The add method adds a part from the inventory to the product. */
    @FXML
    public void OnActionAdd(ActionEvent actionEvent) {
        Part selectedPart = addPartTableView.getSelectionModel().getSelectedItem();

        if(selectedPart == null)
            return;

        associatedParts.add(selectedPart);
        removePartTableView.setItems(associatedParts);
    }
    /** The remove method removes the selected part from the product. */
    @FXML
    public void OnActionRemove(ActionEvent actionEvent) {
        Part selectedPart = removePartTableView.getSelectionModel().getSelectedItem();

        if(selectedPart == null)
            return;

        associatedParts.remove(selectedPart);
        removePartTableView.setItems(associatedParts);
    }
    /** The modify product method saves the updated data to the current product.
     * RUNTIME ERRORS: Issues with saving the updated data occurred, issue was caused due to variable declaration and was resolved.
     * */
    @FXML
    public void OnActionModifyProduct(ActionEvent event) throws IOException {

        try {
            int id = Integer.parseInt(modifyProductIdTxt.getText());
            String name = modifyProductNameTxt.getText();
            int stock = Integer.parseInt(modifyProductInvTxt.getText());
            double price = Double.parseDouble(modifyProductPriceTxt.getText());
            int min = Integer.parseInt(modifyProductMinTxt.getText());
            int max = Integer.parseInt(modifyProductMaxTxt.getText());
            Product newProduct = new Product(id, name, price, stock, min, max);

            if(name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: Name Field must not be blank.");
                alert.showAndWait();
                return;
            }
            if(min > stock || stock > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The Min must be less than the Max, and Inv must be greater than the Min and less than the Max");
                alert.showAndWait();
                return;
            }

            for (Part part : associatedParts) {
                newProduct.addAssociatedParts(part);
            }
            Inventory.updateProduct(id, newProduct);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/smith/firstscreen/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each Text Field.");
            alert.showAndWait();
        }
    }

    /** The search option allows the user to search for a part by name or ID. */
    @FXML
    public void OnActionSearch(ActionEvent actionEvent) {
        String q = searchPart.getText();

        ObservableList<Part> associatedParts = lookupPartName(q);

        if(associatedParts.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Part p = lookupPartId(id);
                if (p != null)
                    associatedParts.add(p);
            }
            catch(NumberFormatException e)
            {
                //Ignore
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("No parts found.");
                alert.showAndWait();
            }
        }

        addPartTableView.setItems(associatedParts);
    }

    /** The cancel method discards entered data and returns to main menu. */
    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will not save any changes, Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get()  == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/smith/firstscreen/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addPartTableView.setItems(Inventory.getAllParts());
        addPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        removePartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        removePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        removePartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        removePartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

}
