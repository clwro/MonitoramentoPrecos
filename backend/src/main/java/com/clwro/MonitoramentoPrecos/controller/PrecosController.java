package com.clwro.MonitoramentoPrecos.controller;


import com.clwro.MonitoramentoPrecos.dto.PrecosDTO;
import com.clwro.MonitoramentoPrecos.service.PrecosQueryService;
import com.clwro.MonitoramentoPrecos.service.PrecosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PrecosController {

    private final PrecosService precosService;
    private final PrecosQueryService precosQueryService;

    @PostMapping("/coletar-precos")
    public ResponseEntity<?> collectPrices(){
        try{
            precosService.downloadAndProcess();
            return ResponseEntity.ok("Coleta concluida com sucesso");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Falha ao processar arquivo de preços.");
        }
    }

    @GetMapping("/precos-mensais")
    public ResponseEntity<List<PrecosDTO>> getPrices(){
        List<PrecosDTO> precos = precosQueryService.getPrecos();
        return ResponseEntity.ok(precos);
    }
}
