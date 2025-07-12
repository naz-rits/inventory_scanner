package com.barcodescanner.controller;

import com.barcodescanner.SceneManager;
import com.barcodescanner.api.ApiService;
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
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class InventoryController {

    private final ApiService apiService = new ApiService();

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
                    try {
                        onSubmitClick();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
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
                        try {
                            onSubmitClick(); // optionally auto-submit
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
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
    private void onSubmitClick() throws Exception {
        String barcode = productTextField.getText().trim();
        if (barcode.isEmpty()) {
            showAlert("Please Enter Barcode");
            return;
        }

        try {
            Product product = apiService.getProductByBarcode(barcode);
            sceneManager.switchScene("/view/ProductView.fxml", "Product View", Optional.of(product));
        }
        catch (Exception ex) {
            productNameLabel.setText("Product not found.");
            productImage.setImage(null);
            ex.printStackTrace();
        }

    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }


}
