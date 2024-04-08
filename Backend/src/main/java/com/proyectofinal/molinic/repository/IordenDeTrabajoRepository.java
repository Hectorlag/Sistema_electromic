package com.proyectofinal.molinic.repository;

import com.proyectofinal.molinic.model.OrdenDeTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IordenDeTrabajoRepository extends JpaRepository <OrdenDeTrabajo, Long> {

    //Obtengo las ordenes de trabajo no eliminadas(campo delete en false)
    @Query("SELECT o FROM OrdenDeTrabajo o " +
            "INNER JOIN o.presupuesto p " +
            "INNER JOIN p.producto pro " +
            "INNER JOIN pro.cliente c " +
            "WHERE o.deleted = false " +
            "ORDER BY o.id")
    List<OrdenDeTrabajo> findActiveOrdenesDeTrabajo();

    //Obtengo la lista de ordenes segun la descripcion
    List<OrdenDeTrabajo> findByEstadoContainingIgnoreCase(String estado);
}
