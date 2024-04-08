package com.proyectofinal.molinic.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class ListaClientesDTO {

    private Long id_cliente;
    private List<String> nombre;
    private String direccion;
    private String cuil;
    private String email;
    private List<String> telefono;
}
