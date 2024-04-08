package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.DTO.ListaProductosDTO;
import com.proyectofinal.molinic.DTO.ProductoDTO;
import com.proyectofinal.molinic.DTO.ProductosClienteDTO;
import com.proyectofinal.molinic.model.Cliente;
import com.proyectofinal.molinic.model.Producto;
import com.proyectofinal.molinic.repository.IclienteRepository;
import com.proyectofinal.molinic.repository.IproductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService implements IproductoService {

    @Autowired
    private IproductoRepository produRepo;

    @Autowired
    private IclienteRepository clienteRepo;

    //Obtengo solo los productos activos (no eliminados)
    public List<Producto> getProductosActivos(){

        List<Producto> productos = produRepo.findByDeletedFalse();
        productos.sort((p1, p2) -> Long.compare(p2.getId_producto(), p1.getId_producto()));
        return productos;
    }

    //Obtengo todos los productos de un cliente


    @Override
    public List<ListaProductosDTO> getProductos() {
        List<Producto> listaProductos =  this.getProductosActivos();
        List<ListaProductosDTO> listaDTO = new ArrayList<>();

        for (Producto prod: listaProductos){
            ListaProductosDTO p = new ListaProductosDTO();
            p.setId(prod.getId_producto());
            p.setNum_serie(prod.getNum_serie());
            p.setMarca(prod.getMarca());
            p.setTipo(prod.getTipo());
            p.setModelo(prod.getModelo());
            p.setAccesorio(prod.getAccesorios());

            listaDTO.add(p);
        }
        return listaDTO;
    }
  //Obtengo un producto por el NUM_SERIE
    @Override
    public Producto findProductoxnumserie(String numSerie) {

        return produRepo.findProductoxnumserie(numSerie);
    }

    @Override
    public List<ProductosClienteDTO> productosDeUnCliente(Long id_cliente) {

        List<Producto> listaActivos = this.getProductosActivos();
        List<Producto> listaDeProductosUnCliente = new ArrayList<>();

        for(Producto p1: listaActivos){
            if(p1.getCliente().getId_cliente() == id_cliente){
                listaDeProductosUnCliente.add(p1);
                System.out.println("Cliente num: " + id_cliente);
                System.out.println("Marca: " + p1.getMarca());
                System.out.println("Número de serie: " + p1.getNum_serie());
            }

        }
        List<ProductosClienteDTO> listaDTO = new ArrayList<>();

        for(Producto p: listaDeProductosUnCliente){
            ProductosClienteDTO proDTO = new ProductosClienteDTO();
            proDTO.setId_producto(p.getId_producto());
            proDTO.setMarca(p.getMarca());
            proDTO.setNum_serie(p.getNum_serie());

            listaDTO.add(proDTO);
        }
        return listaDTO;
    }

    @Override
    public List<ProductoDTO> getProductosYclientes() {

        //Almaceno la lista de productos que viene desde la base de datos
        List<Producto> listaDeProductos = this.getProductosActivos();
        //Creo una lista vacía del tipo productoDTO
        List<ProductoDTO> listaProductosDTO = new ArrayList<>();

        //Recorro la lista y seteo los datos que me interesan
        for (Producto p: listaDeProductos){
            ProductoDTO productoDTO = new ProductoDTO();

            productoDTO.setId_producto(p.getId_producto());
            productoDTO.setModelo(p.getModelo());
            productoDTO.setMarca(p.getMarca());
            productoDTO.setNum_serie(p.getNum_serie());
            productoDTO.setTipo(p.getTipo());
            productoDTO.setAccesorios(p.getAccesorios());

            //Verifico que el ID del cliente no sea nulo
            if (p.getCliente() != null) {
                productoDTO.setId_cliente(p.getCliente().getId_cliente());

                // Verifico si la lista de nombres del cliente no es nula antes de acceder
                if (p.getCliente().getNombre() != null) {
                    // Obtengo los primeros tres nombres si la lista no está vacía
                    List<String> primerosNombres = p.getCliente().getNombre().stream().limit(3).collect(Collectors.toList());
                    productoDTO.setNombre(primerosNombres);
                } else {
                    productoDTO.setNombre(Collections.emptyList());
                }
            } else {
                productoDTO.setId_cliente(null);
                productoDTO.setNombre(Collections.emptyList());
            }
             listaProductosDTO.add(productoDTO);
        }

        //Retorno la lista
        return listaProductosDTO;
    }

    @Override
    public void saveProducto(Producto p) {
        produRepo.save(p);
    }

    @Transactional
    @Override
    public Producto editProducto(Long id, Producto p) {
        //Busco el producto
        Producto producto = this.findProducto(id);
        //Seteo los datos que vienen desde la BD
        producto.setNum_serie(p.getNum_serie());
        producto.setMarca(p.getMarca());
        producto.setTipo(p.getTipo());
        producto.setModelo(p.getModelo());
        producto.setAccesorios(p.getAccesorios());

        // Actualizar Cliente asociado
        Cliente clienteNuevo = p.getCliente();
        if (clienteNuevo != null) {
            // Si hay un nuevo Cliente, asignarlo al Producto
            producto.setCliente(clienteNuevo);
        }
        producto.setCliente(p.getCliente());

        return produRepo.save(producto);
    }

    @Transactional
    @Override
    public void deleteProducto(Long id) {

        Producto p = this.findProducto(id);
        if(p != null) {
            p.setDeleted(true);
            produRepo.save(p);
        }
    }

    @Override
    public Producto findProducto(Long id) {

        return produRepo.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {

        if(produRepo.existsById(id)){
            return true;
        }else {
            return false;
        }
    }


}
