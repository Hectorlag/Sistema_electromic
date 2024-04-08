package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.DTO.PreDTO;
import com.proyectofinal.molinic.DTO.PresupuestoDTO;
import com.proyectofinal.molinic.model.Presupuesto;
import com.proyectofinal.molinic.repository.IpresupuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PresupuestoService implements IpresupuestoService{

    @Autowired
    private IpresupuestoRepository preRepo;
   //Obtengo todos los presupuestos no eliminados(activos)
    public List<Presupuesto> getPresupuestosActivos(){

        List<Presupuesto> presupuestos = preRepo.findByDeletedFalse();
        presupuestos.sort((p1, p2) -> Long.compare(p2.getId_presupuesto(), p1.getId_presupuesto()));
        return presupuestos;

    }

    @Override
    public List<PresupuestoDTO> getPresupuestos() {
        List<Presupuesto> listaDePresupuestos = this.getPresupuestosActivos();
        List<PresupuestoDTO> listaPresupuestosDTO = new ArrayList<>();

        for (Presupuesto presupuesto : listaDePresupuestos) {
            PresupuestoDTO presupuestoDTO = new PresupuestoDTO();

            presupuestoDTO.setId_presupuesto(presupuesto.getId_presupuesto());
            presupuestoDTO.setAprobado(presupuesto.isAprobado());
            presupuestoDTO.setImporte(presupuesto.getImporte());
            presupuestoDTO.setFecha(presupuesto.getFecha());
            presupuestoDTO.setDescripcion(presupuesto.getDescripcion());

            if (presupuesto.getCli() != null) {
                presupuestoDTO.setId_cliente(presupuesto.getCli().getId_cliente());

                if (presupuesto.getCli().getNombre() != null) {
                    List<String> primerosNombres = presupuesto.getCli().getNombre().stream().limit(3).collect(Collectors.toList());
                    presupuestoDTO.setNombre(primerosNombres);
                } else {
                    presupuestoDTO.setNombre(Collections.emptyList());
                }
            } else {
                presupuestoDTO.setId_cliente(null);
                presupuestoDTO.setNombre(Collections.emptyList());
            }

            // Copia de los atributos del producto
            if (presupuesto.getProducto() != null) {
                presupuestoDTO.setId_producto(presupuesto.getProducto().getId_producto());
                presupuestoDTO.setMarca(presupuesto.getProducto().getMarca());
                presupuestoDTO.setNum_serie(presupuesto.getProducto().getNum_serie());
            } else {
                presupuestoDTO.setId_producto(null);
                presupuestoDTO.setMarca(null);
                presupuestoDTO.setNum_serie(null);
            }

            listaPresupuestosDTO.add(presupuestoDTO);
        }

        return listaPresupuestosDTO;
    }

    @Override
    public void savePresupuesto(Presupuesto p) {

        preRepo.save(p);
    }

    @Override
    public Presupuesto editPresupuesto(Long id, Presupuesto p) {

        //Busco el presupuesto
        Presupuesto presu = this.findPresupuesto(id);
        //Seteo los datos que vienen de la BD
        presu.setAprobado(p.isAprobado());
        presu.setDescripcion(p.getDescripcion());
        presu.setFecha(p.getFecha());
        presu.setImporte(p.getImporte());
        presu.setCli(p.getCli());

        return preRepo.save(presu);
    }

    @Override
    public void deletePresupuesto(Long id) {

        Presupuesto p = this.findPresupuesto(id);
        if(p != null) {
            p.setDeleted(true);
            preRepo.save(p);
        }
    }

    @Override
    public Presupuesto findPresupuesto(Long id) {
        return preRepo.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {

        if(preRepo.existsById(id)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Presupuesto buscarXfecha(Long idCliente, LocalDate fecha) {
        Optional<Presupuesto> presupuestoOptional = preRepo.findByCliIdAndFecha(idCliente, fecha);

        // Si el Optional contiene un Presupuesto, devuélvelo; de lo contrario, devuelve null o lanza una excepción según tus necesidades.
        return presupuestoOptional.orElse(null);
    }

    @Override
    public List<PreDTO> getPresupuestosConidYdescripcion() {

        List<Presupuesto> lista= preRepo.getListaPresupuestoAprobadosSinOrdenes();

        List<PreDTO> listaDTO = new ArrayList<>();

        for (Presupuesto pre: lista){

            PreDTO p = new PreDTO();
            p.setId_presupuesto(pre.getId_presupuesto());
            p.setFecha(pre.getFecha());

            listaDTO.add(p);
        }
        return listaDTO;
    }

}
