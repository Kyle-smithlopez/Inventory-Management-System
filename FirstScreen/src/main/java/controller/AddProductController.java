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

/** Add Product method allows the user to add a product and pulls up the Add Product screen. */
public class AddProductController implements Initializable {

    private static int productId = 1000;
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
    public TableView<Part> deleteProductTableView;
    @FXML
    public TableColumn<Part, Integer> removePartIdCol;
    @FXML
    public TableColumn<Part, String> removePartNameCol;
    @FXML
    public TableColumn<Part, Integer> removeInvCol;
    @FXML
    public TableColumn<Part, Double> removePriceCol;
    @FXML
    private TextField productIdTxt;
    @FXML
    private TextField productNameTxt;
    @FXML
    private TextField productInvTxt;
    @FXML
    private TextField productPriceTxt;
    @FXML
    private TextField productMinTxt;
    @FXML
    private TextField productMaxTxt;

    /**
     * The Add method moves a part from the Inventory and assigns the part to the product.
     * @param actionEvent Add Button Clicked
     * */
    @FXML
    public void OnActionAdd(ActionEvent actionEvent) {

        Part selectedPart = addPartTableView.getSelectionModel().getSelectedItem();

        if(selectedPart == null)
            return;

        associatedParts.add(selectedPart);
        deleteProductTableView.setItems(associatedParts);
    }
    /**
     * The Remove Button method removed the assigned part from the product.
     * @param actionEvent Remove button clicked.
     * */
    @FXML
    public void OnActionRemove(ActionEvent actionEvent) {
        Part selectedPart = deleteProductTableView.getSelectionModel().getSelectedItem();

        if(selectedPart == null)
            return;

        associatedParts.remove(selectedPart);
        deleteProductTableView.setItems(associatedParts);
    }

    /**
     * The Search method allows the user to search for parts by ID or name.
     * @param  actionEvent Search intitated by enter button.
     * */
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
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("No parts found.");
                alert.showAndWait();
            }
        }

        addPartTableView.setItems(associatedParts);
    }
    /** The Save Product Method saves the data to the program.
     * @param event Save button clicked.
     * RUNTIME ERROR: Errors arose with saving data including assigned parts. This was resolved by modifying the scope of the part.
     * */
    @FXML
    public void OnActionSaveProduct(ActionEvent event) throws IOException {

    try {
        int id = Integer.parseInt(productIdTxt.getText());
        String name = productNameTxt.getText();
        int stock = Integer.parseInt(productInvTxt.getText());
        double price = Double.parseDouble(productPriceTxt.getText());
        int min = Integer.parseInt(productMinTxt.getText());
        int max = Integer.parseInt(productMaxTxt.getText());
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
        Inventory.addProduct(newProduct);
        productId = getProductIdCount();

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

    /**
     * The Cancel Method discards data entered and returns to the main menu.
     * @param event by cancel button clicked.
     * */
    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all Text Field values, Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
         if(result.isPresent() && result.get()  == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/smith/firstscreen/Main.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
        }
    }

    //Product ID Counter
    public static int getProductIdCount() {
        productId++;
        return productId;
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
        removeInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        removePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIdTxt.setText(String.valueOf(productId));

    }

}
