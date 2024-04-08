package com.proyectofinal.molinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @NotBlank(message = "El nombre del usuario no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contrasenia;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}
