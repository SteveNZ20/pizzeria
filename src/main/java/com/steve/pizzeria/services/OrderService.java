package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9092/orders"; // URL de tu API
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Aquí puedes agregar métodos para interactuar con la API de pedidos

    public List<OrderDto> getAllOrders() {
        try {
            ResponseEntity<OrderDto[]> response = restTemplate.getForEntity(API_BASE_URL, OrderDto[].class);
            return List.of(response.getBody());
        } catch (Exception ex) {
            logger.error("Error al obtener los pedidos de la API: {}", ex.getMessage());
            return List.of();
        }
    }

    public OrderDto saveOrder(OrderDto order) {
        return restTemplate.postForObject(API_BASE_URL, order, OrderDto.class);
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
}
