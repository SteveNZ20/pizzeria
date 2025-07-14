package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;

@Service
public class CartService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9092/orders"; // URL de tu API
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    public CartService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Aquí puedes agregar métodos para interactuar con la API de pedidos

    public List<CartDto> getAllOrders() {
        try {
            ResponseEntity<CartDto[]> response = restTemplate.getForEntity(API_BASE_URL, CartDto[].class);
            return List.of(response.getBody());
        } catch (Exception ex) {
            logger.error("Error al obtener los pedidos de la API: {}", ex.getMessage());
            return List.of();
        }
    }

    public CartDto saveOrder(CartDto order) {
        return restTemplate.postForObject(API_BASE_URL, order, CartDto.class);
    }

    public void updateOrderQuantity(Long id, int newQuantity) {
        try {
            String url = API_BASE_URL + "/" + id;
            Map<String, Integer> request = new HashMap<>();
            request.put("quantity", newQuantity);
            restTemplate.put(url, request);
            logger.info("Cantidad actualizada para el pedido con ID: {}", id);
        } catch (Exception ex) {
            logger.error("Error al actualizar la cantidad del pedido con ID {}: {}", id, ex.getMessage());
        }
    }

    // Nuevo método para obtener órdenes por User ID
    public List<CartDto> getAllOrdersByUserId(Long userId) {
        // Asume que tu API de backend tiene un endpoint como GET /carts/user/{userId}
        // o GET /orders/user/{userId}
        String url = API_BASE_URL + "/user/" + userId;
        try {
            ResponseEntity<CartDto[]> response = restTemplate.getForEntity(url, CartDto[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                // Manejar el caso de que no haya órdenes o haya un error
                // Podrías lanzar una excepción o devolver una lista vacía
                return Collections.emptyList();
            }
        } catch (Exception e) {
            // Loguear el error y devolver una lista vacía o lanzar una excepción
            System.err.println("Error al obtener órdenes por userId " + userId + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
