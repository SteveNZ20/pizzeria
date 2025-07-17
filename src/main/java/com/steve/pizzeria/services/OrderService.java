package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9094/delivery";
    //private final String API_BASE_URL = "http://192.168.18.17:9094/delivery";
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    // Credenciales para la autenticación básica del backend de pedidos
    private final String ORDER_API_USERNAME = "admin";
    private final String ORDER_API_PASSWORD = "admin123";

    @Autowired
    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = ORDER_API_USERNAME + ":" + ORDER_API_PASSWORD;
        byte[] encodedAuth = java.util.Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Obtiene todos los pedidos de la API.
     * @return Una lista de OrderDto.
     */
    public List<OrderDto> getAllOrders() {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders());
        try {
            ResponseEntity<List<OrderDto>> response = restTemplate.exchange(
                    API_BASE_URL,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<OrderDto>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            logger.error("Error al obtener los pedidos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Crea un nuevo pedido en la API.
     * @param orderDto El objeto OrderDto a crear.
     * @return Un Optional que contiene el OrderDto creado si la operación fue exitosa.
     */
    public Optional<OrderDto> createOrder(OrderDto orderDto) {
        HttpEntity<OrderDto> entity = new HttpEntity<>(orderDto, createAuthHeaders());
        try {
            ResponseEntity<OrderDto> response = restTemplate.postForEntity(API_BASE_URL, entity, OrderDto.class);
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Error al crear el pedido: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Obtiene un pedido por su ID.
     * @param id El ID del pedido a buscar.
     * @return Un Optional que contiene el OrderDto si se encuentra.
     */
    public Optional<OrderDto> getOrderById(Long id) {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders());
        String url = UriComponentsBuilder.fromHttpUrl(API_BASE_URL).path("/{id}").buildAndExpand(id).toUriString();
        try {
            ResponseEntity<OrderDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    OrderDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Pedido con ID {} no encontrado.", id);
            return Optional.empty();
        } catch (HttpClientErrorException e) {
            logger.error("Error al obtener el pedido: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Actualiza el estado de un pedido.
     * @param id El ID del pedido a actualizar.
     * @param newStatus El nuevo estado.
     * @return Un Optional que contiene el OrderDto actualizado si la operación fue exitosa.
     */
    public Optional<OrderDto> updateOrderStatus(Long id, String newStatus) {
        HttpHeaders headers = createAuthHeaders();
        String requestBody = String.format("{\"status\":\"%s\"}", newStatus);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        String url = UriComponentsBuilder.fromHttpUrl(API_BASE_URL).path("/{id}/status").buildAndExpand(id).toUriString();
        try {
            ResponseEntity<OrderDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    OrderDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Error al actualizar el estado del pedido: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Elimina un pedido por su ID.
     * @param id El ID del pedido a eliminar.
     * @return Verdadero si el pedido fue eliminado con éxito, falso en caso contrario.
     */
    public boolean deleteOrder(Long id) {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders());
        String url = UriComponentsBuilder.fromHttpUrl(API_BASE_URL).path("/{id}").buildAndExpand(id).toUriString();
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("No se pudo eliminar el pedido con ID {} porque no fue encontrado.", id);
            return false;
        } catch (HttpClientErrorException e) {
            logger.error("Error al intentar eliminar el pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca y retorna una lista de pedidos por el ID de usuario.
     * @param userId El ID del usuario para el que se buscan los pedidos.
     * @return Una lista de OrderDto.
     */
    public List<OrderDto> getOrdersByUserId(String userId) {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // **CORRECCIÓN:** Se agrega la ruta "/search" para que coincida con el endpoint del backend.
        String url = UriComponentsBuilder.fromHttpUrl(API_BASE_URL)
                .path("/search")
                .queryParam("userId", userId)
                .toUriString();

        try {
            ResponseEntity<List<OrderDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<OrderDto>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            logger.error("Error al obtener los pedidos del usuario {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }
}