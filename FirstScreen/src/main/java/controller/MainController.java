package controller;

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

import static model.Inventory.*;

/** The Main Controller creates the Main Menu. */
public class MainController implements Initializable {


    Stage stage;
    Parent scene;

    @FXML
    public TextField searchPart;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    public TextField searchProduct;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    @FXML
    private TableColumn<Product, Integer> productInvCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Double> productPriceCol;

   /** Add Part redirects user to add a part. */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/smith/firstscreen/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /** Add Product redirects user to add a product. */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/smith/firstscreen/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Delete part method deletes selected part. */
    @FXML
    void onActionDeletePart(ActionEvent event) {
        Part part = partTableView.getSelectionModel().getSelectedItem();
        if(partTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Error: No Part Selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get()  == ButtonType.OK) {
                Inventory.deletePart(part);
            }
        }
    }

    /** Delete Product method deletes selected product. */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        Product product = productTableView.getSelectionModel().getSelectedItem();
        if(productTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Error: No Product Selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get()  == ButtonType.OK) {
                if(!product.getAllAssociatedParts().isEmpty()) {
                    Alert assocAlert = new Alert(Alert.AlertType.WARNING);
                    assocAlert.setTitle("Warning Dialog");
                    assocAlert.setContentText("Product has at least 1 associated part");
                    assocAlert.showAndWait();
                }
                else {
                    Inventory.deleteProduct(product);
                }
            }
        }
    }

    /** Modify Part Method redirects user to modify selected Part. */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {

        if (partTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Error: No Part Selected");
            alert.showAndWait();
        } else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/smith/firstscreen/ModifyPart.fxml"));
            loader.load();

            ModifyPartController MPController = loader.getController();
            MPController.sendPart(partTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        }
    }

    /** The Modify Product method redirects user to modify selected product.
     * RUNTIME ERROR: Early drafts of application had errors pulling the selected product data.
     * */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {

        if(productTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Error: No Product Selected");
            alert.showAndWait();
        } else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/smith/firstscreen/ModifyProduct.fxml"));
            loader.load();

            ModifyProductController MPController = loader.getController();
            MPController.sendProduct(productTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** The Search part method searches Part table by name or ID. */
    @FXML
    void OnActionSearchPart(ActionEvent event) {
        String q = searchPart.getText();

        ObservableList<Part> parts = lookupPartName(q);

        if(parts.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Part p = lookupPartId(id);
                if (p != null)
                    parts.add(p);
            }
            catch(NumberFormatException e)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("No parts found.");
                alert.showAndWait();
            }
        }

        partTableView.setItems(parts);
    }

    /** The Search product method searches Part table by name or ID. */
    @FXML
    public void OnActionSearchProduct(ActionEvent event) {
        String q = searchProduct.getText();

        ObservableList<Product> products = lookupProductName(q);

        if (products.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Product p = lookupProductId(id);
                if (p != null)
                    products.add(p);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("No products found.");
                alert.showAndWait();
            }
        }
        productTableView.setItems(products);
    }



    /** Exit method allows user to exit the application. */
    @FXML
    void onActionExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

}