package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.DTO.OrdenDTO;
import com.proyectofinal.molinic.model.OrdenDeTrabajo;

import java.util.List;

public interface IordenDeTrabajoService {

    public List<OrdenDTO> getOrdenes();

    public void saveOrdenDeTrabajo(OrdenDeTrabajo orden);

    public OrdenDeTrabajo editOrdenDeTrabajo(Long id, OrdenDeTrabajo orden);

    public void deleteOrdenDeTrabajo(Long id);

    public OrdenDeTrabajo findOrdenDeTrabajo(Long id);

    boolean existsById(Long id);

    List traerXestado(String estado);
}
