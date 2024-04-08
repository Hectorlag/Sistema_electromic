package com.proyectofinal.molinic.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccesorioDTO {

    private Long id_accesorio;
    private String nombre;
    private Long id_producto;
}
