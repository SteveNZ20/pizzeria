package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.OrderDto;
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
public class OrderDetailService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9092/orders";
    //private final String API_BASE_URL = "http://192.168.18.17:9092/orders";
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailService.class);

    // Credentials for basic authentication
    private final String ORDER_API_USERNAME = "admin";
    private final String ORDER_API_PASSWORD = "admin123";

    @Autowired
    public OrderDetailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Helper method to create headers with basic authentication.
     */
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = ORDER_API_USERNAME + ":" + ORDER_API_PASSWORD;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Fetches all orders from the order API.
     */
    public List<OrderDto> getAllOrders() {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders());
            ResponseEntity<OrderDto[]> response = restTemplate.exchange(
                    API_BASE_URL,
                    HttpMethod.GET,
                    requestEntity,
                    OrderDto[].class
            );
            // It's safer to check if the body is not null before wrapping it in a list
            if (response.getBody() != null) {
                return List.of(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error fetching orders from the API (status {}): {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (Exception ex) {
            logger.error("Unexpected error fetching orders: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Saves a new order to the order API.
     */
    public OrderDto saveOrder(OrderDto order) {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<OrderDto> requestEntity = new HttpEntity<>(order, headers);
        try {
            ResponseEntity<OrderDto> response = restTemplate.postForEntity(
                    API_BASE_URL,
                    requestEntity,
                    OrderDto.class
            );
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error saving the order (status {}): {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return null;
        } catch (Exception ex) {
            logger.error("Unexpected error saving the order: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Updates the quantity of a specific order.
     */
    public boolean updateOrderQuantity(Long id, int newQuantity) {
        try {
            String url = API_BASE_URL + "/" + id;
            HttpHeaders headers = createAuthHeaders();
            // The request body should be a map with the 'quantity' field
            HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(Collections.singletonMap("quantity", newQuantity), headers);

            restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class
            );
            logger.info("Quantity updated for order with ID: {}", id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error updating quantity for order with ID {} (status {}): {}", id, ex.getStatusCode(), ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            logger.error("Unexpected error updating quantity for order with ID {}: {}", id, ex.getMessage());
            return false;
        }
    }
}