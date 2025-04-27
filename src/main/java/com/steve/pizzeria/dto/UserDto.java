package com.steve.pizzeria.dto;

public class UserDto {

    private String username;
    private String email;
    private String password;
    private String usertype;
    private boolean status;

    // Constructor vacío
    public UserDto() {
    }

    // Constructor con parámetros
    public UserDto(String username, String email, String password, String usertype, boolean status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.usertype = usertype;
        this.status = status;
    }

    // Getters y Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private Long id; // Asegúrate de que este campo exista

    // Otros campos y métodos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}