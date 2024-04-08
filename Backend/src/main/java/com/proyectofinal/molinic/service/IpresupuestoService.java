package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.DTO.PreDTO;
import com.proyectofinal.molinic.DTO.PresupuestoDTO;
import com.proyectofinal.molinic.model.Presupuesto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface IpresupuestoService {

    public List<Presupuesto> getPresupuestosActivos();
    public List<PresupuestoDTO> getPresupuestos();

    public void savePresupuesto(Presupuesto p);

    public Presupuesto editPresupuesto(Long id, Presupuesto p);

    public void deletePresupuesto(Long id);

    public Presupuesto findPresupuesto(Long id);

    boolean existsById(Long id);

    public Presupuesto buscarXfecha(Long idCliente, LocalDate fecha);

    List<PreDTO> getPresupuestosConidYdescripcion();
}
