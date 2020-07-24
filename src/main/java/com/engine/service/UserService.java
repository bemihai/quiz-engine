package com.engine.service;

import com.engine.exception.EmailAlreadyExistsException;
import com.engine.model.UserEntity;
import com.engine.model.UserPrincipal;
import com.engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return findByEmail(email)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public Optional<UserEntity> findByEmail(String email) { return userRepository.findById(email); }

    public UserEntity registerNewUser(UserEntity userEntity) {
        String email = userEntity.getEmail();
        String password = userEntity.getPassword();
        userRepository.findById(email)
                .ifPresentOrElse(
                        u -> {throw new EmailAlreadyExistsException(email);},
                        () -> {
                            userEntity.setPassword(encoder.encode(password));
                            userRepository.save(userEntity);
                        }
                );
        return userEntity;
    }
}