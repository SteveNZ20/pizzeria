package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.PaymentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Base64; // Para codificar las credenciales
import java.util.List;

@Service
public class PaymentService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9093/payments";
    //private final String API_BASE_URL = "http://192.168.18.17:9093/payments";
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    // Credenciales para la autenticación básica del backend de pagos
    private final String PAYMENT_API_USERNAME = "admin"; // EL USUARIO CONFIGURADO EN LA API DE PAGOS
    private final String PAYMENT_API_PASSWORD = "admin123"; // LA CONTRASEÑA CONFIGURADA EN LA API DE PAGOS

    @Autowired
    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Método auxiliar para crear los encabezados con autenticación básica
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        // Codifica las credenciales en Base64
        String auth = PAYMENT_API_USERNAME + ":" + PAYMENT_API_PASSWORD;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON); // Asegura que el tipo de contenido también se establezca
        return headers;
    }

    public List<PaymentDto> getAllPayments() {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders()); // Incluye los encabezados de auth
            ResponseEntity<PaymentDto[]> response = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, requestEntity, PaymentDto[].class);
            return List.of(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al obtener los pagos de la API (status {}): {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return List.of();
        } catch (Exception ex) {
            logger.error("Error inesperado al obtener los pagos: {}", ex.getMessage());
            return List.of();
        }
    }

    public PaymentDto savePayment(PaymentDto payment) {
        HttpHeaders headers = createAuthHeaders(); // Usa los encabezados de autenticación
        HttpEntity<PaymentDto> requestEntity = new HttpEntity<>(payment, headers);

        try {
            ResponseEntity<PaymentDto> response = restTemplate.postForEntity(API_BASE_URL, requestEntity, PaymentDto.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Logs más detallados para errores HTTP
            logger.error("Error al guardar el pago (status {}): {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return null;
        } catch (Exception ex) {
            logger.error("Error inesperado al guardar el pago: {}", ex.getMessage());
            return null;
        }
    }

    public PaymentDto getPaymentById(Long id) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders()); // Incluye los encabezados de auth
            ResponseEntity<PaymentDto> response = restTemplate.exchange(API_BASE_URL + "/" + id, HttpMethod.GET, requestEntity, PaymentDto.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al obtener el pago con ID {} (status {}): {}", id, ex.getStatusCode(), ex.getResponseBodyAsString());
            return null;
        } catch (Exception ex) {
            logger.error("Error inesperado al obtener el pago con ID {}: {}", id, ex.getMessage());
            return null;
        }
    }

    public boolean deletePayment(Long id) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(createAuthHeaders());
            restTemplate.exchange(API_BASE_URL + "/" + id, HttpMethod.DELETE, requestEntity, Void.class);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al eliminar el pago con ID {} (status {}): {}", id, ex.getStatusCode(), ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            logger.error("Error inesperado al eliminar el pago con ID {}: {}", id, ex.getMessage());
            return false;
        }
    }

    public PaymentDto updatePaymentStatus(Long id, String status) {
        try {
            String url = API_BASE_URL + "/" + id + "/status";
            HttpHeaders headers = createAuthHeaders(); // Usa los encabezados de autenticación
            HttpEntity<String> requestEntity = new HttpEntity<>(status, headers);
            ResponseEntity<PaymentDto> response = restTemplate.postForEntity(url, requestEntity, PaymentDto.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logger.error("Error al actualizar el estado del pago con ID {} (status {}): {}", id, ex.getStatusCode(), ex.getResponseBodyAsString());
            return null;
        } catch (Exception ex) {
            logger.error("Error inesperado al actualizar el estado del pago con ID {}: {}", id, ex.getMessage());
            return null;
        }
    }
}