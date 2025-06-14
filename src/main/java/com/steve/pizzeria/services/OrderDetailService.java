package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.OrderDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderDetailService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9092/orders_details"; // URL de tu API

    @Autowired
    public OrderDetailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Aquí puedes agregar métodos para interactuar con la API de pedidos

    public List<OrderDetailDto> getAllOrderDetails() {
        ResponseEntity<OrderDetailDto[]> response = restTemplate.getForEntity(API_BASE_URL, OrderDetailDto[].class);
        return List.of(response.getBody());
    }
}
