package com.clwro.MonitoramentoPrecos.controller;

import com.clwro.MonitoramentoPrecos.dto.LoginRequest;
import com.clwro.MonitoramentoPrecos.dto.LoginResponseDTO;
import com.clwro.MonitoramentoPrecos.dto.RegisterResponseDTO;
import com.clwro.MonitoramentoPrecos.exception.UserAlreadyExistsException;
import com.clwro.MonitoramentoPrecos.model.User;
import com.clwro.MonitoramentoPrecos.security.TokenService;
import com.clwro.MonitoramentoPrecos.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<RegisterResponseDTO> addUser(@Valid @RequestBody User user) throws UserAlreadyExistsException {
        userService.registerUser(user);
        return new ResponseEntity<>(new RegisterResponseDTO("Usuário cadastrado com sucesso!"), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequest request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = tokenService.generateToken(auth);
        return new ResponseEntity<>(new LoginResponseDTO(token), HttpStatus.OK);
    }
}
