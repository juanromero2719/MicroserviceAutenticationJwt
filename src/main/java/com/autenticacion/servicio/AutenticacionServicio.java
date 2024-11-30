package com.autenticacion.servicio;

import com.autenticacion.dto.RespuestaAutenticacion;
import com.autenticacion.dto.IngresoSolicitud;
import com.autenticacion.dto.RegistroSolicitud;
import com.autenticacion.modelo.Rol;
import com.autenticacion.modelo.Usuario;
import com.autenticacion.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AutenticacionServicio {

    private final JwtServicio Jwt_service;
    private final UsuarioRepositorio UsuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager autenticacion_manager;

    public RespuestaAutenticacion ingreso(IngresoSolicitud solicitud){

        autenticacion_manager.authenticate(new UsernamePasswordAuthenticationToken(solicitud.getUsuario(),solicitud.getContrasena()));
        UserDetails usuario = UsuarioRepositorio.findByUsuario(solicitud.getUsuario())
                .orElseThrow();

        String token = Jwt_service.getToken(usuario);

        return RespuestaAutenticacion.builder()
                .token(token)
                .build();
    }

    public RespuestaAutenticacion registro(RegistroSolicitud solicitud){

        Usuario Usuario = com.autenticacion.modelo.Usuario.builder()
                .usuario(solicitud.getUsuario())
                .contrasena(passwordEncoder.encode(solicitud.getContrasena()))
                .nombre(solicitud.getNombre())
                .apellido(solicitud.getApellido())
                .pais(solicitud.getPais())
                .role(Rol.USER)
                .build();

        UsuarioRepositorio.save(Usuario);

        return RespuestaAutenticacion.builder()
                .token(Jwt_service.getToken(Usuario))
                .build();

    }
}
