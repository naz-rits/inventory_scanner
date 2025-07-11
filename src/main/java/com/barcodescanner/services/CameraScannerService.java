package com.barcodescanner.services;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CameraScannerService {

    private Webcam webcam;
    private final AtomicBoolean running = new AtomicBoolean(false);


    public void startCamera(ImageView imageView, BarcodeListener listener) {
        if (running.get()) return;

        webcam = Webcam.getDefault();
        webcam.open();
        running.set(true);

        Thread scannerThread = new Thread(() -> {
            long lastDetectedTime = System.currentTimeMillis();
            long timeout = 10_000; // 10 seconds timeout for example

            while (running.get()) {
                BufferedImage image = webcam.getImage();
                if (image != null) {
                    Platform.runLater(() -> imageView.setImage(SwingFXUtils.toFXImage(image, null)));

                    String barcode = decodeBarcode(image);
                    if (barcode != null) {
                        running.set(false);
                        listener.onBarcodeDetected(barcode);
                        break;
                    }
                }

                // Check if timeout exceeded
                if (System.currentTimeMillis() - lastDetectedTime > timeout) {
                    Platform.runLater(() -> System.out.println("Camera is active"));
                    lastDetectedTime = System.currentTimeMillis(); // Reset to avoid spamming
                }

                try {
                    Thread.sleep(200); // Limit CPU usage
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        scannerThread.setDaemon(true);
        scannerThread.start();
    }

    public void stopCamera() {
        running.set(false);
        if (webcam != null) webcam.close();
    }

    public String decodeBarcode(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(
                BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.UPC_A, BarcodeFormat.CODE_128
        ));
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        try {
            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }


    @PreDestroy
    public void cleanup() {
        stopCamera();
    }

    // Functional interface
    public interface BarcodeListener {
        void onBarcodeDetected(String barcode);

    }
}
