package com.proyectofinal.molinic.controller;

import com.proyectofinal.molinic.DTO.PreDTO;
import com.proyectofinal.molinic.DTO.PresupuestoDTO;
import com.proyectofinal.molinic.model.Presupuesto;
import com.proyectofinal.molinic.model.Producto;
import com.proyectofinal.molinic.service.IpresupuestoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/presupuestos")
public class PresupuestoController {

    @Autowired
    private IpresupuestoService presuRepo;

    //Obtengo el id del presupuesto y la descripcion
    @GetMapping("/traeridyfecha")
    public List<PreDTO> getPresupuestosConid(){
        return presuRepo.getPresupuestosConidYdescripcion();
    }

    //Obtengo los presupuesto con el id y nombre del cliente
    @GetMapping("/traerconclientes")
    public List<PresupuestoDTO> getPresupuesto(){
        return presuRepo.getPresupuestos();
    }

    @PostMapping("/crear")
    public ResponseEntity<String> savePresupuesto(@Valid @RequestBody Presupuesto p, BindingResult bindingResult){

        System.out.println("Descipcion: " + p.getDescripcion());
        System.out.println("Fecha: " + p.getFecha());
        System.out.println("Aprobado: " + p.isAprobado());
        System.out.println("importe: " + p.getImporte());

        System.out.println("Id cliente: " + p.getCli().getId_cliente());
        System.out.println("Id del producto " + p.getProducto().getId_producto());

        // Verifica si el cliente en el presupuesto es nulo
        if (p.getCli() == null) {
            return ResponseEntity.badRequest().body("El cliente en el presupuesto no puede ser nulo.");
        }
        if (p.getProducto() == null) {
            return ResponseEntity.badRequest().body("El producto en el presupuesto no puede ser nulo.");
        }

        if(bindingResult.hasErrors()){
            // Maneja errores de validación
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }
        //Guarda en la base de datos
        presuRepo.savePresupuesto(p);

        return ResponseEntity.ok("Presupuesto creado con éxito");
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editPresupuesto(@PathVariable Long id, @Valid @RequestBody Presupuesto p, BindingResult bindingResult){

        System.out.println("Descripcion: " + p.getDescripcion());
        System.out.println("Fecha: " + p.getFecha());
        System.out.println("Aprobado: " + p.isAprobado());
        System.out.println("importe: " + p.getImporte());
        System.out.println("El id del cliente es: " + p.getCli().getId_cliente());


        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }
        //Actualiza en la base de datos
        presuRepo.editPresupuesto(id, p);

        return ResponseEntity.ok("Presupuesto editado con éxito");
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> deletePresupuesto(@PathVariable Long id){

        // Verifico si el presupuesto con el ID proporcionado existe en la base de datos
        if(presuRepo.existsById(id)) {
            presuRepo.deletePresupuesto(id);
            return new ResponseEntity<>("Presupuesto eliminado", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Presupuesto no encontrado", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("traer/{id}")
    public ResponseEntity<Presupuesto> traerXid(@PathVariable Long id){

        Presupuesto p = presuRepo.findPresupuesto(id);
        if(p != null){
            System.out.println("Presupuesto encontrado: " + p.toString());
            return ResponseEntity.ok(p);
        }else {
            System.out.println("Presupuesto no encontrado.");
            return ResponseEntity.notFound().build();
        }
    }

    //Obtener un presupuesto por su fecha y el id del cliente
    @GetMapping("/traer/{id_cliente}/{fecha}")
    public ResponseEntity<?> getPresupuestoXfecha(@PathVariable Long id_cliente,
                                                  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        System.out.println("Presupuesto con fecha: " + fecha);

       Presupuesto p = this.presuRepo.buscarXfecha(id_cliente, fecha);

        if (p != null ) {
            System.out.println("Presupuesto encontrado: " + p.toString());
            return ResponseEntity.ok(p);
        } else {
            System.out.println("Presupuesto no encontrado.");
            return ResponseEntity.notFound().build();
        }
    }


}
