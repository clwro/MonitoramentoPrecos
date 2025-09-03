package com.clwro.MonitoramentoPrecos.controller;

import com.clwro.MonitoramentoPrecos.dto.PrecosDTO;
import com.clwro.MonitoramentoPrecos.service.PrecosQueryService;
import com.clwro.MonitoramentoPrecos.service.PrecosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PrecosControllerTest {

    @Mock
    private PrecosService precosService;

    @Mock
    private PrecosQueryService precosQueryService;

    @InjectMocks
    private PrecosController precosController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(precosController).build();
    }

    @Test
    void collectPrices() throws Exception {
        doNothing().when(precosService).downloadAndProcess();

        mockMvc.perform(post("/coletar-precos"))
                .andExpect(status().isOk());

        verify(precosService, times(1)).downloadAndProcess();
    }

    @Test
    void collectPrices_IOException() throws Exception {
        doThrow(new IOException()).when(precosService).downloadAndProcess();

        mockMvc.perform(post("/coletar-precos"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPrices() throws Exception {
                PrecosDTO preco1 = new PrecosDTO("2025-09-03", "teste1", 10.0);
        PrecosDTO preco2 = new PrecosDTO("2025-09-03", "teste22", 20.0);
        List<PrecosDTO> precos = Arrays.asList(preco1, preco2);

        when(precosQueryService.getPrecos()).thenReturn(precos);

        mockMvc.perform(get("/precos-mensais")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(precos.size()));
    }
}
