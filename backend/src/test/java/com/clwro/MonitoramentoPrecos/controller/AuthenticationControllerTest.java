package com.clwro.MonitoramentoPrecos.controller;

import com.clwro.MonitoramentoPrecos.dto.LoginRequest;
import com.clwro.MonitoramentoPrecos.model.User;
import com.clwro.MonitoramentoPrecos.service.UserService;
import com.clwro.MonitoramentoPrecos.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.clwro.MonitoramentoPrecos.exception.GlobalExceptionHandler;
import com.clwro.MonitoramentoPrecos.exception.UserAlreadyExistsException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
                mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerUser() throws Exception {
        User user = new User();
        user.setName("Teste teste");
        user.setEmail("test@test.com");
        user.setPassword("senha123");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response").value("Usuário cadastrado com sucesso!"));
    }

    @Test
    void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@test.com", "senha123");
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn("test-token");

                mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    void whenUserAlreadyExists_thenReturnsConflict() throws Exception {
        User user = new User();
        user.setName("Teste teste");
        user.setEmail("teste@teste.com");
        user.setPassword("senha123");

        doThrow(new UserAlreadyExistsException("Usuário já cadastrado")).when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict());
    }

    @Test
    void whenInvalidCredentials_thenReturnsUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@test.com", "senhaerrada1234");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Senha incorreta"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
