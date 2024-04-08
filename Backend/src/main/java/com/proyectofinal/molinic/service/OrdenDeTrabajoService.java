package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.DTO.OrdenDTO;
import com.proyectofinal.molinic.model.Cliente;
import com.proyectofinal.molinic.model.OrdenDeTrabajo;
import com.proyectofinal.molinic.model.Presupuesto;
import com.proyectofinal.molinic.model.Producto;
import com.proyectofinal.molinic.repository.IordenDeTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class OrdenDeTrabajoService implements IordenDeTrabajoService {

    @Autowired
    private IordenDeTrabajoRepository ordenRepo;

    @Override
    public List<OrdenDTO> getOrdenes() {
        //Recibo la lista de ordenes de trabajo desde la BD
        List<OrdenDeTrabajo> listaOrden = ordenRepo.findActiveOrdenesDeTrabajo();
        List<OrdenDTO> listaOrdenDTO = new ArrayList<>();

        //Recorro la lista de la BD
        for (OrdenDeTrabajo orden : listaOrden) {
            OrdenDTO o = new OrdenDTO();

            o.setId_orden(orden.getId_orden());
            o.setEstado(orden.getEstado());

            Presupuesto presupuesto = orden.getPresupuesto();
            if (presupuesto != null) {
                o.setId_presupuesto(presupuesto.getId_presupuesto());
                o.setFecha(presupuesto.getFecha());

                // Verificar si el producto asociado al presupuesto existe
                Producto producto = presupuesto.getProducto();
                if (producto != null) {
                    o.setId_producto(producto.getId_producto());
                } else {
                    // Si el producto no existe, puedes establezco valor null en el DTO
                    o.setId_producto(null);
                }

                // Verificar si el cliente asociado al presupuesto existe
                Cliente c = presupuesto.getCli();
                if (c != null) {
                    o.setId_cliente(c.getId_cliente());
                } else {
                    // Si el cliente no existe, establezco valor null en el DTO
                    o.setId_cliente(null);
                }

            } else {
                // Si no hay presupuesto asociado, establecer valores predeterminados o null en el DTO
                o.setId_presupuesto(null);

            }

            listaOrdenDTO.add(o);
        }
        // Ordenar la lista de DTO por el ID de la orden en forma descendente
        Collections.sort(listaOrdenDTO, Comparator.comparingLong(OrdenDTO::getId_orden).reversed());

        return listaOrdenDTO;
    }

    @Override
    public void saveOrdenDeTrabajo(OrdenDeTrabajo orden) {
        ordenRepo.save(orden);
    }

    @Override
    public OrdenDeTrabajo editOrdenDeTrabajo(Long id, OrdenDeTrabajo orden) {
        //Busco la orden de trabajo
        OrdenDeTrabajo ord = this.findOrdenDeTrabajo(id);
        //Seteo los datos que viene desde la BD
        ord.setEstado(orden.getEstado());
        ord.setPresupuesto(orden.getPresupuesto());

        return ordenRepo.save(ord);
    }

    @Override
    public void deleteOrdenDeTrabajo(Long id) {

        OrdenDeTrabajo orden = this.findOrdenDeTrabajo(id);

        if (orden != null) {
            orden.setDeleted(true);
            ordenRepo.save(orden);
            System.out.println("Orden numero " + id + " Eliminada!!");
        }

    }

    @Override
    public OrdenDeTrabajo findOrdenDeTrabajo(Long id) {
        return ordenRepo.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {

        if (ordenRepo.existsById(id)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<OrdenDTO> traerXestado(String estado) {

        List<OrdenDeTrabajo> lista = this.ordenRepo.findByEstadoContainingIgnoreCase(estado);
        List<OrdenDTO> listaDTO = new ArrayList<>();


        for (OrdenDeTrabajo orden : lista) {
            if (!orden.getPresupuesto().isDeleted()) {
                OrdenDTO o = new OrdenDTO();
                o.setId_orden(orden.getId_orden());
                o.setEstado(orden.getEstado());
                o.setId_presupuesto(orden.getPresupuesto().getId_presupuesto());
                listaDTO.add(o);
            }

        }

        for (OrdenDTO or: listaDTO){
            System.out.println("id orde: " + or.getId_orden());
            System.out.println("descricion: " + or.getEstado());
            System.out.println("fk presu: " + or.getId_presupuesto());
        }
        return listaDTO;
    }

}
