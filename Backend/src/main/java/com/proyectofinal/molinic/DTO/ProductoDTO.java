package com.proyectofinal.molinic.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {

    private Long id_producto;
    private String num_serie;
    private String tipo;
    private String marca;
    private String modelo;
    private List<String> accesorios;
    private Long id_cliente;  // Id del cliente asociado
    private List<String> nombre;  // Lista de los primeros dos nombres del cliente
}
