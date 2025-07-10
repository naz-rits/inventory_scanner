package com.barcodescanner.controller;

import com.barcodescanner.SceneManager;
import com.barcodescanner.model.Product;
import com.barcodescanner.services.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;




@Controller
public class AddProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private TextField productName;

    @FXML
    private TextField productBarcode;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productQuantity;

    @FXML
    private TextArea productDescription;

    @FXML
    private TextField productUrl;

    @FXML
    private Button addProduct;

    @FXML
    private Button inventoryButton;

    @FXML
    public void goToInventory(){
        sceneManager.switchScene("/view/Inventory.fxml", "Inventory");
    }

    @FXML
    public void initialize() {
        addProduct.setOnAction(e -> {
            try {
                Product product = new Product();
                product.setName(productName.getText().trim());
                product.setBarcode(productBarcode.getText().trim());
                product.setPrice(Double.parseDouble(productPrice.getText().trim()));
                product.setQuantity(Integer.parseInt(productQuantity.getText().trim()));
                product.setDescription(productDescription.getText().trim());
                product.setImageUrl(productUrl.getText().trim());

                productService.save(product);
                showAlert("Product saved successfully!");

                clearForm();

            } catch (Exception ex) {
                showAlert("Error: " + ex.getMessage());
            }
        });
    }


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearForm() {
        productName.clear();
        productBarcode.clear();
        productPrice.clear();
        productQuantity.clear();
        productDescription.clear();
        productUrl.clear();
    }
}
