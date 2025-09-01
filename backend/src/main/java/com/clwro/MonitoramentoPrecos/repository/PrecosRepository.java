package com.clwro.MonitoramentoPrecos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clwro.MonitoramentoPrecos.model.Precos;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrecosRepository extends JpaRepository<Precos, Integer> {

    @NativeQuery(value = "SELECT * FROM Precos WHERE date >= :startDate")
    List<Precos> findPrecosSince(@Param("startDate")LocalDate startDate);
}
