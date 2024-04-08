package com.proyectofinal.molinic.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class OrdenDeTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_orden;

    @NotBlank(message = "El estado de la orden no puede estar vacía")
    private String estado;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @OneToOne
    @JoinColumn(name = "fk_presup", referencedColumnName = "id_presupuesto")
    private Presupuesto presupuesto;


}
