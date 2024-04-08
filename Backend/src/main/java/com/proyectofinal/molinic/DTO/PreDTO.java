package com.proyectofinal.molinic.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreDTO {

    private Long id_presupuesto;
    private LocalDate fecha;
    private Long id_producto;
    private String tipo_producto;
    private String num_serie;
}
