package com.proyectofinal.molinic.repository;

import com.proyectofinal.molinic.model.Cliente;
import com.proyectofinal.molinic.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IproductoRepository extends JpaRepository <Producto, Long> {

    //Obtengo los productos no eliminados(campo delete en false)
    List<Producto> findByDeletedFalse();

    //Obtengo un producto por el NUM_SERIE
    @Query("SELECT p FROM Producto p WHERE p.num_serie = :num_serie")
    Producto findProductoxnumserie(@Param("num_serie") String num_serie);

}
