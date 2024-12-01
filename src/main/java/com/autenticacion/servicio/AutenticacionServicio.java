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

    private final JwtServicio jwtServicio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager autenticacionManejador;

    public RespuestaAutenticacion ingreso(IngresoSolicitud solicitud){

        autenticacionManejador.authenticate(new UsernamePasswordAuthenticationToken(solicitud.getUsuario(),solicitud.getContrasena()));
        UserDetails usuario = usuarioRepositorio.findByUsuario(solicitud.getUsuario())
                .orElseThrow();

        String token = jwtServicio.getToken(usuario);

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

        usuarioRepositorio.save(Usuario);

        return RespuestaAutenticacion.builder()
                .token(jwtServicio.getToken(Usuario))
                .build();

    }
}
