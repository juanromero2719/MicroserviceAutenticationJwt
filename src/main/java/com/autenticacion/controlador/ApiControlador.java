package com.autenticacion.controlador;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiControlador {

    @PostMapping(value = "bienvenido")
    public String bienvenido(){

        return "bienvenido a la api";
    }
}
