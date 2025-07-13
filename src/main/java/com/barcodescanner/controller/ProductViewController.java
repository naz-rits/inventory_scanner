package com.barcodescanner.controller;

import com.barcodescanner.SceneManager;
import com.barcodescanner.api.ApiService;
import com.barcodescanner.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@Controller
public class ProductViewController {

    @Autowired
    private SceneManager sceneManager;

    private final ApiService apiService = new ApiService();

    @FXML private ImageView productImage;
    @FXML private Label productName;
    @FXML private Label productBarcode;
    @FXML private Label productPrice;
    @FXML private Label productDescription;
    @FXML private Button sceneButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private TextField productNameTextfield;
    @FXML private TextField productBarcodeTextfield;
    @FXML private TextField productPriceTextfield;
    @FXML private TextArea productDescriptionTextarea;

    private boolean isEditing = false;

    public void sceneSwitch(){
        sceneManager.switchScene("/view/Inventory.fxml", "Product View", Optional.empty());
    }

    public void productStage(Optional<Product> product) throws FileNotFoundException {
        productImage.setImage(new Image(new FileInputStream(product.get().getImageUrl())));
        productName.setText(product.get().getName());
        productBarcode.setText(product.get().getBarcode());
        productPrice.setText(String.valueOf(product.get().getPrice()));
        productDescription.setText(product.get().getDescription());

    }

    public void deleteButtonAction() throws FileNotFoundException {
        deleteButton.setOnAction(event -> {
            try {
                Product product = apiService.getProductByBarcode(productBarcode.getText().trim());
                apiService.deleteProduct(product.getBarcode());
                showAlert("Product deleted successfully");
                sceneManager.switchScene("/view/Inventory.fxml", "Inventory", Optional.empty());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void initialize(){
        productNameTextfield.setVisible(false);
        productBarcodeTextfield.setVisible(false);
        productPriceTextfield.setVisible(false);
        productDescriptionTextarea.setVisible(false);
        productNameTextfield.setManaged(false);
        productBarcodeTextfield.setManaged(false);
        productPriceTextfield.setManaged(false);
        productDescriptionTextarea.setManaged(false);
    }

    @FXML
    public void editButtonAction() throws Exception {
        isEditing = !isEditing;

        if (isEditing) {
            // Show editable fields
            productNameTextfield.setVisible(true);
            productNameTextfield.textProperty().bindBidirectional(productName.textProperty());

            productBarcodeTextfield.setVisible(true);
            productBarcodeTextfield.textProperty().bindBidirectional(productBarcode.textProperty());

            productPriceTextfield.setVisible(true);
            productPriceTextfield.textProperty().bindBidirectional(productPrice.textProperty());

            productDescriptionTextarea.setVisible(true);
            productDescriptionTextarea.textProperty().bindBidirectional(productDescription.textProperty());

            productNameTextfield.setManaged(true);
            productBarcodeTextfield.setManaged(true);
            productPriceTextfield.setManaged(true);
            productDescriptionTextarea.setManaged(true);

            // Hide display labels
            productName.setVisible(false);
            productBarcode.setVisible(false);
            productPrice.setVisible(false);
            productDescription.setVisible(false);
            productName.setManaged(false);
            productBarcode.setManaged(false);
            productPrice.setManaged(false);
            productDescription.setManaged(false);

            editButton.setText("Save Product");
        } else {
            // Hide editable fields
            productNameTextfield.setVisible(false);
            productBarcodeTextfield.setVisible(false);
            productPriceTextfield.setVisible(false);
            productDescriptionTextarea.setVisible(false);
            productNameTextfield.setManaged(false);
            productBarcodeTextfield.setManaged(false);
            productPriceTextfield.setManaged(false);
            productDescriptionTextarea.setManaged(false);

            // Show display labels
            productName.setVisible(true);
            productBarcode.setVisible(true);
            productPrice.setVisible(true);
            productDescription.setVisible(true);
            productName.setManaged(true);
            productBarcode.setManaged(true);
            productPrice.setManaged(true);
            productDescription.setManaged(true);

            Product product = apiService.getProductByBarcode(productBarcode.getText());

            // Update label text with textfield values
            productName.setText(productNameTextfield.getText());
            product.setName(productNameTextfield.getText());

            productBarcode.setText(productBarcodeTextfield.getText());
            product.setBarcode(productBarcodeTextfield.getText());

            productPrice.setText(productPriceTextfield.getText());
            product.setPrice(Double.parseDouble(productPriceTextfield.getText()));

            productDescription.setText(productDescriptionTextarea.getText());
            product.setDescription(productDescriptionTextarea.getText());

            apiService.updateProduct(product);
            showAlert("Product updated successfully");

            editButton.setText("Edit");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}
