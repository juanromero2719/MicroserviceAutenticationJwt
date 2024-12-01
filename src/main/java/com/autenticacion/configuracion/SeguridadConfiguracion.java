package com.autenticacion.configuracion;


import com.autenticacion.jwt.JwtAutenticacionFiltro;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SeguridadConfiguracion {

    private final JwtAutenticacionFiltro JwtAutenticacionFiltro;
    private final AuthenticationProvider proveedorAutenticacion;

    @Bean
    public SecurityFilterChain CadenaFiltroSeguridad(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf ->
                        csrf.disable())

                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/autenticacion/**").permitAll()
                                .anyRequest().authenticated()
                        )

                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(proveedorAutenticacion)

                .addFilterBefore(JwtAutenticacionFiltro, UsernamePasswordAuthenticationFilter.class)

                .build();

    }
}
