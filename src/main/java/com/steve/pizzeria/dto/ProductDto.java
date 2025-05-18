package com.steve.pizzeria.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * Data Transfer Object (DTO) para representar un producto en la pizzería.
 * Contiene información como nombre, descripción, precio, categoría e imagen.
 * Incluye un campo adicional para la carga de archivos (imagen) que no se persiste.
 *
 * Este DTO se utiliza en la capa de presentación para transportar datos
 * hacia y desde el frontend o las APIs.
 *
 * @author Franco
 * @version 1.0
 */
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private boolean status;

    /**
     * Campo utilizado para la carga de archivos (multipart), no persistente en base de datos.
     */
    private MultipartFile image;

    /**
     * Constructor vacío necesario para frameworks y deserialización.
     */
    public ProductDto() {
    }

    /**
     * Obtiene el ID del producto.
     *
     * @return ID del producto
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del producto.
     *
     * @param id ID del producto
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return nombre del producto
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param name nombre del producto
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la descripción del producto.
     *
     * @return descripción del producto
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del producto.
     *
     * @param description descripción del producto
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return precio del producto
     */
    public double getPrice() {
        return price;
    }

    /**
     * Establece el precio del producto.
     *
     * @param price precio del producto
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Obtiene la URL de la imagen del producto.
     *
     * @return URL de la imagen
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Establece la URL de la imagen del producto.
     *
     * @param imageUrl URL de la imagen
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Obtiene la categoría del producto.
     *
     * @return categoría del producto
     */
    public String getCategory() {
        return category;
    }

    /**
     * Establece la categoría del producto.
     *
     * @param category categoría del producto
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Verifica si el producto está activo o habilitado.
     *
     * @return true si está activo; false si está inactivo
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Establece el estado del producto.
     *
     * @param status estado del producto (true: activo, false: inactivo)
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Obtiene el archivo de imagen cargado (no persistente).
     *
     * @return imagen cargada como MultipartFile
     */
    public MultipartFile getImage() {
        return image;
    }

    /**
     * Establece el archivo de imagen cargado.
     *
     * @param image imagen cargada como MultipartFile
     */
    public void setImage(MultipartFile image) {
        this.image = image;
    }
}