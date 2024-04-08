package com.proyectofinal.molinic.repository;

import com.proyectofinal.molinic.model.Cliente;
import com.proyectofinal.molinic.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IusuarioRepository extends JpaRepository <Usuario, Long> {

    //Obtengo un un usuario por su nombre
    Optional<Usuario> findByNombre(String nombre);

    //Obtengo los usuarios no eliminado
    List<Usuario> findByDeletedFalse();


}
