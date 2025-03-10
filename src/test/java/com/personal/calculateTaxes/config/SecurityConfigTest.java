package com.personal.calculateTaxes.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @Test
    void shouldNotBeNullWhenInstanceSecurityConfig() {
        SecurityConfig config = new SecurityConfig();
        assertNotNull(config);
    }

    @Test
    void shouldNotBeNullWhenInstanceWebSecurityCustomizer() {
        SecurityConfig config = new SecurityConfig();
        assertNotNull(config.webSecurityCustomizer());
    }
}
