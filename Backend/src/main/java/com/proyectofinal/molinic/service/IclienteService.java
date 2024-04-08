package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.DTO.ClienteBasicoDTO;
import com.proyectofinal.molinic.DTO.ListaClientesDTO;
import com.proyectofinal.molinic.DTO.ClienteProductoDTO;
import com.proyectofinal.molinic.model.Cliente;

import java.util.List;

public interface IclienteService {

    public List<ClienteProductoDTO> getClientesConProductos();

    public void saveCliente(Cliente cli);

    public Cliente editCliente(Long id, Cliente cli);

    public void deleteCliente(Long id);

    public Cliente findCliente(Long id);

    boolean existsById(Long id);

    List<ListaClientesDTO> getClientes();

    ClienteProductoDTO findClienteXcuil(String cuil);

    List<ClienteBasicoDTO> getClientesBasicos();

}
