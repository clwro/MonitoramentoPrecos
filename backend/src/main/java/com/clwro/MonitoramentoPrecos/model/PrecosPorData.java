package com.clwro.MonitoramentoPrecos.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "precos")
@Data
@NoArgsConstructor
public class PrecosPorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Column(name ="date")
    private LocalDate date;

    @NotBlank
    @NotNull
    @Column(name = "sul")
    private double sul;

    @NotBlank
    @NotNull
    @Column(name = "sudeste")
    private double sudeste;

    @NotBlank
    @NotNull
    @Column(name = "nordeste")
    private double nordeste;

    @NotBlank
    @NotNull
    @Column(name = "norte")
    private double norte;

    public PrecosPorData(LocalDate date, double sul, double sudeste, double nordeste, double norte) {
        this.date = date;
        this.sul = sul;
        this.sudeste = sudeste;
        this.nordeste = nordeste;
        this.norte = norte;
    }
}
