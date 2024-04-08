package com.proyectofinal.molinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;

    @ElementCollection(fetch = FetchType.EAGER)
    @NotNull(message = "La lista de nombre/apellido no puede ser nula")
    private List<@NotBlank(message = "El nombre/apellido no puede estar en blanco") String> nombre;

    @NotBlank(message = "La dirección no puede estar en blanco")
    private String direccion;

    @NotBlank(message = "El cuil no puede estar en blanco")
    private String cuil;

    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El formato del email no es válido")
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> telefono;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cliente")  // Ignora la propiedad "cliente" en la clase producto
    private List<Producto> listaproductos;

    @OneToMany(mappedBy = "cli", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cli")  // Ignora la propiedad "cli" en la clase presupuesto
    private List<Presupuesto> presupuestosCliente;


}
