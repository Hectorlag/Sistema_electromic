package com.proyectofinal.molinic.repository;

import com.proyectofinal.molinic.DTO.ClienteBasicoDTO;
import com.proyectofinal.molinic.DTO.ClienteProductoDTO;
import com.proyectofinal.molinic.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IclienteRepository extends JpaRepository <Cliente, Long> {

    //Obtengo los clientes no eliminados(campo delete en false)
    List<Cliente> findByDeletedFalse();


    //Obtengo un cliente según su cuil
    Optional<Cliente> findByCuil(String cuil);

}
