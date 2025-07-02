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

/**
 * Controlador responsable de gestionar las operaciones relacionadas a los pedidos (orders)
 * en la aplicación de la pizzería. Permite obtener la lista de pedidos y agregar nuevos pedidos.
 *
 * Utiliza librerías como:
 * - Apache Commons Lang (`StringUtils`) para validación de texto.
 * - Google Guava (`Ordering`) para ordenar listas de objetos.
 * - SLF4J con Logback para registrar logs.
 */
@Controller
public class CartController {

    /** Logger para registrar información del sistema (con SLF4J y Logback). */
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    /** Servicio que maneja la lógica de negocio relacionada a los pedidos. */
    @Autowired
    private OrderService orderService;

    /**
     * Maneja las solicitudes GET para la URL "/orders".
     * Obtiene todos los pedidos, filtra los inválidos (nombre vacío), los ordena de mayor a menor
     * por cantidad, calcula el total y los envía a la vista.
     *
     * @param model el modelo de Spring MVC que se pasa a la vista.
     * @return nombre de la vista HTML a renderizar.
     */
    @GetMapping("/orders")
    public String getOrders(Model model) {
        // Se obtiene una lista mutable de todos los pedidos
        List<OrderDto> orders = new ArrayList<>(orderService.getAllOrders());

        // Validación: se eliminan pedidos sin nombre usando Apache Commons Lang
        orders.removeIf(order -> StringUtils.isBlank(order.getProduct_name()));

        // Ordenamiento con Guava: de mayor a menor cantidad, lista inmutable
        orders = Ordering
                .from(Comparator.comparing(OrderDto::getQuantity))
                .reverse()
                .immutableSortedCopy(orders);

        // Registro con Logback: cantidad de pedidos y total
        logger.info("Se cargaron {} pedidos para mostrar en la vista.", orders.size());

        // Cálculo del precio total de los pedidos
        double total = orders.stream().mapToDouble(OrderDto::getPrice).sum();

        logger.info("Se cargaron {} pedidos para mostrar en la vista. Total: {}", orders.size(), total);

        // Datos para mostrar en la vista
        model.addAttribute("orders", orders);
        model.addAttribute("total", total);

        return "orders/orderIndex";
    }

    /**
     * Maneja solicitudes POST para agregar un nuevo pedido desde el cliente.
     * Los datos del pedido se envían como un JSON a una API REST mediante `RestTemplate`.
     *
     * @param product_id ID del producto.
     * @param product_name Nombre del producto.
     * @param price Precio del producto.
     * @param image URL o nombre de la imagen del producto.
     * @param model Modelo de Spring MVC para enviar datos a la vista.
     * @return Nombre de la vista a mostrar tras procesar el pedido.
     */
    @PostMapping("/orders/add")
    public String createOrder(@RequestParam int product_id,
                              @RequestParam String product_name,
                              @RequestParam double price,
                              @RequestParam String image,
                              Model model) {
        RestTemplate restTemplate = new RestTemplate();

        // Construcción del cuerpo de la petición (pedido)
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("user_id", 1); // fijo en 1 por simplicidad
        orderData.put("product_id", product_id);
        orderData.put("product_name", product_name);
        orderData.put("price", price);
        orderData.put("quantity", 1); // cantidad fija
        orderData.put("image", image);

        // Configuración de cabeceras
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Se construye la petición con headers y cuerpo
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(orderData, headers);

        // Se envía la petición POST a la API REST (cliente llama a servidor)
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:8080/orders", request, String.class
        );

        // Se añade un mensaje al modelo según si fue exitoso o no
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("message", "Pedido agregado correctamente");
        } else {
            model.addAttribute("message", "Error al agregar el pedido");
        }

        return "orders/orderIndex"; // vuelve a la vista del listado
    }

}