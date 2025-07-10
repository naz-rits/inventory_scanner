package com.barcodescanner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class InventoryController {

    public ConfigurableApplicationContext applicationContext;

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
}
