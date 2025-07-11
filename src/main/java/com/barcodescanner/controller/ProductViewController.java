package com.barcodescanner.controller;

import com.barcodescanner.SceneManager;
import com.barcodescanner.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;


@Controller
public class ProductViewController {

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Label productBarcode;

    @FXML
    private Label productPrice;

    @FXML
    private Label productDescription;

    @FXML
    private Button sceneButton;

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
}
