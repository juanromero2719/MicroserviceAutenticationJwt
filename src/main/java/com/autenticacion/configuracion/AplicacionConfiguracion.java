package com.autenticacion.configuracion;

import com.autenticacion.repositorio.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AplicacionConfiguracion {

    private final UsuarioRepositorio UsuarioRepositorio;

    @Bean
    public AuthenticationManager ManejadorAutenticacion(AuthenticationConfiguration configuracion) throws Exception {

        return configuracion.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider ProveedorAutenticacion(){

        DaoAuthenticationProvider proveedorAutenticacion = new DaoAuthenticationProvider();
        proveedorAutenticacion.setUserDetailsService(UsuarioDetallesServicio());
        proveedorAutenticacion.setPasswordEncoder(CodificadorContrasena());
        return proveedorAutenticacion;
    }

    @Bean
    public PasswordEncoder CodificadorContrasena(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService UsuarioDetallesServicio(){

        return usuario -> UsuarioRepositorio.findByUsuario(usuario)
        .orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado"));
    }
}
