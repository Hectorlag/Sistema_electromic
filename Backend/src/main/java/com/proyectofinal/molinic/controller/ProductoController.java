package com.proyectofinal.molinic.controller;

import com.proyectofinal.molinic.DTO.ListaClientesDTO;
import com.proyectofinal.molinic.DTO.ListaProductosDTO;
import com.proyectofinal.molinic.DTO.ProductoDTO;
import com.proyectofinal.molinic.DTO.ProductosClienteDTO;
import com.proyectofinal.molinic.model.Producto;
import com.proyectofinal.molinic.service.IproductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private IproductoService prodRepo;

    //Obtengo solo los datos de los productos
    @GetMapping("/traer")
    public List<ListaProductosDTO> getProductos(){

        return prodRepo.getProductos();
    }

    //Obtengo todos los productos de un cliente en especifico(id_prod, marca y num_serie)
    @GetMapping("/uncliente/{id_cliente}")
    public ResponseEntity<?> getProductosDeUnCliente(@PathVariable Long id_cliente){

        List<ProductosClienteDTO> lista = prodRepo.productosDeUnCliente(id_cliente);

        if (lista == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la lista de productos para el cliente.");
        } else if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("No se encontraron productos para el cliente.");
        } else {
            return ResponseEntity.ok(lista);
        }
    }

    //Obtengo los de los productos con el id y nombre del cliente
    @GetMapping("/traerconcliente")
    public List<ProductoDTO> getProductosYcliente(){

        return prodRepo.getProductosYclientes();
    }

    @PostMapping("/crear")
    public ResponseEntity<String> saveProducto(@Valid @RequestBody Producto p, BindingResult bindingResult){

        System.out.println("Num de serie: " + p.getNum_serie());
        System.out.println("Tipo: " + p.getTipo());
        System.out.println("Marca: " + p.getMarca());
        System.out.println("Modelo: " + p.getModelo());
        System.out.println("Accesorios: " + p.getAccesorios());
        System.out.println("Id del cliente: " + p.getCliente().getId_cliente());

        // Verifica si el cliente en el presupuesto es nulo
        if (p.getCliente().getId_cliente() == null) {
            return ResponseEntity.badRequest().body("El cliente en el producto no puede ser nulo.");
        }

        if(bindingResult.hasErrors()){
            // Maneja errores de validación
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }
        //Guarda en la base de datos
        prodRepo.saveProducto(p);
        return ResponseEntity.ok("Producto creado");
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editarProducto(@PathVariable Long id, @Valid @RequestBody Producto p, BindingResult bindingResult){

        System.out.println("Num de serie: " + p.getNum_serie());
        System.out.println("Tipo: " + p.getTipo());
        System.out.println("Marca: " + p.getMarca());
        System.out.println("Modelo: " + p.getModelo());
        System.out.println("Accesorios: " + p.getAccesorios());
        System.out.println("Id del cliente: " + p.getCliente().getId_cliente());

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }
        //Actualiza en la base de datos
        prodRepo.editProducto(id, p);
        return ResponseEntity.ok("Producto actualizado con éxito");
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id){

        // Verifico si el producto con el ID proporcionado existe en la base de datos
        if(prodRepo.existsById(id)) {
            prodRepo.deleteProducto(id);
            return new ResponseEntity<>("Producto eliminado", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }
    //Obtengo un producto por el ID
    @GetMapping("/traer/{id}")
    public ResponseEntity<ListaProductosDTO> traerXid(@PathVariable Long id) {

        List<ListaProductosDTO> listaDTO = prodRepo.getProductos();
        // Buscamos el producto por su ID en la lista
        for (ListaProductosDTO p : listaDTO) {
            if (p.getId().equals(id)) {
                // Si encontramos el producto, lo devolvemos como respuesta
                ListaProductosDTO productoDTO = new ListaProductosDTO();
                productoDTO.setId(p.getId());
                productoDTO.setMarca(p.getMarca());
                productoDTO.setTipo(p.getTipo());
                productoDTO.setModelo(p.getModelo());
                productoDTO.setNum_serie(p.getNum_serie());
                productoDTO.setAccesorio(p.getAccesorio());

                return ResponseEntity.ok(productoDTO);
            }
        }
        // Si no se encontró ningún cliente con el ID especificado, devolvemos una respuesta 404
        return ResponseEntity.notFound().build();

    }

    //Obtengo un producto por el NUM_SERIE
    @GetMapping("/traerxnumserie/{num_serie}")
    public ResponseEntity<?> getProductoxnumserie(@PathVariable String num_serie) {
        System.out.println("Buscando producto con número de serie: " + num_serie);
        Producto p = this.prodRepo.findProductoxnumserie(num_serie);
        if (p != null) {
            System.out.println("Producto encontrado: " + p.toString());
            return ResponseEntity.ok(p);
        } else {
            System.out.println("Producto no encontrado.");
            return ResponseEntity.notFound().build();
        }
    }


}
