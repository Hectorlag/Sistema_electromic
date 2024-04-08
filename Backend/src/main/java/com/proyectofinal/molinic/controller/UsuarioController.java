package com.proyectofinal.molinic.controller;


import com.proyectofinal.molinic.model.Usuario;
import com.proyectofinal.molinic.service.IusuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IusuarioService usuServi;

    @GetMapping("/traer")
    public List<Usuario> getUsuarios(){
        return usuServi.obtenerUsuariosActivos();
    }
    @PostMapping("/crear")
    public ResponseEntity<String> saveUsuario(@Valid @RequestBody Usuario u, BindingResult bindingResult){

        // Maneja errores de validación
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }

        Optional<Usuario> usu = usuServi.buscarPorNombre(u.getNombre());
        if(usu.isPresent()){
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso. Por favor, elija otro.");
        }



        //Guarda el usuario sino existe
        usuServi.saveUsuario(u);
        return ResponseEntity.ok("Usuario registrado con éxito");
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editUsuario(@PathVariable Long id, @Valid @RequestBody Usuario u,
                               BindingResult bindingResult){
        // Maneja errores de validación
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Error de validación: " + bindingResult.getAllErrors());
        }else {

            //Actualiza los datos
            usuServi.editUsuario(id, u);

            return ResponseEntity.ok("Usuario actualizado con éxito");
        }

    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id){

        // Verifico si el usuario con el ID proporcionado existe en la base de datos
        if(usuServi.existsById(id)) {
            usuServi.deleteUsuario(id);
            return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
        }else{
            return  new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/traer/{id}")
    public ResponseEntity<Usuario> traerXid(@PathVariable Long id){

        Usuario u = usuServi.findUsuario(id);
        if(u != null){
            return ResponseEntity.ok(u);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //Logueo
    // Endpoint para la autenticación (login) de un usuario
    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@RequestBody Usuario usuario) {
        String nombreUsuario = usuario.getNombre();
        String contrasenia = usuario.getContrasenia();

        if (nombreUsuario == null || contrasenia == null || nombreUsuario.isEmpty() || contrasenia.isEmpty()) {
            return ResponseEntity.badRequest().body("Nombre de usuario y contraseña son obligatorios.");
        }

        if (usuServi.autenticarUsuario(usuario)) {
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    @GetMapping("traerXnombre/{nombre}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        Optional<Usuario> usuario = usuServi.buscarPorNombre(nombre);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            String mensaje = "No se encontró ningún usuario con el nombre '" + nombre + "'.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        }
    }



}
