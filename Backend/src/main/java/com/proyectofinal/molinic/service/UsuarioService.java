package com.proyectofinal.molinic.service;

import com.proyectofinal.molinic.model.Usuario;
import com.proyectofinal.molinic.repository.IusuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IusuarioService{

    @Autowired
    private IusuarioRepository usuRepo;
    @Override
    public List<Usuario> obtenerUsuariosActivos() {

        return usuRepo.findByDeletedFalse();
    }

    @Override
    public void saveUsuario(Usuario usu) {
        usuRepo.save(usu);
    }

    @Override
    public Usuario editUsuario(Long id, Usuario usu) {
        //Busco el usuario
        Usuario u = this.findUsuario(id);
        u.setNombre(usu.getNombre());
        u.setContrasenia(usu.getContrasenia());
        return usuRepo.save(u);
    }

    @Override
    public void deleteUsuario(Long id) {

        Usuario usu = this.findUsuario(id);
        if(usu != null) {
            usu.setDeleted(true);
            //Guardo el Usuario en la BD
            usuRepo.save(usu);
        }
    }

    @Override
    public Usuario findUsuario(Long id) {

        return usuRepo.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {

        if(usuRepo.existsById(id)){
            return true;
        }else{
            return false;
        }
    }

    //Logueo del usuario
    @Override
    public boolean autenticarUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getContrasenia() == null) {
            return false;
        }

        Optional<Usuario> usuarioEncontrado = usuRepo.findByNombre(usuario.getNombre());

        return usuarioEncontrado.isPresent() && usuarioEncontrado.get().getContrasenia().equals(usuario.getContrasenia());
    }

    @Override
    public Optional<Usuario> buscarPorNombre(String nombre) {

        return usuRepo.findByNombre(nombre);
    }

}
