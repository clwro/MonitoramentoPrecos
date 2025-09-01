package com.clwro.MonitoramentoPrecos.service;

import com.clwro.MonitoramentoPrecos.exception.UserAlreadyExistsException;
import com.clwro.MonitoramentoPrecos.model.User;
import com.clwro.MonitoramentoPrecos.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Usuário com este e-mail já existe!");
        }

        String password_hash = encryptPassword(user.getPassword());
        userRepository.save(new User(user.getName(), user.getEmail(), password_hash));
    }

    private String encryptPassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }
}