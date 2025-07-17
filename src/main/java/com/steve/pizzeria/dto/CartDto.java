package com.steve.pizzeria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartDto {

    private Long id;
    // userId ya es Long, y con los getters/setters correctos, @JsonProperty no es estrictamente necesario aquí.
    private Long userId;

    // Cambiado a Long para coincidir con OrderDetailsModel del backend
    private Long productId;

    private String productName;
    private double price;
    private int quantity;
    private String image;

    /**
     * Constructor vacío para frameworks o inicialización manual.
     */
    public CartDto() {}

    /**
     * Constructor con parámetros principales.
     *
     * @param id ID del pedido
     * @param userId ID del usuario que realizó el pedido
     * @param productId ID del producto pedido
     * @param productName nombre del producto pedido
     * @param price precio del producto
     * @param quantity cantidad del producto pedido
     * @param image URL de la imagen del producto
     */
    public CartDto(Long id, Long userId, Long productId, String productName, double price, int quantity, String image) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    // Getters y Setters Estándar (CamelCase)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; } // Corregido de getUser_id()
    public void setUserId(Long userId) { this.userId = userId; } // Corregido de setUser_id()

    public Long getProductId() { return productId; } // Corregido de getProduct_id() y tipo a Long
    public void setProductId(Long productId) { this.productId = productId; } // Corregido de setProduct_id() y tipo a Long

    public String getProductName() { return productName; } // Corregido de getProduct_name() y eliminado duplicado
    public void setProductName(String productName) { this.productName = productName; } // Corregido de setProduct_name() y eliminado duplicado

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}