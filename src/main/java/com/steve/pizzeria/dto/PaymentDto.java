package com.steve.pizzeria.dto;

public class PaymentDto {

    private Long id;
    private Long userId;
    private double amount;
    private String paymentMethod;
    private String status;

    // --- ¡Nuevos campos para los detalles de la tarjeta! ---
    // Estos nombres deben coincidir EXACTAMENTE con los atributos 'name' de tus inputs en el HTML
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
    private String cardType;      // "credito" o "debito"
    private String cardBrand;     // "Visa", "MasterCard", "American Express" (viene del input hidden de JS)
    private String cardHolderName; // Nombre completo en la tarjeta

    public PaymentDto() {
    }

    public PaymentDto(Long id, Long userId, double amount, String paymentMethod, String status, String cardNumber, String expiryMonth, String expiryYear, String cvv,
                      String cardType, String cardBrand, String cardHolderName) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;

        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
        this.cardType = cardType;
        this.cardBrand = cardBrand;
        this.cardHolderName = cardHolderName;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getExpiryMonth() {
        return expiryMonth;
    }
    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }
    public String getExpiryYear() {
        return expiryYear;
    }
    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }
    public String getCvv() {
        return cvv;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    public String getCardType() {
        return cardType;
    }
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    public String getCardBrand() {
        return cardBrand;
    }
    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }
    public String getCardHolderName() {
        return cardHolderName;
    }
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
}