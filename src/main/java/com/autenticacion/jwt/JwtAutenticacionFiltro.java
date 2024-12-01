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

    private final JwtServicio jwtServicio;
    private final UserDetailsService usuarioDetallesServicio;

    @Override
    protected void doFilterInternal(HttpServletRequest solicitud, HttpServletResponse respuesta, FilterChain filterChain) throws ServletException, IOException {

        final String token = obtenerTokenSolicitud(solicitud);
        final String usuario;

        if(token == null){

            filterChain.doFilter(solicitud, respuesta);
            return;
        }

        usuario = jwtServicio.obtenerUsuarioDelToken(token);

        if(usuario != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails usuarioDetalles = usuarioDetallesServicio.loadUserByUsername(usuario);

            if(jwtServicio.esTokenValido(token, usuarioDetalles)){

                UsernamePasswordAuthenticationToken autenticacionToken = new UsernamePasswordAuthenticationToken(
                        usuarioDetalles,
                        null,
                        usuarioDetalles.getAuthorities());

                autenticacionToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(solicitud));

                SecurityContextHolder.getContext().setAuthentication(autenticacionToken);
            }
        }

        filterChain.doFilter(solicitud, respuesta);
    }

    private String obtenerTokenSolicitud(HttpServletRequest request){

        final String encabezadoAutenticacion = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(encabezadoAutenticacion) && encabezadoAutenticacion.startsWith("Bearer ")){

            return encabezadoAutenticacion.substring(7);
        }
        return null;
    }
}
