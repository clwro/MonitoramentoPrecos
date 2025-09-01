package com.clwro.MonitoramentoPrecos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clwro.MonitoramentoPrecos.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
