package com.barcodescanner.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.barcodescanner.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiService {

    private static final String BASE_URL = "http://localhost:8080/api/products";

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public ApiService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Product getProductByBarcode(String barcode) throws Exception {
        String url = BASE_URL + "/" + barcode;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 && !response.body().isBlank()) {
            return objectMapper.readValue(response.body(), Product.class);
        } else {
            throw new Exception("Product not found or error: " + response.statusCode());
        }
    }

    public Product addProduct(Product product) throws Exception {
        String json = objectMapper.writeValueAsString(product);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();

        if ((statusCode == 200 || statusCode == 201)) {
            return objectMapper.readValue(response.body(), Product.class);
        } else {
            return product;
        }
        }

    public void deleteProduct(String barcode) throws Exception {
        String url = BASE_URL + "/" + barcode;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new Exception("Product not found or error: " + response.statusCode());
        }
    }
    }




