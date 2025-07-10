package com.barcodescanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class InventoryScannerApp extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(BarcodeScannerApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Inventory.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        // Inject primaryStage into SceneManager
        SceneManager sceneManager = context.getBean(SceneManager.class);
        sceneManager.setPrimaryStage(primaryStage);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Inventory Scanner");
        primaryStage.show();
    }

    @Override
    public void stop() {
        context.close();
    }
}
