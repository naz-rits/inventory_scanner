package com.barcodescanner.controller;

import com.barcodescanner.SceneManager;
import com.barcodescanner.services.ProductService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Component
public class InventoryController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private TextField productTextField;

    @FXML
    private Button productSubmitButton;

    @FXML
    private Label productNameLabel;

    @FXML
    private ImageView productImage;

    @FXML
    private Label headerLabel;

    @FXML
    private Button goToAddProduct;

    @FXML
    private void handleGoToAddProduct() {
        System.out.println("Button clicked!");
        sceneManager.switchScene("/view/AddProduct.fxml", "Add Product");
    }

    @FXML
    private void onSubmitClick() {
        String barcode = productTextField.getText().trim();
        if (barcode.isEmpty()) {
            showAlert();
            return;
        }

        productService.findByBarcode(barcode).ifPresentOrElse(product -> {
            productNameLabel.setText(product.getName());
            try {
                if (product.getImageUrl() != null) {
                    productImage.setImage(new Image(new FileInputStream(product.getImageUrl())));
                }
            } catch (FileNotFoundException e) {
                productImage.setImage(null);
            }
        }, () -> {
            productNameLabel.setText("Product not found.");
            productImage.setImage(null);
        });
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Please enter a barcode.");
        alert.showAndWait();
    }
}
