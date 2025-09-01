package com.clwro.MonitoramentoPrecos.service;

import com.clwro.MonitoramentoPrecos.dto.PrecosDTO;
import com.clwro.MonitoramentoPrecos.repository.PrecosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrecosQueryService {
    private final PrecosRepository precosRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<PrecosDTO> getPrecos(){
        LocalDate startDate = LocalDate.now().withDayOfMonth(1).minusMonths(12);

        return precosRepository.findPrecosSince(startDate)
                .stream()
                .map(price -> new PrecosDTO(
                        price.getDate().format(formatter),
                        price.getRegion(),
                        price.getPrice()))
                .collect(Collectors.toList());
    }
}
