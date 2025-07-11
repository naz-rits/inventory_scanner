package com.barcodescanner.services;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

import com.github.sarxos.webcam.Webcam;

@Component
public class BarcodeScanner {

    private final AtomicBoolean scanning = new AtomicBoolean(false);


    public void startScanning(BarcodeScanCallback callback) {
        if (scanning.get()) return;

        new Thread(() -> {
            try {
                Webcam webcam = Webcam.getDefault();
                webcam.open();

                scanning.set(true);

                while (scanning.get()) {
                    BufferedImage image = webcam.getImage();
                    if (image == null) continue;

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        Result result = new MultiFormatReader().decode(bitmap);
                        scanning.set(false);
                        callback.onBarcodeDetected(result.getText());
                        webcam.close();
                        break;
                    } catch (NotFoundException e) {
                        // No barcode found in this frame
                    }

                    Thread.sleep(100);
                }

            } catch (InterruptedException e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public interface BarcodeScanCallback {
        void onBarcodeDetected(String barcode);
        void onError(String message);
    }
}
