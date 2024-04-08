package com.proyectofinal.molinic.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ProductosClienteDTO {

    private Long id_producto;
    private String marca;
    private String num_serie;
}
