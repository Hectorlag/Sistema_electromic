package com.proyectofinal.molinic.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class ClienteBasicoDTO {

    private Long id_cliente;
    private List<String> nombre;

  }

