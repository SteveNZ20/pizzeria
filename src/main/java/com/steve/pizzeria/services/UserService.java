package com.steve.pizzeria.services;

import com.steve.pizzeria.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "http://localhost:9090/users";
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

    public UserDto getUserDetailsByUsername(String username) {
        String url = API_BASE_URL + "/username/" + username;
        return restTemplate.getForObject(url, UserDto.class);
    }

    public void enableUser(Long userId) {
        String url = API_BASE_URL + "/enable/" + userId;
        restTemplate.postForObject(url, null, String.class);
    }

    public void disableUser(Long userId) {
        String url = API_BASE_URL + "/disable/" + userId;
        restTemplate.postForObject(url, null, String.class);
    }

    public List<UserDto> getUsersByType(String userType) {
        String url = API_BASE_URL + "/type/" + userType;
        ResponseEntity<UserDto[]> response = restTemplate.getForEntity(url, UserDto[].class);
        return Arrays.asList(response.getBody());
    }

    public UserDto updateUser(UserDto userDto) {
        String url = API_BASE_URL + "/" + userDto.getId();
        restTemplate.put(url, userDto);
        return userDto;
    }

     public List<UserDto> actualizarEstadosUsuarios() {
         ResponseEntity<UserDto[]> response = restTemplate.postForEntity(UPDATE_STATUS_ENDPOINT, null, UserDto[].class);
         return Arrays.asList(response.getBody());
     }
}