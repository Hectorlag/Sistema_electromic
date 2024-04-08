package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.DTO.ClienteBasicoDTO;
import com.proyectofinal.molinic.DTO.ListaClientesDTO;
import com.proyectofinal.molinic.DTO.ClienteProductoDTO;
import com.proyectofinal.molinic.DTO.ListaProductosDTO;
import com.proyectofinal.molinic.model.Cliente;
import com.proyectofinal.molinic.repository.IclienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService implements IclienteService {

    @Autowired
    private IclienteRepository cliRepo;

    //Obtengo solo los datos de los clientes no eliminados
    public List<Cliente> obtenerClientesActivos() {

        List<Cliente> clientes = cliRepo.findByDeletedFalse();

        // Ordeno la lista de clientes por el último elemento del atributo "nombre" ignorando la capitalización
        clientes.sort(Comparator.comparing(cliente -> {
            List<String> nombres = cliente.getNombre();
            if (nombres.isEmpty()) {
                return "";
            } else {
                String ultimoNombre = nombres.get(nombres.size() - 1);
                return ultimoNombre;
            }
        }, String.CASE_INSENSITIVE_ORDER));

        return clientes;
    }

    //Obtengo TODOS los datos de la tabla cliente
  @Override
  public List<ListaClientesDTO> getClientes() {
      //Recibo lista de clientes que vienen desde la base de datos
      List<Cliente> listaCompleta = this.obtenerClientesActivos();

      //Creo una lista del tipo ClienteDTO
      List<ListaClientesDTO> listaClienteDTO = new ArrayList<>();

      for (Cliente c: listaCompleta){
          ListaClientesDTO cliDto = new ListaClientesDTO();
          cliDto.setId_cliente(c.getId_cliente());
          cliDto.setNombre(c.getNombre());
          cliDto.setDireccion(c.getDireccion());
          cliDto.setCuil(c.getCuil());
          cliDto.setEmail(c.getEmail());
          cliDto.setTelefono(c.getTelefono());

          //Añado el objeto seteado
          listaClienteDTO.add(cliDto);
      }
      return listaClienteDTO;
  }
   //Obtengo el cliente con sus productos por su número de cuil
    @Override
    public ClienteProductoDTO findClienteXcuil(String cuil) {

        Optional<Cliente> clienteOptional = cliRepo.findByCuil(cuil);

        // Verifica si el cliente existe
        if (clienteOptional.isPresent()) {

            //Creo un cliente de clienteProductoDTO
            ClienteProductoDTO clienteProductoDTO = new ClienteProductoDTO();

            //Seteo los datos
            clienteProductoDTO.setId_cliente(clienteOptional.get().getId_cliente());
            clienteProductoDTO.setNombreCompleto(clienteOptional.get().getNombre());
            clienteProductoDTO.setDireccion(clienteOptional.get().getDireccion());
            clienteProductoDTO.setTelefono(clienteOptional.get().getTelefono());

            // transforma 1 lista de productos (List<Producto>) en una lista de objetos DTO (List<ProDTO>)
            List<ListaProductosDTO> productosDTO = clienteOptional.get().getListaproductos().stream()
                    .map(producto -> new ListaProductosDTO(producto.getId_producto(), producto.getMarca(), producto.getModelo(),
                            producto.getNum_serie(), producto.getTipo(), producto.getAccesorios()))
                    .collect(Collectors.toList());

            clienteProductoDTO.setListaproductos(productosDTO);
            return clienteProductoDTO;
        } else {
            // Maneja el caso en que el cliente no se encuentra
            throw new RuntimeException("Cliente con CUIL " + cuil + " no encontrado");
      }
    }

    //Obtengo solo el id y nombres del cliente
    @Override
    public List<ClienteBasicoDTO> getClientesBasicos() {

        List<Cliente> listaClientes = this.obtenerClientesActivos();
        List<ClienteBasicoDTO> listaClientesBasicoDTO = new ArrayList<>();

        for(Cliente cliente: listaClientes){
            ClienteBasicoDTO c = new ClienteBasicoDTO();

            c.setId_cliente(cliente.getId_cliente());
            c.setNombre(cliente.getNombre());

            listaClientesBasicoDTO.add(c);
        }
        return listaClientesBasicoDTO;
    }


    //Método para devolver algunos datos del cliente y todos sus productos asociados
    @Override
    public List<ClienteProductoDTO> getClientesConProductos() {

        //Almaceno la lista de clientes que vienen desde la base de datos
        List<Cliente> listaClientes = this.obtenerClientesActivos();

        //Creo una lista vacía del tipo ClienteproductoDTO
        List<ClienteProductoDTO> listaClienteProductoDTO = new ArrayList<>();

        //Recorro la lista de clientes
        for (Cliente c: listaClientes){
            ClienteProductoDTO cliDTO = new ClienteProductoDTO();
            cliDTO.setId_cliente(c.getId_cliente());
            cliDTO.setDireccion(c.getDireccion());
            cliDTO.setTelefono(c.getTelefono());
            cliDTO.setNombreCompleto(c.getNombre());

            // transforma 1 lista de productos (List<Producto>) en una lista de objetos DTO (List<ProDTO>)
            List<ListaProductosDTO> productosDTO =  c.getListaproductos().stream()
                    .map(producto -> new ListaProductosDTO(producto.getId_producto(), producto.getMarca(), producto.getModelo(),
                                        producto.getNum_serie(), producto.getTipo(), producto.getAccesorios()))
                    .collect(Collectors.toList());

            cliDTO.setListaproductos(productosDTO);

            //Agrego a la listaDTO el objeto DTO
            listaClienteProductoDTO.add(cliDTO);
        }
        return listaClienteProductoDTO;
    }

    @Override
    public void saveCliente(Cliente cli) {
        cliRepo.save(cli);
    }

    @Override
    public Cliente editCliente(Long id, Cliente cli) {
        //Busco el cliente
        Cliente cliente = this.findCliente(id);
        //Seteo los datos que vienen de la BD
        cliente.setDireccion(cli.getDireccion());
        cliente.setNombre(cli.getNombre());
        cliente.setCuil(cli.getCuil());
        cliente.setEmail(cli.getEmail());
        cliente.setTelefono(cli.getTelefono());
        cliente.setPresupuestosCliente(cli.getPresupuestosCliente());
        cliente.setListaproductos(cli.getListaproductos());

        return cliRepo.save(cliente);
    }

    @Override
    public void deleteCliente(Long id) {
        Cliente cliente = this.findCliente(id);

        if(cliente != null) {
            //Marco el cliente como eliminado
            cliente.setDeleted(true);
            //Guardo el cliente en la BD
            cliRepo.save(cliente);
        }


    }
    @Override
    public Cliente findCliente(Long id) {
        return cliRepo.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {
        if(cliRepo.existsById(id)){
            return true;
        }else {
            return false;
        }
    }


}
