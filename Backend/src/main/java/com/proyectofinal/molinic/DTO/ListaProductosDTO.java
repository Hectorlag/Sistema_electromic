package com.proyectofinal.molinic.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class ListaProductosDTO {
    private Long id;
    private String marca;
    private String modelo;
    private String num_serie;
    private String tipo;
    private List<String> accesorio;
}
