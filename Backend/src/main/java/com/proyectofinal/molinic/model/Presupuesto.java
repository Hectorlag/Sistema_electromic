package com.proyectofinal.molinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_presupuesto;

    @DecimalMin(value = "0.0", inclusive = true, message = "El importe debe ser mayor que cero")
    private double importe;

    @NotBlank(message = "La descripción no puede estar en blanco")
    private String descripcion;

    @NotNull(message = "La fecha no puede ser nula")
    @FutureOrPresent(message = "La fecha debe ser presente o futura")
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;

    private boolean aprobado;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "fk_cliente", referencedColumnName = "id_cliente")
    @JsonIgnoreProperties("presupuestosCliente")  // Ignora la propiedad "listaproductos" en la serialización
    private Cliente cli;


    @ManyToOne
    @JoinColumn(name = "fk_producto", referencedColumnName = "id_producto")
    @JsonIgnoreProperties("listaPresu")
    private Producto producto;

}
