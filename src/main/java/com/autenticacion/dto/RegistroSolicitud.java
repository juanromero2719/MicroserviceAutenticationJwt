package com.autenticacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistroSolicitud {

    String usuario;
    String contrasena;
    String nombre;
    String apellido;
    String pais;
}
