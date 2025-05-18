package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9091/products";

    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProductDto> getAllProducts() {
        ResponseEntity<ProductDto[]> response = restTemplate.getForEntity(API_BASE_URL, ProductDto[].class);
        return List.of(response.getBody());
    }

    public ProductDto saveProduct(ProductDto product) {
        // Crear un mapa o un nuevo objeto DTO con solo los datos que necesita el otro servicio
        Map<String, Object> productDataForApi = new HashMap<>();
        productDataForApi.put("name", product.getName());
        productDataForApi.put("description", product.getDescription());
        productDataForApi.put("price", product.getPrice());
        productDataForApi.put("imageUrl", product.getImageUrl()); // Envía la URL guardada
        productDataForApi.put("category", product.getCategory());
        productDataForApi.put("status", product.isStatus());
        // No incluyas el campo 'image' (MultipartFile)

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Ajusta el Content-Type según lo que espera la otra API
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(productDataForApi, headers);

        ResponseEntity<ProductDto> response = restTemplate.postForEntity(API_BASE_URL, requestEntity, ProductDto.class);
        return response.getBody();
    }
}