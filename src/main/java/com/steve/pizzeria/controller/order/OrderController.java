package com.steve.pizzeria.controller.order;

import com.google.common.collect.Ordering;
import com.steve.pizzeria.dto.OrderDto;
import com.steve.pizzeria.services.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String getOrders(Model model) {
        List<OrderDto> orders = new ArrayList<>(orderService.getAllOrders()); //  mutable

        // Apache Commons Lang: validación
        orders.removeIf(order -> StringUtils.isBlank(order.getProduct_name()));

        // Guava: ordenar por cantidad de mayor a menor
        orders = Ordering
                .from(Comparator.comparing(OrderDto::getQuantity))
                .reverse()
                .immutableSortedCopy(orders); // esta copia ya no se modifica, lo cual está bien

        logger.info("Se cargaron {} pedidos para mostrar en la vista.", orders.size());

        // Calcular total
        double total = orders.stream().mapToDouble(OrderDto::getPrice).sum();

        logger.info("Se cargaron {} pedidos para mostrar en la vista. Total: {}", orders.size(), total);

        model.addAttribute("orders", orders);
        model.addAttribute("total", total);
        return "orders/orderIndex";
    }

    @PostMapping("/orders/add")
    public String createOrder(@RequestParam int product_id,
                              @RequestParam String product_name,
                              @RequestParam double price,
                              @RequestParam String image,
                              Model model) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("user_id", 1);
        orderData.put("product_id", product_id);
        orderData.put("product_name", product_name);
        orderData.put("price", price);
        orderData.put("quantity", 1);
        orderData.put("image", image);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(orderData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/orders", request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("message", "Pedido agregado correctamente");
        } else {
            model.addAttribute("message", "Error al agregar el pedido");
        }

        return "orders/orderIndex"; // o la vista que desees mostrar
    }

}
