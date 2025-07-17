package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.CartDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@Service
public class CartService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9092/orders";
    // private final String API_BASE_URL = "http://192.168.18.17:9092/orders";
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    // Credenciales para la autenticación básica
    private final String CART_API_USERNAME = "admin";
    private final String CART_API_PASSWORD = "admin123";

    @Autowired
    public CartService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Método auxiliar para crear los encabezados con autenticación básica.
     */
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = CART_API_USERNAME + ":" + CART_API_PASSWORD;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Obtiene todos los pedidos del servicio de órdenes.
     */
    public List<CartDto> getAllOrders() {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders());
            ResponseEntity<CartDto[]> response = restTemplate.exchange(
                    API_BASE_URL,
                    HttpMethod.GET,
                    requestEntity,
                    CartDto[].class
            );
            return List.of(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al obtener los pedidos (status {}): {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (Exception ex) {
            logger.error("Error inesperado al obtener los pedidos: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Guarda un nuevo pedido en el servicio de órdenes.
     */
    public CartDto saveOrder(CartDto order) {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<CartDto> requestEntity = new HttpEntity<>(order, headers);
        try {
            ResponseEntity<CartDto> response = restTemplate.postForEntity(
                    API_BASE_URL,
                    requestEntity,
                    CartDto.class
            );
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al guardar el pedido (status {}): {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return null;
        } catch (Exception ex) {
            logger.error("Error inesperado al guardar el pedido: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Elimina un pedido por su ID.
     */
    public boolean deleteOrder(Long id) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders());
            restTemplate.exchange(
                    API_BASE_URL + "/" + id,
                    HttpMethod.DELETE,
                    requestEntity,
                    Void.class
            );
            logger.info("Pedido con ID {} eliminado exitosamente.", id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al eliminar el pedido con ID {} (status {}): {}", id, ex.getStatusCode(), ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            logger.error("Error inesperado al eliminar el pedido con ID {}: {}", id, ex.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la cantidad de un pedido por su ID.
     * @param id El ID del pedido a actualizar.
     * @param newQuantity La nueva cantidad.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean updateOrderQuantity(Long id, int newQuantity) {
        try {
            String url = API_BASE_URL + "/" + id;
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(Collections.singletonMap("quantity", newQuantity), headers);

            restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class
            );
            logger.info("Cantidad actualizada para el pedido con ID: {}", id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al actualizar la cantidad del pedido con ID {} (status {}): {}", id, ex.getStatusCode(), ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            logger.error("Error inesperado al actualizar la cantidad del pedido con ID {}: {}", id, ex.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los pedidos asociados a un ID de usuario.
     */
    public List<CartDto> getAllOrdersByUserId(Long userId) {
        try {
            String url = API_BASE_URL + "/user/" + userId;
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders());
            ResponseEntity<CartDto[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    CartDto[].class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return List.of(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al obtener órdenes por userId {} (status {}): {}", userId, ex.getStatusCode(), ex.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error inesperado al obtener órdenes por userId {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }
}