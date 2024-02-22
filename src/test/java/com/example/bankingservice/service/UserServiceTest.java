package com.example.bankingservice.service;

import com.example.bankingservice.domain.Role;
import com.example.bankingservice.domain.User;
import com.example.bankingservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void userDetailsAreMappedToSpringSecurityUser() {
        User user = new User();
        user.setExternalId(1L);
        user.setUsername("igor@igor.ee");
        user.setPassword("letmein");

        Role role = new Role();
        role.setExternalId(1);
        role.setName("ROLE_USER");

        user.setRoles(List.of(role));

        when(userRepository.findByUsername("igor@igor.ee")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("igor@igor.ee");

        assertThat(userDetails.getUsername()).isEqualTo("igor@igor.ee");
        assertThat(userDetails.getPassword()).isEqualTo("letmein");
        assertThat(userDetails.getAuthorities())
                .extracting("authority", String.class)
                .containsAnyElementsOf(List.of("ROLE_USER"));
    }
}