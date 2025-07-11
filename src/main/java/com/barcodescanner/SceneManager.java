package com.barcodescanner;

import com.barcodescanner.controller.ProductViewController;
import com.barcodescanner.model.Product;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Data;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
public class SceneManager {

    private final ApplicationContext applicationContext;

    @Setter
    private Stage primaryStage;

    public SceneManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void switchScene(String fxmlPath, String title, Optional<Product> product) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            if (product.isPresent()) {
                ProductViewController productViewController = loader.getController();
                productViewController.productStage(product);
            }
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
