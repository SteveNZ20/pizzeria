package com.steve.pizzeria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Date;

public class OrderDto {

    private Long id;
    private double amount;
    private String paymentMethod;
    private String observations;
    private String userId;
    private String userName;
    private String userAddress;
    private String userPhone;
    private Date fechaCreacion;
    private String status;

    // Constructor vacío
    public OrderDto() {
    }

    // Constructor con todos los campos
    public OrderDto(Long id, double amount, String paymentMethod, String observations, String userId, String userName, String userAddress, String userPhone, Date fechaCreacion, String status) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.observations = observations;
        this.userId = userId;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhone = userPhone;
        this.fechaCreacion = fechaCreacion;
        this.status = status;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getObservations() {
        return observations;
    }
    public void setObservations(String observations) {
        this.observations = observations;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return fechaCreacion != null ? fechaCreacion.toInstant().atOffset(OffsetDateTime.now().getOffset()) : null;
    }
}