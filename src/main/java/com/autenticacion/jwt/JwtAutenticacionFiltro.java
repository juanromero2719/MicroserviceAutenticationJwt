package com.autenticacion.jwt;

import com.autenticacion.servicio.JwtServicio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAutenticacionFiltro extends OncePerRequestFilter {

    private final JwtServicio JwtServicio;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token = obtener_token_solicitud(request);
        final String usuario;

        if(token == null){

            filterChain.doFilter(request, response);
            return;
        }

        usuario = JwtServicio.getUsernameFromToken(token);

        if(usuario != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails usuario_detalles = userDetailsService.loadUserByUsername(usuario);

            if(JwtServicio.isTokenValid(token, usuario_detalles)){

                UsernamePasswordAuthenticationToken autenticacion_token = new UsernamePasswordAuthenticationToken(
                        usuario_detalles,
                        null,
                        usuario_detalles.getAuthorities());

                autenticacion_token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(autenticacion_token);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String obtener_token_solicitud(HttpServletRequest request){

        final String encabezado_autenticacion = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(encabezado_autenticacion) && encabezado_autenticacion.startsWith("Bearer ")){

            return encabezado_autenticacion.substring(7);
        }
        return null;
    }
}
