package com.barcodescanner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Service;

@Service
public class InventoryController {

    @FXML
    private Label lblInventory;

    @FXML
    private TextField textField;

    @FXML
    protected void onAction() {
        lblInventory.textProperty().setValue(textField.getText());
    }
}
