package com.personal.calculateTaxes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/**Classe responsável por configurar a segurança para acesso aos endpoints da aplicação
 * @author TadeuMoreira
 * @since 1.0.0
 * */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { return (web) -> web.ignoring().requestMatchers("/**");}
}
