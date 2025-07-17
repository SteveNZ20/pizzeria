package com.steve.pizzeria.controller.admin;

import com.steve.pizzeria.dto.OrderDto;
import com.steve.pizzeria.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/orders")
public class OrdersController {

    private final OrderService orderService;

    @Autowired
    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String viewPendingOrders(Model model) {
        List<OrderDto> allOrders = orderService.getAllOrders();

        List<OrderDto> pendingOrders = allOrders.stream()
                .filter(order -> !("success".equalsIgnoreCase(order.getStatus())))
                .collect(Collectors.toList());

        model.addAttribute("orders", pendingOrders);
        return "admin/orders/orders";
    }

    @PostMapping("/update_status")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId,
                                    @RequestParam("newStatus") String newStatus,
                                    RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(orderId, newStatus);
        redirectAttributes.addFlashAttribute("message", "Estado del pedido actualizado correctamente a '" + newStatus + "'.");
        return "redirect:/admin/orders";
    }

    // Nuevo método para mostrar pedidos completados
    @GetMapping("/completed")
    public String viewCompletedOrders(Model model) {
        // Obtiene todos los pedidos del servicio
        List<OrderDto> allOrders = orderService.getAllOrders();

        // Filtra los pedidos para mostrar solo aquellos que tienen el estado 'success'
        List<OrderDto> completedOrders = allOrders.stream()
                .filter(order -> "success".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.toList());

        double totalCompletedAmount = completedOrders.stream()
                .mapToDouble(OrderDto::getAmount)
                .sum();

        model.addAttribute("orders", completedOrders);
        model.addAttribute("totalCompletedAmount", totalCompletedAmount);

        return "admin/orders/ordersComplete";
    }
}