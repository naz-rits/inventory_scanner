package com.barcodescanner;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BarcodeScannerApplication {

    public static void main(String[] args) {
        Application.launch(InventoryScannerApp.class, args);
    }

}
