package com.steve.pizzeria.dto;

import java.util.Date;

/**
 * Representa un Data Transfer Object (DTO) para el usuario.
 * Contiene información personal, de contacto y metadatos de auditoría.
 * Este DTO se utiliza para transportar datos entre capas sin exponer entidades internas.
 *
 * @author Franco
 * @version 1.0
 */
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String usertype;
    private boolean status;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String distrito;
    private String direccion;
    private String referencia;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Integer meses;

    /**
     * Constructor vacío para frameworks o inicialización manual.
     */
    public UserDto() {
    }

    /**
     * Constructor con parámetros principales.
     *
     * @param username nombre de usuario
     * @param email correo electrónico
     * @param password contraseña del usuario
     * @param usertype tipo de usuario (por ejemplo: "user", "admin")
     * @param status estado del usuario (activo o inactivo)
     */
    public UserDto(String username, String email, String password, String usertype, boolean status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.usertype = usertype;
        this.status = status;
    }

    /** @return ID del usuario */
    public Long getId() {
        return id;
    }

    /** @param id ID del usuario */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return nombre de usuario */
    public String getUsername() {
        return username;
    }

    /** @param username nombre de usuario */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return correo electrónico */
    public String getEmail() {
        return email;
    }

    /** @param email correo electrónico */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return contraseña del usuario */
    public String getPassword() {
        return password;
    }

    /** @param password contraseña del usuario */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return tipo de usuario */
    public String getUsertype() {
        return usertype;
    }

    /** @param usertype tipo de usuario */
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    /** @return estado del usuario */
    public boolean isStatus() {
        return status;
    }

    /** @param status estado del usuario */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /** @return nombre del usuario */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre nombre del usuario */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return apellido del usuario */
    public String getApellido() {
        return apellido;
    }

    /** @param apellido apellido del usuario */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** @return DNI del usuario */
    public String getDni() {
        return dni;
    }

    /** @param dni DNI del usuario */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /** @return teléfono del usuario */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono teléfono del usuario */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return distrito del usuario */
    public String getDistrito() {
        return distrito;
    }

    /** @param distrito distrito del usuario */
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    /** @return dirección del usuario */
    public String getDireccion() {
        return direccion;
    }

    /** @param direccion dirección del usuario */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /** @return referencia adicional */
    public String getReferencia() {
        return referencia;
    }

    /** @param referencia referencia adicional */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /** @return fecha de creación del registro */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /** @param fechaCreacion fecha de creación del registro */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /** @return fecha de la última actualización */
    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    /** @param fechaActualizacion fecha de la última actualización */
    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /** @return meses asociados al usuario (por ejemplo: membresía, antigüedad) */
    public Integer getMeses() {
        return meses;
    }

    /** @param meses meses asociados al usuario */
    public void setMeses(Integer meses) {
        this.meses = meses;
    }
}