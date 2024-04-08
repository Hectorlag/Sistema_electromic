package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IusuarioService {

    public List<Usuario> obtenerUsuariosActivos();

    public void saveUsuario(Usuario usu);

    public Usuario editUsuario(Long id, Usuario usu);

    public void deleteUsuario(Long id);

    public Usuario findUsuario (Long id);

    boolean existsById(Long id);

    boolean autenticarUsuario(Usuario usuario);

    Optional<Usuario> buscarPorNombre(String nombre);
}
