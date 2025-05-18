package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9090/users"; // URL de tu API
    private final String UPDATE_STATUS_ENDPOINT = API_BASE_URL + "/update-status";

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<UserDto> getAllUsers() {
        ResponseEntity<UserDto[]> response = restTemplate.getForEntity(API_BASE_URL, UserDto[].class);
        return Arrays.asList(response.getBody());
    }

    public UserDto saveUser(UserDto user) {
        return restTemplate.postForObject(API_BASE_URL, user, UserDto.class);
    }

    // Método para obtener los detalles de un usuario por nombre de usuario
    public UserDto getUserDetailsByUsername(String username) {
        String url = API_BASE_URL + "/username/" + username;
        UserDto userDetails = restTemplate.getForObject(url, UserDto.class);
        return userDetails;
    }

//    public List<UserDto> actualizarEstadosUsuarios() {
//        ResponseEntity<UserDto[]> response = restTemplate.postForEntity(UPDATE_STATUS_ENDPOINT, null, UserDto[].class);
//        return Arrays.asList(response.getBody());
//    }

    // Método para habilitar un usuario
    public void enableUser(Long userId) {
        String url = API_BASE_URL + "/enable/" + userId;
        restTemplate.postForObject(url, null, String.class); // Enviar la solicitud POST
    }

    // Método para deshabilitar un usuario
    public void disableUser(Long userId) {
        String url = API_BASE_URL + "/disable/" + userId;
        restTemplate.postForObject(url, null, String.class); // <-- espera un String
    }

    public List<UserDto> getUsersByType(String userType) {
        String url = API_BASE_URL + "/type/" + userType;
        ResponseEntity<UserDto[]> response = restTemplate.getForEntity(url, UserDto[].class);
        return Arrays.asList(response.getBody());
    }

    public UserDto updateUser(UserDto userDto) {
        String url = API_BASE_URL + "/" + userDto.getId(); // usa el ID, no el username
        restTemplate.put(url, userDto);
        return userDto;
    }
}