package com.proyectofinal.molinic.controller;

import com.proyectofinal.molinic.DTO.OrdenDTO;
import com.proyectofinal.molinic.model.OrdenDeTrabajo;
import com.proyectofinal.molinic.service.IordenDeTrabajoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenDeTrabajoController {

    @Autowired
    private IordenDeTrabajoService ordenRepo;

    @GetMapping("/traer")
    public List<OrdenDTO> getOrdenes(){
        return ordenRepo.getOrdenes();
    }

    @PostMapping("/crear")
    public ResponseEntity<String> saveOrden(@Valid @RequestBody OrdenDeTrabajo orden, BindingResult bindingResult){

        System.out.println("Estado de la orden: "+ orden.getEstado());
        System.out.println("Id del presupuesto: " + orden.getPresupuesto().getId_presupuesto());
        if(bindingResult.hasErrors()){
            // Maneja errores de validación
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }
        //Guarda en la base de datos
        ordenRepo.saveOrdenDeTrabajo(orden);
        return ResponseEntity.ok("Orden creada con éxito");
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editOrdenDeTrabajo(@PathVariable Long id,
                                                     @Valid @RequestBody OrdenDeTrabajo orden,
                                                     BindingResult bindingResult){
        System.out.println("El id de la orden es: " + orden.getId_orden());
        System.out.println("El estado es: " + orden.getEstado());
        System.out.println("EL id del presupuesto es: " + orden.getPresupuesto());

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }
        //Actualiza en la base de datos
        ordenRepo.editOrdenDeTrabajo(id, orden);

        return ResponseEntity.ok("Orden de trabajo editada con éxito");
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> deleteOrdenDeTrabajo(@PathVariable Long id){
        // Verifico si la orden con el ID proporcionado existe en la base de datos
        if(ordenRepo.existsById(id)){
            ordenRepo.deleteOrdenDeTrabajo(id);
            return new ResponseEntity<>("Orden de trabajo eliminada", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Orden no encontrada eliminado", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/traer/{id}")
    public ResponseEntity<OrdenDeTrabajo> traerXid(@PathVariable Long id){

        OrdenDeTrabajo orden = ordenRepo.findOrdenDeTrabajo(id);
        if(orden != null){
        return ResponseEntity.ok(orden);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/traerXestado/{estado}")
    public ResponseEntity<List<OrdenDTO>> OrdenPorDescripcion(@PathVariable String estado) {
        List<OrdenDTO> lista = ordenRepo.traerXestado(estado);

        if (!lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
