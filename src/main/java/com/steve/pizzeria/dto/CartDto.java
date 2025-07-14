package com.steve.pizzeria.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CartDto {

    private Long id;
    @JsonProperty("userId")
    private int userId;

    @JsonProperty("productId")
    private int productId;

    @JsonProperty("productName")
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
    public CartDto(Long id, int userId, int productId, String productName, double price, int quantity, String image) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    // Getters y Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public int getUser_id() {return userId;}
    public void setUser_id(int user_id) {this.userId = user_id;}
    public int getProduct_id() {return productId;}
    public void setProduct_id(int product_id) {this.productId = product_id;}
    public String getProduct_name() {return productName;}
    public void setProduct_name(String product_name) {this.productName = product_name;}
    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}
    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}
    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public CharSequence getProductName() {
        return productName;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public void setUserId(int i) {
        this.userId = i;
    }
}
