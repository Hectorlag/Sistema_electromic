package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.DTO.ListaProductosDTO;
import com.proyectofinal.molinic.DTO.ProductoDTO;
import com.proyectofinal.molinic.DTO.ProductosClienteDTO;
import com.proyectofinal.molinic.model.Producto;

import java.util.List;

public interface IproductoService {

    public List<ProductoDTO> getProductosYclientes();

    public void saveProducto(Producto p);

    public Producto editProducto(Long id, Producto p);

    public void deleteProducto(Long id);

    public Producto findProducto(Long id);

    boolean existsById(Long id);

    List<ListaProductosDTO> getProductos();

    Producto findProductoxnumserie(String numSerie);

    //Obtengo todos los productos de un cliente
    List<ProductosClienteDTO> productosDeUnCliente(Long id_cliente);
}
