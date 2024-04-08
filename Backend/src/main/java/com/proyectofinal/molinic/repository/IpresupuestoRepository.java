package com.proyectofinal.molinic.repository;

import com.proyectofinal.molinic.model.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IpresupuestoRepository extends JpaRepository <Presupuesto, Long> {

    //Obtengo los presupuestos no eliminados y ordenados por fecha
    List<Presupuesto> findByDeletedFalse();

    //Obtengo un presupuesto por su fecha y id del cliente
    @Query("SELECT p FROM Presupuesto p WHERE p.cli.id_cliente = :idCliente AND p.fecha = :fecha")
    Optional<Presupuesto> findByCliIdAndFecha(Long idCliente, LocalDate fecha);

    //Obtengo los presupuestos que no esten eliminados, esten aprobados y no tengan ordenes de trabajo
    @Query("SELECT p FROM Presupuesto p WHERE p.aprobado = true AND p.deleted = false AND NOT EXISTS (SELECT o FROM OrdenDeTrabajo o WHERE o.presupuesto = p)")
    List<Presupuesto> getListaPresupuestoAprobadosSinOrdenes();

}
