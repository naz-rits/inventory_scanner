package com.barcodescanner.controller;

import com.barcodescanner.SceneManager;
import com.barcodescanner.model.Product;
import com.barcodescanner.services.BarcodeScanner;
import com.barcodescanner.services.CameraScannerService;
import com.barcodescanner.services.ProductService;
import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
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
import org.springframework.stereotype.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

@Controller
public class InventoryController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SceneManager sceneManager;

    @Autowired
    private BarcodeScanner barcodeScanner;

    @Autowired
    private CameraScannerService cameraScannerService;

    private Webcam webcam;
    private boolean scanning = false;

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
    private Button scanWithCamera;

    @FXML
    public void initialize() {
        scanWithCamera.setOnAction(e -> {
            cameraScannerService.startCamera(productImage, barcode -> {
                Platform.runLater(() -> {
                    productTextField.setText(barcode);
                    onSubmitClick();
                });
            });
        });
    }

    @FXML
    public void scanCamera(ActionEvent actionEvent) {
        scanWithCamera.setOnAction(e -> {
            barcodeScanner.startScanning(new BarcodeScanner.BarcodeScanCallback() {
                @Override
                public void onBarcodeDetected(String barcode) {
                    // update on JavaFX thread
                    Platform.runLater(() -> {
                        productTextField.setText(barcode);
                        onSubmitClick(); // optionally auto-submit
                    });
                }

                @Override
                public void onError(String message) {
                    Platform.runLater(() ->
                            showAlert("Scanner Error: " + message)
                    );
                }
            });
        });
    }
    @FXML
    private void handleGoToAddProduct() {
        cameraScannerService.stopCamera();
        sceneManager.switchScene("/view/AddProduct.fxml", "Add Product", Optional.empty());
    }

    @FXML
    private void onSubmitClick() {
        String barcode = productTextField.getText().trim();
        if (barcode.isEmpty()) {
            showAlert("Please Enter Barcode");
            return;
        }

        productService.findByBarcode(barcode).ifPresentOrElse(product -> {
            sceneManager.switchScene("/view/ProductView.fxml", "Product View", Optional.of(product));
        }, () -> {
            productNameLabel.setText("Product not found.");
            productImage.setImage(null);
        });
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }


}
