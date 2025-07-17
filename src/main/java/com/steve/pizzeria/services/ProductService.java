package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9091/products";
    //private final String API_BASE_URL = "http://192.168.18.17:9091/products";
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    // Credenciales para la autenticación básica
    private final String PRODUCT_API_USERNAME = "admin";
    private final String PRODUCT_API_PASSWORD = "admin123";

    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Método auxiliar para crear los encabezados con autenticación básica.
     */
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = PRODUCT_API_USERNAME + ":" + PRODUCT_API_PASSWORD;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Obtiene todos los productos del servicio de productos.
     */
    public List<ProductDto> getAllProducts() {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders());
            ResponseEntity<ProductDto[]> response = restTemplate.exchange(
                    API_BASE_URL,
                    HttpMethod.GET,
                    requestEntity,
                    ProductDto[].class
            );
            if (response.getBody() != null) {
                return List.of(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al obtener los productos (status {}): {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (Exception ex) {
            logger.error("Error inesperado al obtener los productos: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Guarda un nuevo producto en el servicio de productos.
     */
    public ProductDto saveProduct(ProductDto product) {
        HttpHeaders headers = createAuthHeaders();
        // Construye el cuerpo de la solicitud con los campos necesarios
        Map<String, Object> productDataForApi = Map.of(
                "name", product.getName(),
                "description", product.getDescription(),
                "price", product.getPrice(),
                "imageUrl", product.getImageUrl(),
                "category", product.getCategory(),
                "status", product.isStatus()
        );
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(productDataForApi, headers);
        try {
            ResponseEntity<ProductDto> response = restTemplate.postForEntity(
                    API_BASE_URL,
                    requestEntity,
                    ProductDto.class
            );
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al guardar el producto (status {}): {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return null;
        } catch (Exception ex) {
            logger.error("Error inesperado al guardar el producto: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Elimina un producto por su ID.
     */
    public boolean deleteProduct(Long id) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders());
            restTemplate.exchange(
                    API_BASE_URL + "/" + id,
                    HttpMethod.DELETE,
                    requestEntity,
                    Void.class
            );
            logger.info("Producto con ID {} eliminado exitosamente.", id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al eliminar el producto con ID {} (status {}): {}", id, ex.getStatusCode(), ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            logger.error("Error inesperado al eliminar el producto con ID {}: {}", id, ex.getMessage());
            return false;
        }
    }

    /**
     * Actualiza un producto existente.
     */
    public boolean updateProduct(ProductDto productDto) {
        try {
            String url = API_BASE_URL + "/" + productDto.getId();
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<ProductDto> requestEntity = new HttpEntity<>(productDto, headers);

            restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class
            );
            logger.info("Producto con ID {} actualizado exitosamente.", productDto.getId());
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al actualizar el producto con ID {} (status {}): {}", productDto.getId(), ex.getStatusCode(), ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            logger.error("Error inesperado al actualizar el producto con ID {}: {}", productDto.getId(), ex.getMessage());
            return false;
        }
    }

    /**
     * Obtiene un producto por su ID.
     */
    public ProductDto getProductById(Long id) {
        try {
            String url = API_BASE_URL + "/" + id;
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders());
            ResponseEntity<ProductDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    ProductDto.class
            );
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("Producto con ID {} no encontrado.", id);
                return null;
            }
            logger.error("Error al obtener el producto con ID {} (status {}): {}", id, ex.getStatusCode(), ex.getResponseBodyAsString());
            return null;
        } catch (Exception ex) {
            logger.error("Error inesperado al obtener el producto con ID {}: {}", id, ex.getMessage());
            return null;
        }
    }
}