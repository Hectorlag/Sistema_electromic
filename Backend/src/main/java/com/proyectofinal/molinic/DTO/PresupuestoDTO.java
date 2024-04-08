package com.proyectofinal.molinic.DTO;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PresupuestoDTO {

    private Long id_presupuesto;
    private double importe;
    private String descripcion;
    private LocalDate fecha;
    private boolean aprobado;
    private Long id_cliente;
    private List<String> nombre;
    private Long id_producto;
    private String marca;
    private  String num_serie;
}
