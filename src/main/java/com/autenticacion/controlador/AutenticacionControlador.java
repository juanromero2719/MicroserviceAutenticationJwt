package com.autenticacion.controlador;


import com.autenticacion.dto.RespuestaAutenticacion;
import com.autenticacion.dto.IngresoSolicitud;
import com.autenticacion.dto.RegistroSolicitud;
import com.autenticacion.servicio.AutenticacionServicio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autenticacion")
@RequiredArgsConstructor
public class AutenticacionControlador {

    private final AutenticacionServicio AutenticacionServicio;

    @PostMapping(value = "login")
    public ResponseEntity<RespuestaAutenticacion> login(@RequestBody IngresoSolicitud solicitud){

        return ResponseEntity.ok(AutenticacionServicio.ingreso(solicitud));
    }

    @PostMapping("registro")
    public ResponseEntity<RespuestaAutenticacion>  registro(@RequestBody RegistroSolicitud solicitud){

        return ResponseEntity.ok(AutenticacionServicio.registro(solicitud));
    }
}
