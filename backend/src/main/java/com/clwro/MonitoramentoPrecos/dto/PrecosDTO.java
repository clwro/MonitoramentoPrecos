package com.clwro.MonitoramentoPrecos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record PrecosDTO(@JsonProperty("data") String date,
                        @JsonProperty("region") String region,
                        @JsonProperty("price") double price) {}
