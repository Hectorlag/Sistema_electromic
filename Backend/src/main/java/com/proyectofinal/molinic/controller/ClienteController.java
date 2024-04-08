package com.proyectofinal.molinic.controller;

import com.proyectofinal.molinic.DTO.ClienteBasicoDTO;
import com.proyectofinal.molinic.DTO.ListaClientesDTO;
import com.proyectofinal.molinic.DTO.ClienteProductoDTO;
import com.proyectofinal.molinic.model.Cliente;
import com.proyectofinal.molinic.service.IclienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IclienteService cliRepo;

    //Obtengo solo los datos de los clientes
    @GetMapping("/traer")
    public List<ListaClientesDTO> getClientes(){
        return cliRepo.getClientes();
    }

    //Obtengo los clientes con los productos
    @GetMapping("/traerConProductos")
    public List<ClienteProductoDTO> getClientesConProductos(){

        return cliRepo.getClientesConProductos();
    }

    //Obtengo el id y nombres del cliente
    @GetMapping("/traerXidYnombre")
    public List<ClienteBasicoDTO> getClientesBasicos(){
        return cliRepo.getClientesBasicos();
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearCliente(@Valid @RequestBody Cliente c, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            // Maneja errores de validación
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }
        // Guarda en la base de datos
        cliRepo.saveCliente(c);
        return ResponseEntity.ok("Cliente creado con éxito");
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<String> editCliente(@PathVariable Long id, @Valid @RequestBody Cliente c, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }
        // Actualiza en la base de datos
        cliRepo.editCliente(id, c);

        return ResponseEntity.ok("Cliente actualizado con éxito");
    }

    @DeleteMapping("/borrar/{id}")
    public  ResponseEntity<String> deleteCliente(@PathVariable Long id){

        // Verifico si el cliente con el ID proporcionado existe en la base de datos
        if (cliRepo.existsById(id)) {
            cliRepo.deleteCliente(id);
            return new ResponseEntity<>("Cliente eliminado", HttpStatus.OK);
        } else {
            // Si el cliente no existe, devuelvo un código de estado 404 (Not Found)
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    //Traer cliente por id
    @GetMapping("/traer/{id}")
    public ResponseEntity<ListaClientesDTO> traerXid(@PathVariable Long id){
        // Obtenemos la lista de clientes
        List<ListaClientesDTO> listaDTO = cliRepo.getClientes();

        // Buscamos el cliente por su ID en la lista
        for(ListaClientesDTO l : listaDTO){
            if(l.getId_cliente().equals(id)){
                // Si encontramos el cliente, lo devolvemos como respuesta
                ListaClientesDTO clienteDTO = new ListaClientesDTO();
                clienteDTO.setId_cliente(l.getId_cliente());
                clienteDTO.setNombre(l.getNombre());
                clienteDTO.setDireccion(l.getDireccion());
                clienteDTO.setCuil(l.getCuil());
                clienteDTO.setEmail(l.getEmail());
                clienteDTO.setTelefono(l.getTelefono());

                return ResponseEntity.ok(clienteDTO);
            }
        }

        // Si no se encontró ningún cliente con el ID especificado, devolvemos una respuesta 404
        return ResponseEntity.notFound().build();
    }

    //Traer cliente por cuil
    @GetMapping("/traerxcuil/{cuil}")
    public ResponseEntity<ClienteProductoDTO> traerXcuil(@PathVariable String cuil){

        ClienteProductoDTO c = cliRepo.findClienteXcuil(cuil);

        if(c != null){
            return ResponseEntity.ok(c);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


}
