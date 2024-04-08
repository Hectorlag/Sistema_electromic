package com.proyectofinal.molinic.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenDTO {

    private Long id_orden;
    private String estado;
    private Long id_presupuesto;
    private LocalDate fecha;
    private Long id_producto;
    private Long id_cliente;

}
